package net.catchpole.B9.devices.esc;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import net.catchpole.B9.devices.Device;

import java.io.IOException;

/*

Due to the length of the T100 cables and their possible capasitance problems,
I recommend changing the I2C speed of the Pi from 100khz down to something like 20khz.
On newer version of raspbian the way to do this is to edit /boot/config.txt and add this line..

dtparam=i2c1_baudrate=20000

On reboot, you should see the rate has changed from the default (it appears in the boot log)

Keep the leads as short as you can. As the BlueESC I2C is 5 volt and the Pi's I2C is 3.3v
you will need to use a level converter, and put I2C pull up resistors on the 5v side.

*/

public class BlueESC implements ESC, Device {
    private static final int BLUEESC_ADDRESS = 0x29;

    private final int device;
    private final boolean forwardPropeller;
    private volatile boolean stopping = false;
    private I2CBus bus;
    private I2CDevice i2CDevice;
    private volatile int lastThrottle = 0;
    private Thread updateThread;

    // the Blue Robotics thrusters (T100 etc) can be installed with a forward or reverse propeller
    public BlueESC(int device, boolean forwardPropeller) throws Exception {
        this.forwardPropeller = forwardPropeller;
        this.bus = I2CFactory.getInstance(I2CBus.BUS_1);
        this.device = device;
    }

    public void initialize() throws Exception {
        if (this.bus == null) {
            this.bus = I2CFactory.getInstance(I2CBus.BUS_1);
        }
        if (this.i2CDevice == null) {
            this.i2CDevice = bus.getDevice(BLUEESC_ADDRESS + device);
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    stopping = true;
                    try {
                        update(0);
                    } catch (IOException ioe) {
                    }
                }
            });

            this.updateThread = new Thread() {
                public void run() {
                    for (; ; ) {
                        try {
                            Thread.sleep(500);
                            update(lastThrottle);
                        } catch (InterruptedException ioe) {
                            // closing
                            return;
                        } catch (Throwable t) {
                        }
                    }
                }
            };
            this.updateThread.start();
        }
        resetEsc();
    }

    public void close() throws Exception {
        this.bus = null;
        this.i2CDevice = null;
        if (this.updateThread != null) {
            updateThread.interrupt();
            updateThread = null;
        }
    }

    @Override
    public boolean isHealthy() throws Exception {
        return true;
    }

    public void resetEsc() throws IOException {
        // thrusters wont initialize unless they are set to zero for a few seconds
        for (int x=0;x<2;x++) {
            update(0);
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
        }
    }

    public BlueESCData read() throws IOException {
        byte[] readBuffer = new byte[9];
        synchronized (this.bus) {
            if (i2CDevice.read(0x02, readBuffer, 0, readBuffer.length) != readBuffer.length) {
                throw new IOException(this.getClass().getSimpleName() + " read error");
            }
        }
        return new BlueESCData(
                getValue(readBuffer, 0),
                getValue(readBuffer, 2),
                getValue(readBuffer, 4),
                getValue(readBuffer, 6),
                readBuffer[8] & 0xff);
    }

    private int getValue(byte[] bytes, int offset) {
        return ((bytes[offset] & 0xff) << 8) | (bytes[offset+1] & 0xff);
    }

    public void update(int throttle) throws IOException {
        if (stopping) {
            return;
        }
        if (!this.forwardPropeller) {
            throttle = 0-throttle;
        }
        byte[] writeBuffer = new byte[2];
        writeBuffer[0] = (byte) (throttle >> 8);
        writeBuffer[1] = (byte) throttle;
        synchronized (this.bus) {
            i2CDevice.write(0, writeBuffer, 0, 2);
        }
    }

    @Override
    public String toString() {
        return "BlueESC " + device + " forwardPropeller=" + forwardPropeller;
    }
}