package net.catchpole.B9.devices.thrusters;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

// experimental first draft of the Blue Robotics Blue ESC T100 Thruster controller - might not work :/ awkward
// wish me luck!
public class T100Thruster {
    private final I2CBus bus;
    private final I2CDevice device;
    private final byte[] readBuffer = new byte[9];
    private final byte[] writeBuffer = new byte[2];

    public T100Thruster(int device) throws Exception {
        this.bus = I2CFactory.getInstance(I2CBus.BUS_1);
        this.device = bus.getDevice(0x29 + device);
    }

    public void testMethod(int velocity) {
        for (;;) {
            try {
                update(0);
                read();
                Thread.sleep(1000);

                update(velocity);
                read();
                Thread.sleep(1000);
            } catch (Exception e) {
                System.out.println(e);
                try {
                    Thread.sleep(1000);
                } catch (Exception t) {
                }
            }
        }
    }

    private int getValue(byte[] bytes, int offset) {
        return ((bytes[offset] & 0xff) << 8) | (bytes[offset+1] & 0xff);
    }

    private void read() throws IOException {
        int len = device.read(0x00, readBuffer, 0, readBuffer.length);
        if (len == readBuffer.length) {
            int pulse = getValue(readBuffer, 0);
            int voltage = getValue(readBuffer, 2);
            int temperature = getValue(readBuffer, 4);
            int current = getValue(readBuffer, 6);
            int identifier = readBuffer[8] & 0xff;
            System.out.println("pulse: " + pulse + " voltage: " + voltage + " temperature: " + temperature + " current: " + current + " identifier: " + identifier);
        }
    }

    private void update(int throttle) throws IOException {
        writeBuffer[0] = (byte)(throttle >> 8);
        writeBuffer[1] = (byte)throttle;
        device.write(0,writeBuffer,0,2);
    }
}