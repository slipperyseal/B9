package net.catchpole.B9.codec.stream;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

// todo: support for reading signed values
public class BitInputStream extends InputStream {
    private static int[] masks = new BitMasks().createBitMasks(32+1);

    public InputStream inputStream;
    private int value;
    private int remain;

    public BitInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public int read() throws IOException {
        return read(8);
    }

    public long readLong(int bits) throws IOException {
        if (bits > 64 || bits < 1) {
            throw new IllegalArgumentException();
        }
        long value = 0;
        while (bits > 8) {
            value |= ((long)readByte(8) << (bits -= 8));
        }
        return value | readByte(bits);
    }

    public int read(int bits) throws IOException {
        if (bits > 32 || bits < 1) {
            throw new IllegalArgumentException();
        }
        int value = 0;
        while (bits > 8) {
            value |= (readByte(8) << (bits -= 8));
        }
        return value | readByte(bits);
    }

    private int readByte(int bits) throws IOException {
        if (remain == 0) {
            value = inputStream.read();
            if (value == -1) {
                throw new EOFException();
            }
            remain = 8;
        }
        if (bits <= remain) {
            int result = value & masks[bits];
            value >>= bits;
            remain -= bits;
            return result;
        } else {
            int result = value;
            bits -= remain;

            value = inputStream.read();
            if (value == -1) {
                throw new EOFException();
            }
            result |= ((value & masks[bits])<<remain);
            value >>= bits;
            remain = 8-bits;
            return result;
        }
    }

    public boolean readBoolean() throws IOException {
        return read(1) == 1;
    }

    public final void readFully(byte b[]) throws IOException {
        readFully(b, 0, b.length);
    }

    public final void readFully(byte b[], int off, int len) throws IOException {
        if (len < 0)
            throw new IndexOutOfBoundsException();
        int n = 0;
        while (n < len) {
            int count = read(b, off + n, len - n);
            if (count < 0)
                throw new EOFException();
            n += count;
        }
    }
}