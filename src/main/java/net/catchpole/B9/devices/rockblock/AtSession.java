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

    public void eatNoise() throws IOException {
        int avail = this.dataInputStream.available();
        if (avail > 0) {
            // consume any noise, ring alerts etc.
            this.dataInputStream.skipBytes(avail);
        }
    }

    public String atCommand(String command) throws IOException {
        return atCommandWriteBinary(command, null);
    }

    public String atCommandWriteBinary(String command, byte[] data) throws IOException {
        String result = null;
        this.serialConnection.write((command + '\r').getBytes());
        for (;;) {
            String line = this.dataInputStream.readLine();
            if (line.isEmpty() || line.equals("SBDRING")) {
                continue;
            }
            if (line.equals("OK")) {
                return result;
            }
            if (line.equals("ERROR")) {
                throw new IOException(command + " returned ERROR");
            }
            if (line.equals("READY")) {
                int checkSum = getChecksum(data);
                byte[] check = new byte[2];
                check[0] = (byte)(checkSum >> 8);
                check[1] = (byte)checkSum;
                serialConnection.write(data);
                serialConnection.write(check);
                continue;
            }
            if (result != null) {
                result = result + "\r\n" + line;
            } else {
                result = line;
            }
        }
    }

    public byte[] atCommandReadBinary(String command) throws IOException {
        this.serialConnection.write((command + '\r').getBytes());

        int len = dataInputStream.readShort() & 0xffff;
        if (len > 1024) {
            throw new IOException("Invalid message length " + len);
        }
        byte[] data = new byte[len];
        dataInputStream.readFully(data);
        if (getChecksum(data) != (dataInputStream.readShort() & 0xffff)) {
            throw new IOException("Checksum error");
        }

        for (;;) {
            String line = this.dataInputStream.readLine();
            if (line.isEmpty() || line.equals("SBDRING")) {
                continue;
            }
            if (line.equals("OK")) {
                return data;
            }
            if (line.equals("ERROR")) {
                throw new IOException(command + " returned ERROR");
            }
        }
    }

    private int getChecksum(byte[] data) {
        int checksum = 0;
        for (byte b : data) {
            checksum += b & 0xff;
        }
        return checksum;
    }

    private void debugData() throws IOException {
        int b;
        while ((b=dataInputStream.read()) != -1) {
            System.out.println(" " + b + " '" + (char)((b >= ' ' && b <= '~') ? b : '.') + "'");
        }
    }
}
