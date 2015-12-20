package net.catchpole.B9.devices.esc;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

public class BlueESC implements ESC {
    private final I2CBus bus;
    private final I2CDevice i2CDevice;
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
        this.i2CDevice = bus.getDevice(0x29 + device);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                stopping = true;
                update(0);
            }
        });
    }

    public void initialize() {
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
        } catch (Exception e) {
            return null;
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
        } catch (Exception e) {
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