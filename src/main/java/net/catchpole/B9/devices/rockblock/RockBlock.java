package net.catchpole.B9.devices.rockblock;

import net.catchpole.B9.devices.rockblock.message.Reception;
import net.catchpole.B9.devices.rockblock.message.ShortBurstDataInitiateSession;
import net.catchpole.B9.devices.rockblock.message.ShortBurstDataStatusExtended;
import net.catchpole.B9.devices.serial.SerialConnection;
import net.catchpole.B9.devices.serial.SerialPort;

import java.io.IOException;

public class RockBlock {
    private SerialPort serialPort;
    private AtSession atSession;

    public RockBlock(SerialPort serialPort) {
        this.serialPort = serialPort;
    }

    public void connect() throws Exception {
        SerialConnection serialConnection = serialPort.openConnection(19200);
        this.atSession = new AtSession(serialConnection);

        // a simple AT to clear any serial buffers
        try {
            atSession.atCommand("AT");
        } catch (IOException e) {
            atSession.atCommand("AT");
        }
        atSession.atCommand("AT&D0");   // ignore the DTR input
        atSession.atCommand("AT&K0");   // disable RTS/CTS
    }

    public void sendTestMessage(String text) throws IOException {
        atSession.atCommand("AT+SBDWT=" + text);
    }

    public ShortBurstDataStatusExtended getStatus() throws IOException {
        return new ShortBurstDataStatusExtended(getData(atSession.atCommandResult("AT+SBDSX")));
    }

    public boolean waitForReception() throws IOException {
        for (int x=0;x<6*5;x++) {
            if (hasReception()) {
                return true;
            }
            sleep(10000);
        }
        return false;
    }

    public ShortBurstDataInitiateSession initiateSession() throws IOException {
        return new ShortBurstDataInitiateSession(getData(atSession.atCommandResult("AT+SBDIX")));
    }

    public boolean hasReception() throws IOException {
        return hasReception(2);
    }

    public boolean hasReception(int minimumReception) throws IOException {
        Reception reception = new Reception(getData(atSession.atCommandResult("AT+CSQ")));
        System.out.println(reception);
        return reception.getReception() >= minimumReception;
    }

    private String[] getData(String response) {
        String line = response.substring(response.indexOf(":") + 1);
        String[] values = line.split(",");
        for (int x=0;x<values.length;x++) {
            values[x] = values[x].trim();
        }
        return values;
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException i) {
        }
    }
}
