package net.catchpole.B9.devices.serial;

import com.pi4j.io.serial.*;

import java.io.IOException;
import java.io.InputStream;

public class PiSerialConnection implements SerialConnection {
    private final Serial serial = SerialFactory.createInstance();
    private SerialDataEventListener serialDataEventListener;

    public PiSerialConnection(String port, int baud) throws SerialPortException, InterruptedException, IOException  {
        SerialConfig config = new SerialConfig();
        config.device(port != null ? port : com.pi4j.io.serial.SerialPort.getDefaultPort())
                .baud(Baud.getInstance(baud))
                .dataBits(DataBits._8)
                .parity(Parity.NONE)
                .stopBits(StopBits._1)
                .flowControl(FlowControl.NONE);
        this.serial.open(config);
    }

    public void close() throws IOException {
        if (this.serialDataEventListener != null) {
            this.serial.removeListener(serialDataEventListener);
            this.serialDataEventListener = null;
        }
        this.serial.close();
    }

    public void write(byte[] data) throws IOException {
        this.serial.write(data);
    }

    public InputStream getInputStream() throws IOException {
        return serial.getInputStream();
    }

    public void setDataListener(final DataListener dataListener) {
        this.serial.addListener(this.serialDataEventListener = new SerialDataEventListener() {
            public void dataReceived(SerialDataEvent event) {
                try {
                    byte[] data = event.getBytes();
                    dataListener.receive(data, data.length);
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        });
    }
}
