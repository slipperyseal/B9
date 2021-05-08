package net.catchpole.B9.tools;

import net.catchpole.B9.devices.level.I2CLevelReader;

public class I2CLevelReaderlTest {
    private static I2CLevelReader i2CLevelReader = new I2CLevelReader();

    public static void main(String[] args) throws Exception {
        i2CLevelReader.initialize();
        for (;;) {
            System.out.println(scale(i2CLevelReader.readLevel(0)) + "\t" + scale(i2CLevelReader.readLevel(1)));
            Thread.sleep(200);
            i2CLevelReader.blink(0);
            Thread.sleep(200);
            i2CLevelReader.blink(1);
        }
    }

    private static double scale(int value) {
        return value * 0.019526627d;
    }
}
