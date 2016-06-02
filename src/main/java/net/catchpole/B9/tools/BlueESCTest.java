package net.catchpole.B9.tools;

import net.catchpole.B9.devices.esc.BlueESC;

import java.util.ArrayList;
import java.util.List;

/**
 * Don't forget to use a voltage level converter when connecting the BlueESC I2C interface to the Pi.
 * You will also need to add pull-up resistors to the 5 volt side of the level converts.
 */

// increments the speed of a BlueESC until it reaches the target speed
public class BlueESCTest {
    public static void main(String[] args) throws Exception {
        int firstDevice = Integer.parseInt(args[0]);
        int totalDevices = Integer.parseInt(args[1]);
        int targeThrottle = Integer.parseInt(args[2]);
        int increment = Integer.parseInt(args[3]);

        List<BlueESC> devices = new ArrayList<>();
        for (int x=0;x<totalDevices;x++) {
            devices.add(new BlueESC(firstDevice + x, false));
        }

        for (BlueESC blueESC : devices) {
            blueESC.initialize();
        }
        int velocity=0;
        for (;;) {
            for (BlueESC blueESC : devices) {
                blueESC.update(velocity);
            }

            System.out.println();
            for (BlueESC blueESC : devices) {
                System.out.println(blueESC.toString() + '\t' + velocity + '\t' + blueESC.read());
            }
            Thread.sleep(200);
            if (targeThrottle > 0) {
                if (velocity < targeThrottle) {
                    velocity += increment;
                }
            }
            if (targeThrottle < 0) {
                if (velocity > targeThrottle) {
                    velocity -= increment;
                }
            }
        }
    }
}
