package net.catchpole.B9.tools;

import net.catchpole.B9.devices.serial.DataListener;
import net.catchpole.B9.devices.serial.PiSerialPort;
import net.catchpole.B9.devices.serial.SerialConnection;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class CommLogger {
    public CommLogger(int baud) throws IOException, InterruptedException {
        SerialConnection serialConnection = new PiSerialPort().openConnection(baud);
        serialConnection.setDataListener(new DataListener() {
            @Override
            public void receive(byte[] data, int len) {
                try {
                    System.out.println(new String(data, 0, len, "utf-8"));
                } catch (UnsupportedEncodingException ioe) {
                }
            }
        });
    }

    public static void main(String[] args) throws Exception {
        CommLogger commLogger = new CommLogger(9600);

        for (;;) {
            Thread.sleep(Long.MAX_VALUE);
        }
    }
}
