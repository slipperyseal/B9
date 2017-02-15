package net.catchpole.B9.devices.thrusters;

import net.catchpole.B9.devices.esc.BlueESCData;

import java.io.IOException;

public interface Thrusters {
    void update(double left, double right) throws IOException;

    BlueESCData getLeftData() throws IOException;

    BlueESCData getRightData() throws IOException;
}
