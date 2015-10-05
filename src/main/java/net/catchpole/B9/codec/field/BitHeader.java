package net.catchpole.B9.codec.field;

import java.util.BitSet;

public class BitHeader implements Flags {
    private final BitSet bitSet;
    private int index = 0;

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
}
