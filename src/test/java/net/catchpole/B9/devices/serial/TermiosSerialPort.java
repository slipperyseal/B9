package net.catchpole.B9.devices.serial;

import java.io.IOException;

public class TermiosSerialPort implements SerialPort {
    private String port;

    public TermiosSerialPort(String port) {
        this.port = port;
    }

    @Override
    public SerialConnection openConnection(int baud, DataListener dataListener) throws IOException{
        return new TermiosSerialConnection(port, baud, dataListener);
    }
}
