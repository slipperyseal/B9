package net.catchpole.B9.devices.serial;

import com.pi4j.io.serial.SerialPortException;

import java.io.IOException;

public class PiSerialPort implements SerialPort {
    private String port;

    public PiSerialPort() {
    }

    public PiSerialPort(String port) {
        this.port = port;
    }

    @Override
    public SerialConnection openConnection(int baud) throws IOException {
        try {
            return new PiSerialConnection(port, baud);
        } catch (SerialPortException serialPortException) {
            throw new IOException(serialPortException);
        } catch (InterruptedException interruptedException) {
            throw new IOException(interruptedException);
        }
    }
}
