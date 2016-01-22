package net.catchpole.B9.devices.rockblock;

import net.catchpole.B9.devices.serial.SerialConnection;
import net.catchpole.B9.devices.serial.binding.ResponseDataListener;

import java.io.IOException;

public class AtSession {
    private ResponseDataListener responseDataListener;
    private SerialConnection serialConnection;

    public AtSession(ResponseDataListener responseDataListener, SerialConnection serialConnection) {
        this.responseDataListener = responseDataListener;
        this.serialConnection = serialConnection;
    }

    public String atCommand(String command) throws IOException {
        this.serialConnection.write((command + '\r').getBytes());
        String result = null;
        for (int x=0;x<4;x++) {
            String value = responseDataListener.readString();
            //System.out.println(x + " >" + value + "<");
            if (x == 1) {
                result = value;
            }

            if (value.equals("OK")) {
                return result;
            }
            if (value.equals("ERROR")) {
                throw new IOException("ERROR");
            }
        }
        throw new IOException("no valid response");
    }
}
