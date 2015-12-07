package net.catchpole.B9.devices.serial;

import com.pi4j.io.serial.*;
import net.catchpole.B9.devices.gps.command.LineWriter;

public class PiCommPort implements LineWriter {
    private final Serial serial = SerialFactory.createInstance();
    private final StringBuilder dataBuffer = new StringBuilder();
    private final DataListener dataListener;
    private final LineWriter lineWriter;

    public PiCommPort(int baud) throws SerialPortException {
        this(baud, null);
    }

    public PiCommPort(int baud, LineWriter lineWriter) throws SerialPortException {
        this.lineWriter = lineWriter;
        if (lineWriter != null) {
            this.dataListener = new DataListener();
            this.serial.addListener(dataListener);
        } else {
            this.dataListener = null;
        }
        this.serial.open(Serial.DEFAULT_COM_PORT, baud);
    }

    public void close() {
        if (dataListener != null) {
            this.serial.removeListener(dataListener);
        }
        this.serial.close();
    }

    public void writeLine(String line) {
        this.serial.writeln(line);
    }

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
                lineWriter.writeLine(line);
            }
        }
    }
}
