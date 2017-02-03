package net.catchpole.B9.devices.serial;

import java.io.IOException;
import java.io.InputStream;

public interface SerialConnection {
    void write(byte[] data) throws IOException;

    void close() throws IOException;

    InputStream getInputStream() throws IOException;

    void setDataListener(DataListener dataListener);
}
