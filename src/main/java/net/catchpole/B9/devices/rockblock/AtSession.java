package net.catchpole.B9.devices.rockblock;

import net.catchpole.B9.devices.rockblock.message.BinaryMessage;
import net.catchpole.B9.devices.serial.SerialConnection;

import java.io.DataInputStream;
import java.io.IOException;

public class AtSession {
    private DataInputStream dataInputStream;
    private SerialConnection serialConnection;

    public AtSession(SerialConnection serialConnection) throws IOException {
        this.serialConnection = serialConnection;
        this.dataInputStream = new DataInputStream(serialConnection.getInputStream());
        eatNoise();
    }

    public void eatNoise() throws IOException {
        int avail = this.dataInputStream.available();
        if (avail > 0) {
            // consume any noise, ring alerts etc.
            this.dataInputStream.skipBytes(avail);
        }
    }

    public void atCommand(String command) throws IOException {
        this.serialConnection.write((command + '\r').getBytes());
        this.dataInputStream.readLine();
        this.dataInputStream.readLine();
        String ok = this.dataInputStream.readLine();
        if (!ok.equals("OK")) {
            throw new IOException("Expected OK: '" + ok + "'");
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
            throw new IOException("Expected OK: '" + ok + "'");
        }
        System.out.println("RESULT: " + result);
        return result;
    }

    public void atCommandWriteBinary(String command, byte[] data) throws IOException {
        this.serialConnection.write((command + '\r').getBytes());
        this.dataInputStream.readLine();
        this.dataInputStream.readLine();
        String ready = this.dataInputStream.readLine();
        if (!ready.equals("READY")) {
            throw new IOException("Expected READY: '" + ready + "'");
        }
        BinaryMessage binaryMessage = new BinaryMessage(data);
        serialConnection.write(binaryMessage.getEncoded());
        this.dataInputStream.readLine();
        String result = this.dataInputStream.readLine();
        if (!result.equals("0")) {
            throw new IOException("Write binary error code: " + result);
        }
        this.dataInputStream.readLine();
        String ok = this.dataInputStream.readLine();
        if (!ok.equals("OK")) {
            throw new IOException("Expected OK: '" + ok + "'");
        }
    }

    public byte[] atCommandReadBinary(String command) throws IOException {
        this.serialConnection.write((command + '\r').getBytes());
        this.dataInputStream.readLine();

        int len = dataInputStream.readShort() & 0xffff;
        if (len > 1024) {
            throw new IOException("Invalid message length " + len);
        }
        byte[] data = new byte[len];
        dataInputStream.readFully(data);
        BinaryMessage binaryMessage = new BinaryMessage(data);
        if (binaryMessage.getCheckSum() != (dataInputStream.readShort() & 0xffff)) {
            throw new IOException("Checksum error");
        }

        this.dataInputStream.readLine();
        String ok = this.dataInputStream.readLine();
        if (!ok.equals("OK")) {
            throw new IOException("Expected OK: '" + ok + "'");
        }
        return data;
    }

    private void debugData() throws IOException {
        int b;
        while ((b=dataInputStream.read()) != -1) {
            System.out.println(" " + b + " '" + (char)((b >= ' ' && b <= '~') ? b : '.') + "'");
        }
    }
}
