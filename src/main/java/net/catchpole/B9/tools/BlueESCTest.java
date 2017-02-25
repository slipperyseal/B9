package net.catchpole.B9.tools;

import net.catchpole.B9.devices.esc.BlueESC;
import net.catchpole.B9.lang.Arguments;

import java.util.ArrayList;
import java.util.List;

/**
 * Don't forget to use a voltage level converter when connecting the BlueESC I2C interface to the Pi.
 * You will also need to add pull-up resistors to the 5 volt side of the level converts.
 */

// increments the speed of a BlueESC until it reaches the target speed
public class BlueESCTest {
    public static void main(String[] args) throws Exception {
        Arguments arguments = new Arguments(args);
        int firstDevice = arguments.getArgumentProperty("-firstDevice", 0);
        int totalDevices = arguments.getArgumentProperty("-totalDevices", 1);
        int targetThrottle = arguments.getArgumentProperty("-targetThrottle", 1000);
        int increment = arguments.getArgumentProperty("-increment", 100);
        boolean statusOnly = arguments.hasArgument("-statusonly");

        List<BlueESC> devices = new ArrayList<BlueESC>();
        for (int x=0;x<totalDevices;x++) {
            devices.add(new BlueESC(firstDevice + x, false));
        }

        for (BlueESC blueESC : devices) {
            blueESC.initialize();
        }

        if (statusOnly) {
            for (;;) {
                for (BlueESC blueESC : devices) {
                    blueESC.update(0);
                    System.out.println(blueESC.toString() + '\t' + blueESC.read());
                }
                Thread.sleep(500);
            }
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
            if (targetThrottle > 0) {
                if (velocity < targetThrottle) {
                    velocity += increment;
                }
            }
            if (targetThrottle < 0) {
                if (velocity > targetThrottle) {
                    velocity -= increment;
                }
            }
        }
    }
}
