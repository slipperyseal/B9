package net.catchpole.B9.devices.serial;

import com.pi4j.io.serial.*;

public abstract class PiCommPort {
    private final Serial serial = SerialFactory.createInstance();
    private final StringBuilder dataBuffer = new StringBuilder();

    public PiCommPort() throws SerialPortException {
        this(Serial.DEFAULT_COM_PORT, 9600);
    }

    public PiCommPort(String port, int baud) throws SerialPortException {
        this.serial.addListener(new DataListener());
        this.serial.open(port, baud);
    }

    public void writeln(String line) {
        this.serial.writeln(line);
    }

    public abstract void process(String line);

    class DataListener implements SerialDataListener {
        // we might not receive entire lines in serial events
        // we buffer received strings and then push complete lines to process() as they become available
        public void dataReceived(SerialDataEvent event) {
            String data = event.getData();
            dataBuffer.append(data);

            int cr;
            while ((cr= dataBuffer.indexOf("\n")) != -1) {
                String line = dataBuffer.substring(0,cr);
                dataBuffer.delete(0,cr+1);
                process(line);
            }
        }
    }
}