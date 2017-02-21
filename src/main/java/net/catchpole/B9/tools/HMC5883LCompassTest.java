package net.catchpole.B9.tools;

import net.catchpole.B9.devices.compass.HMC5883LCompass;

public class HMC5883LCompassTest {
    public static void main(String[] args) throws Exception {
        HMC5883LCompass hmc5883LCompass = new HMC5883LCompass();
        hmc5883LCompass.initialize();

        for (;;) {
            try {
                Thread.sleep(250);
                System.out.print(hmc5883LCompass.isHealthy());
                System.out.print('\t');
                System.out.println(hmc5883LCompass.getHeading());
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName() + ' ' + e.getMessage());
            }
        }
    }
}