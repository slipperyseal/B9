package net.catchpole.B9.devices.thrusters;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

// experimental first draft of the Blue Robotics Blue ESC T100 Thruster controller - might not work :/ awkward
// wish me luck!
// IT WORKS!
public class T100Thruster {
    private final I2CBus bus;
    private final I2CDevice device;
    private final byte[] readBuffer = new byte[9];
    private final byte[] writeBuffer = new byte[2];

    public T100Thruster(int device) throws Exception {
        this.bus = I2CFactory.getInstance(I2CBus.BUS_1);
        this.device = bus.getDevice(0x29 + device);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    update(0);
                } catch (Exception e) {
                }
            }
        });
    }

    public void testMethod(int velocity) {
        int v=0;
        try {
            for (int x=0;x<4;x++) {
                update(0);
                Thread.sleep(1000);
            }
        } catch (Exception ioe) {
            ioe.printStackTrace();
        }
        System.out.println("Go..");
        for (;;) {
            try {
                if (velocity > 0) {
                    if (v < velocity) {
                        v += 50;
                    }
                } else {
                    if (v > velocity) {
                        v -= 50;
                    }
                }
                System.out.println(v);
                update(v);
                read();
                Thread.sleep(250);
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
        int len = device.read(0x02, readBuffer, 0, readBuffer.length);
        if (len == readBuffer.length) {
            int pulse = getValue(readBuffer, 0);
            int voltage = getValue(readBuffer, 2);
            int temperature = getValue(readBuffer, 4);
            int current = getValue(readBuffer, 6);
            int identifier = readBuffer[8] & 0xff;

            float volts = ((float)voltage)/65536.0f * 5.0f * 6.45f;
            float amps = (((float)current)-32767.0f)/65535.0f * 5.0f * 14.706f;
            System.out.println("pulse: " + pulse + " voltage: " + volts + " temperature: " + temp((float)temperature) + " current: " + amps + " identifier: " + identifier);
        }
    }

    // resistance at 25 degrees C
    private static final float THERMISTORNOMINAL = 10000;
    // temp. for nominal resistance (almost always 25 C)
    private static final float TEMPERATURENOMINAL = 25;
    // The beta coefficient of the thermistor (usually 3000-4000
    private static final float BCOEFFICIENT = 3900;
    // the value of the 'other' resistor
    private static final float SERIESRESISTOR = 3300;

    private double temp(float temp) {
        float resistance = SERIESRESISTOR/(65535/temp-1);
        double steinhart;
        steinhart = resistance / THERMISTORNOMINAL;  // (R/Ro)
        steinhart = Math.log(steinhart);             // ln(R/Ro)
        steinhart /= BCOEFFICIENT;                   // 1/B * ln(R/Ro)
        steinhart += 1.0 / (TEMPERATURENOMINAL + 273.15); // + (1/To)
        steinhart = 1.0 / steinhart;                 // Invert
        steinhart -= 273.15;                         // convert to C
        return steinhart;
    }

    private void update(int throttle) throws IOException {
        writeBuffer[0] = (byte)(throttle >> 8);
        writeBuffer[1] = (byte)throttle;
        device.write(0,writeBuffer,0,2);
    }

    public static void main(String[] args) throws Exception {
        // pass it a speed +/- 32768
        new T100Thruster(0).testMethod(Integer.parseInt(args[0]));
    }
}