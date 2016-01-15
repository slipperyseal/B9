package net.catchpole.B9.devices.serial;

import com.pi4j.io.serial.*;

import java.io.IOException;

public class PiSerialConnection implements SerialConnection {
    private final Serial serial = SerialFactory.createInstance();
    private final PiSerialDataListener piSerialDataListener = new PiSerialDataListener();
    private final DataListener dataListener;

    public PiSerialConnection(String port, int baud, DataListener dataListener) throws SerialPortException {
        this.dataListener = dataListener;
        if (dataListener != null) {
            this.serial.addListener(piSerialDataListener);
        }
        this.serial.open(port == null ? Serial.DEFAULT_COM_PORT : port, baud);
    }

    public void close() {
        if (piSerialDataListener != null) {
            this.serial.removeListener(piSerialDataListener);
        }
        this.serial.close();
    }

    public void write(byte[] data) throws IOException {
        this.serial.write(data);
    }

    class PiSerialDataListener implements SerialDataListener {
        public void dataReceived(SerialDataEvent event) {
            byte[] data = event.getData().getBytes();
            dataListener.receive(data, data.length);
        }
    }
}
