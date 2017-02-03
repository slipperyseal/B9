package net.catchpole.B9.devices.rockblock;

import net.catchpole.B9.devices.serial.SerialConnection;

import java.io.DataInputStream;
import java.io.IOException;

public class AtSession {
    private DataInputStream dataInputStream;
    private SerialConnection serialConnection;

    public AtSession(SerialConnection serialConnection) throws IOException {
        this.serialConnection = serialConnection;
        this.dataInputStream = new DataInputStream(serialConnection.getInputStream());
    }

    public void atCommand(String command) throws IOException {
        this.serialConnection.write((command + '\r').getBytes());
        this.dataInputStream.readLine();
        this.dataInputStream.readLine();
        String ok = this.dataInputStream.readLine();
        if (!ok.equals("OK")) {
            throw new IOException("ERROR");
        }
    }

    public String atCommandResult(String command) throws IOException {
        this.serialConnection.write((command + '\r').getBytes());
        this.dataInputStream.readLine();
        this.dataInputStream.readLine();
        String result = this.dataInputStream.readLine();
        this.dataInputStream.readLine();
        String ok = this.dataInputStream.readLine();
        if (!ok.equals("OK")) {
            throw new IOException("ERROR");
        }
        return result;
    }
}
