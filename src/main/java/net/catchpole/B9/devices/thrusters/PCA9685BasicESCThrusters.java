package net.catchpole.B9.devices.thrusters;

import net.catchpole.B9.devices.esc.BlueESCData;
import net.catchpole.B9.devices.esc.ESC;
import net.catchpole.B9.devices.esc.PCA9685BasicESC;

import java.io.IOException;

public class PCA9685BasicESCThrusters implements Thrusters {
    private ESC leftThuster;
    private ESC rightThuster;

    public PCA9685BasicESCThrusters(PCA9685BasicESC pca9685BasicESC, int leftChannel, int rightChannel) {
        this.leftThuster = pca9685BasicESC.createThruster(leftChannel);
        this.rightThuster = pca9685BasicESC.createThruster(rightChannel);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException i) {
        }
    }
    
    @Override
    public void update(double left, double right) throws IOException {
        this.leftThuster.update(left);
        this.rightThuster.update(right);
    }

    @Override
    public BlueESCData getLeftData() throws IOException {
        return null;
    }

    @Override
    public BlueESCData getRightData() throws IOException {
        return null;
    }
}
