package net.catchpole.B9.devices.serial;

import java.io.IOException;

public interface SerialPort {
    SerialConnection openConnection(int baud) throws IOException, InterruptedException;
}
