package net.catchpole.B9.devices.rockblock;

import net.catchpole.B9.devices.rockblock.message.Reception;
import net.catchpole.B9.devices.rockblock.message.ShortBurstDataInitiateSession;
import net.catchpole.B9.devices.rockblock.message.ShortBurstDataStatusExtended;
import net.catchpole.B9.devices.serial.DataListener;
import net.catchpole.B9.devices.serial.SerialConnection;
import net.catchpole.B9.devices.serial.SerialPort;

import java.io.IOException;

public class RockBlock {
    private SerialPort serialPort;
    private AtSession atSession;
    private SerialConnection serialConnection;
    private DataListener dataListener;

    public RockBlock(SerialPort serialPort) {
        this.serialPort = serialPort;
    }

    public void setDataListener(DataListener dataListener) {
        this.dataListener = dataListener;
    }

    public void connect() throws Exception {
        this.serialConnection = serialPort.openConnection(19200);
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

    public void sendBinaryMessage(byte[] data) throws IOException {
        atSession.atCommandWriteBinary("AT+SBDWB=" + data.length, data);
    }

    public void sendAndReceive() throws IOException {
        waitForReception();

        ShortBurstDataInitiateSession shortBurstDataInitiateSession = null;
        do {
            waitForReception();
            shortBurstDataInitiateSession = this.initiateSession();
            System.out.println(shortBurstDataInitiateSession);

            if (shortBurstDataInitiateSession.getTerminatedStatus() == 1) {
                byte[] data = this.readBinaryMessage();
                if (data.length > 0 && dataListener != null) {
                    dataListener.receive(data, data.length);
                }
            }
        } while (shortBurstDataInitiateSession.getGatewayQueuedCount() > 0);
    }

    public byte[] readBinaryMessage() throws IOException {
        byte[] data = atSession.atCommandReadBinary("AT+SBDRB");
        clearReceivingBuffer();
        return data;
    }

    public ShortBurstDataStatusExtended getStatus() throws IOException {
        return new ShortBurstDataStatusExtended(getData(atSession.atCommandResult("AT+SBDSX")));
    }

    public boolean waitForReception() throws IOException {
        return waitForReception(1000 * 60 * 3, 2);
    }

    public boolean waitForReception(long timeout, int minimumReception) throws IOException {
        long testUntil = System.currentTimeMillis() + timeout;
        do {
            if (hasReception(minimumReception)) {
                return true;
            }
            sleep(2000);
        } while (System.currentTimeMillis() < testUntil);
        return false;
    }

    public ShortBurstDataInitiateSession initiateSession() throws IOException {
        ShortBurstDataInitiateSession shortBurstDataInitiateSession = new ShortBurstDataInitiateSession(
                getData(atSession.atCommandResult("AT+SBDIX")));
        clearSendingBuffer();
        return shortBurstDataInitiateSession;
    }

    private void clearSendingBuffer() throws IOException {
        atSession.atCommandResult("AT+SBDD0");
    }

    private void clearReceivingBuffer() throws IOException {
        atSession.atCommandResult("AT+SBDD1");
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

    public void close() throws IOException {
        this.serialConnection.close();
    }
}
