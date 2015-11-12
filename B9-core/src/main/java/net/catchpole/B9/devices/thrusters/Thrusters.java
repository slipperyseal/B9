package net.catchpole.B9.devices.thrusters;

import net.catchpole.B9.devices.esc.BlueESCData;

public interface Thrusters {
    void update(double left, double right);

    BlueESCData getLeftData();

    BlueESCData getRightData();
}
