package net.catchpole.B9.devices.thrusters;

import net.catchpole.B9.devices.esc.BlueESC;
import net.catchpole.B9.devices.esc.BlueESCData;

import java.io.IOException;

public class BlueESCThrusters implements Thrusters {
    private BlueESC blueESCLeft;
    private BlueESC blueESCRight;
    private double left;
    private double right;

    public BlueESCThrusters(BlueESC blueESCLeft, BlueESC blueESCRight) throws Exception {
        this.blueESCLeft = blueESCLeft;
        this.blueESCRight = blueESCRight;
    }

    public synchronized void update(double leftFraction, double rightFraction) throws IOException {
        this.left = leftFraction;
        this.right = rightFraction;
        this.blueESCLeft.update(this.left);
        this.blueESCRight.update(this.right);
    }

    public synchronized void updateEsc() throws IOException {
        this.blueESCLeft.update(this.left);
        this.blueESCRight.update(this.right);
    }

    public synchronized BlueESCData getLeftData() throws IOException {
        return blueESCLeft.read();
    }

    public synchronized BlueESCData getRightData() throws IOException {
        return blueESCRight.read();
    }
}
