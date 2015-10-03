package net.catchpole.B9.tools;

import net.catchpole.B9.devices.esc.BlueESC;

// increments the speed of a BlueESC until it reaches the target speed
public class BlueESCTest {
    public static void main(String[] args) throws Exception {
        int device = Integer.parseInt(args[0]);
        int target = Integer.parseInt(args[1]);
        int increment = Integer.parseInt(args[2]);

        BlueESC blueESC0 = new BlueESC(device, false);
        BlueESC blueESC1 = new BlueESC(device+1, false);

        blueESC0.initialize();
        blueESC1.initialize();
        int velocity=0;
        for (;;) {
            blueESC0.update(velocity);
            blueESC1.update(velocity);
            System.out.println();
            System.out.println("0: " + velocity + " " + blueESC0.read());
            System.out.println("1: " + velocity + " " + blueESC1.read());
            Thread.sleep(200);
            if (target > 0) {
                if (velocity < target) {
                    velocity += increment;
                }
            }
            if (target < 0) {
                if (velocity > target) {
                    velocity -= increment;
                }
            }
        }
    }
}
