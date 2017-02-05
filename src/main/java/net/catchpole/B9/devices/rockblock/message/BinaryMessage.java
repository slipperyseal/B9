package net.catchpole.B9.devices.rockblock.message;

public class BinaryMessage {
    private byte[] data;
    private byte[] encoded;
    private int checkSum;

    public BinaryMessage(byte[] data) {
        this.data = data;
        this.encoded = new byte[data.length + 2];
        int checksum = 0;
        int i=0;
        for (byte b : data) {
            checksum += b & 0xff;
            encoded[i] = data[i];
            i++;
        }
        this.checkSum = checksum;
        this.encoded[this.encoded.length-2] = (byte)(checksum >> 8);
        this.encoded[this.encoded.length-1] = (byte)checksum;
    }

    public byte[] getData() {
        return this.data;
    }

    public byte[] getEncoded() {
        return this.encoded;
    }

    public int getCheckSum() {
        return this.checkSum;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (byte b : encoded) {
            sb.append(String.format("%02X ", (int)b));
        }
        return sb.toString().trim();
    }
}
