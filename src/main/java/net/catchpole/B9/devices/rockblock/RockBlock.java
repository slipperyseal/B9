package net.catchpole.B9.devices.rockblock;

import net.catchpole.B9.devices.Device;
import net.catchpole.B9.devices.rockblock.message.Reception;
import net.catchpole.B9.devices.rockblock.message.ShortBurstDataInitiateSession;
import net.catchpole.B9.devices.rockblock.message.ShortBurstDataStatusExtended;
import net.catchpole.B9.devices.serial.DataListener;
import net.catchpole.B9.devices.serial.SerialConnection;
import net.catchpole.B9.devices.serial.SerialPort;

import java.io.IOException;

public class RockBlock implements Device {
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

    public void initialize() throws IOException {
        try {
            this.serialConnection = serialPort.openConnection(19200);
        } catch (Exception e) {
            throw new IOException(e);
        }
        this.atSession = new AtSession(serialConnection);

        atSession.eatNoise();
        try {
            atSession.atCommand("ATE0");
        } catch (IOException e) {
            atSession.atCommand("ATE0");
        }
//        atSession.atCommand("AT+SBDMTA0");   // disable unsolicited SBDRING alerts to the serial
        atSession.atCommand("AT&D0");   // ignore DTR
        atSession.atCommand("AT&K0");   // disable RTS/CTS
    }

    public boolean isHealthy() throws Exception {
        return true;
    }

    public String getManufacturer() throws IOException {
        return atSession.atCommand("AT+CGMI");
    }

    public String getModel() throws IOException {
        return atSession.atCommand("AT+CGMM");
    }

    public String getRevision() throws IOException {
        return atSession.atCommand("AT+CGMR");
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
        return new ShortBurstDataStatusExtended(splitResponseValues(atSession.atCommand("AT+SBDSX")));
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
                splitResponseValues(atSession.atCommand("AT+SBDIX")));
        clearSendingBuffer();
        return shortBurstDataInitiateSession;
    }

    public void clearSendingBuffer() throws IOException {
        atSession.atCommand("AT+SBDD0");
    }

    public void clearReceivingBuffer() throws IOException {
        atSession.atCommand("AT+SBDD1");
    }

    public boolean hasReception() throws IOException {
        return hasReception(2);
    }

    public boolean hasReception(int minimumReception) throws IOException {
        Reception reception = new Reception(splitResponseValues(atSession.atCommand("AT+CSQ")));
        System.out.println(reception);
        return reception.getReception() >= minimumReception;
    }

    private String[] splitResponseValues(String response) {
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
