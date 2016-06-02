package net.catchpole.B9.codec.stream;

import java.io.IOException;
import java.io.OutputStream;

public class BitOutputStream extends OutputStream {
    private static int[] masks = new BitMasks().createBitMasks(32+1);
    private static long[] masksLong = new BitMasks().createBitMasksLong(64+1);
    private OutputStream outputStream;
    private int value;
    private int used;

    public BitOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public void write(int b) throws IOException {
        this.write(b, 8);
    }

    public void write(int value, int bits) throws IOException {
        if (bits > 32 || bits < 1) {
            throw new IllegalArgumentException();
        }
        value &= masks[bits];
        while (bits > 8) {
            writeByte((value >> (bits -= 8)) & 0xff, 8);
        }
        writeByte(value & masks[bits], bits);
    }

    public void writeLong(long value, int bits) throws IOException {
        if (bits > 64 || bits < 1) {
            throw new IllegalArgumentException();
        }
        value &= masksLong[bits];
        while (bits > 8) {
            writeByte((int)(value >> (bits -= 8)) & 0xff, 8);
        }
        writeByte((int)value & masks[bits], bits);
    }

    public void writeBoolean(boolean value) throws IOException {
        write(value ? 1 : 0, 1);
    }

    private void writeByte(int b, int bits) throws IOException {
        value |= (b << used);
        used += bits;
        if (used >= 8) {
            used -= 8;
            outputStream.write(value);
            value >>= 8;
        }
    }

    @Override
    public void flush() throws IOException {
        if (used > 0) {
            outputStream.write(value);
            used = 0;
        }
    }
}
