package net.catchpole.B9.devices.thrusters;

import net.catchpole.B9.devices.esc.BlueESC;
import net.catchpole.B9.devices.esc.BlueESCData;

public class BlueESCThrusters implements Thrusters {
    private BlueESC blueESCLeft;
    private BlueESC blueESCRight;
    private int left;
    private int right;

    public BlueESCThrusters() throws Exception {
        this.blueESCLeft = new BlueESC(0, false);
        this.blueESCRight = new BlueESC(1, false);
        this.blueESCLeft.initialize();
        this.blueESCRight.initialize();

        // the blueesc will shut down if it hasn't received an update in a while
        new Thread() {
            public void run() {
                for (; ; ) {
                    try {
                        Thread.sleep(500);
                        updateEsc();
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public synchronized void update(double leftFraction, double rightFraction) {
        this.left = (int)(32768*leftFraction);
        this.right = ((int)(32768*rightFraction));
        this.blueESCLeft.update(this.left);
        this.blueESCRight.update(this.right);
    }

    public synchronized void updateEsc() {
        this.blueESCLeft.update(this.left);
        this.blueESCRight.update(this.right);
    }

    public synchronized BlueESCData getLeftData() {
        return blueESCLeft.read();
    }

    public synchronized BlueESCData getRightData() {
        return blueESCRight.read();
    }
}
