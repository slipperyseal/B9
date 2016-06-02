package net.catchpole.B9.devices.serial;

import java.io.IOException;

public interface SerialConnection {
    void write(byte[] data) throws IOException;

    void close();
}
