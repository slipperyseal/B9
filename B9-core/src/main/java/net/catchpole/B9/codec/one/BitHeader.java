package net.catchpole.B9.codec.one;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.BitSet;

public class BitHeader implements Flags {
    private final BitSet bitSet;
    private int index = 0;

    public BitHeader(DataInputStream dis, int bits) throws IOException {
        byte[] data = new byte[expectedLength(bits)];
        dis.readFully(data);
        this.bitSet = BitSet.valueOf(data);
    }

    public BitHeader(byte[] bytes) {
        this.bitSet = BitSet.valueOf(bytes);
    }

    public BitHeader() {
        this.bitSet = new BitSet();
    }

    public boolean readFlag() {
        return bitSet.get(index++);
    }

    public void writeFlag(boolean value) {
        bitSet.set(index++, value);
    }

    public int getSize() {
        return index;
    }

    public byte[] toByteArray() {
        byte[] bits = bitSet.toByteArray();
        int expectedLength = ((index+7)/8);
        if (bits.length < expectedLength) {
            byte[] result = new byte[expectedLength];
            System.arraycopy(bits, 0, result, 0, bits.length);
            return result;
        } else {
            return bits;
        }
    }

    public int expectedLength(int bits) {
        return ((bits+7)/8);
    }
}
