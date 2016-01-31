package net.catchpole.B9.devices.esc;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

/*

Due to the length of the T100 cables and their possible capasitance problems,
I recommend changing the I2C speed of the Pi from 100khz down to something like 20khz.
On newer version of raspbian the way to do this is to edit /boot/config.txt and add this line..

dtparam=i2c1_baudrate=20000

On reboot, you should see the rate has changed from the default.

Keep the leads as short as you can. As the BlueESC I2C is 5 volt and the Pi's I2C is 3.3v
you will need to use level converters, and put I2C pull up resistors on the 5v side.
Also, i'm going to put each thruster behind it's own level converter, to isolate the capasitance of each cable
(that's my theory) and see how that goes.

*/

public class BlueESC implements ESC {
    private final I2CBus bus;
    private I2CDevice i2CDevice;
    private final int device;
    private final byte[] readBuffer = new byte[9];
    private final byte[] writeBuffer = new byte[2];
    private final boolean forwardPropeller;
    private volatile boolean stopping = false;

    // the Blue Robotics thrusters (T100 etc) can be installed with a forward or reverse propeller
    public BlueESC(int device, boolean forwardPropeller) throws Exception {
        this.forwardPropeller = forwardPropeller;
        this.bus = I2CFactory.getInstance(I2CBus.BUS_1);
        this.device = device;
        this.init();
    }

    private void init() {
        try {
            if (this.i2CDevice == null) {
                this.i2CDevice = bus.getDevice(0x29 + device);
                Runtime.getRuntime().addShutdownHook(new Thread() {
                    @Override
                    public void run() {
                        stopping = true;
                        update(0);
                    }
                });
            }
        } catch (Throwable t) {
            System.err.println(BlueESC.class.getName() + " " + t.getClass() + " " + t.getMessage());
        }
    }

    public void initialize() {
        init();
        // thrusters wont initialize unless they are set to zero for a few seconds
        for (int x=0;x<2;x++) {
            update(0);
            this.sleep(1000);
        }
    }

    public BlueESCData read() {
        try {
            if (i2CDevice.read(0x02, readBuffer, 0, readBuffer.length) == readBuffer.length) {
                return new BlueESCData(
                        getValue(readBuffer, 0),
                        getValue(readBuffer, 2),
                        getValue(readBuffer, 4),
                        getValue(readBuffer, 6),
                        readBuffer[8] & 0xff);
            }
        } catch (Throwable t) {
            System.err.println(BlueESC.class.getName() + " " + t.getClass() + " " + t.getMessage());
        }
        return null;
    }

    private int getValue(byte[] bytes, int offset) {
        return ((bytes[offset] & 0xff) << 8) | (bytes[offset+1] & 0xff);
    }

    public boolean update(int throttle) {
        if (throttle != 0 && stopping) {
            return false;
        }
        if (!this.forwardPropeller) {
            throttle = 0-throttle;
        }
        try {
            writeBuffer[0] = (byte) (throttle >> 8);
            writeBuffer[1] = (byte) throttle;
            synchronized (bus) {
                i2CDevice.write(0, writeBuffer, 0, 2);
            }
            return true;
        } catch (Throwable t) {
            System.err.println(BlueESC.class.getName() + " " + t.getClass() + " " + t.getMessage());
            return false;
        }
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
        }
    }

    @Override
    public String toString() {
        return "BlueESC " + device + " forwardPropeller=" + forwardPropeller;
    }
}