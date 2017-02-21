package net.catchpole.B9.codec.transcoder;

import net.catchpole.B9.codec.stream.BitInputStream;
import net.catchpole.B9.codec.stream.BitMasks;
import net.catchpole.B9.codec.stream.BitOutputStream;

import java.io.IOException;

class ByteArrayTranscoder implements TypeTranscoder<byte[]> {
    private final BitMasks bitMasks = new BitMasks();

    @Override
    public byte[] read(BitInputStream in) throws IOException {
        if (!in.readBoolean()) {
            return new byte[0];
        }
        int len = in.readBoolean() ? in.readSigned(16) : in.readSigned(32);
        byte[] value = new byte[len];

        int bits = in.read(3);
        int base = in.read(8);
        if (bits == 0) {
            for (int x = 0; x < len; x++) {
                value[x] = (byte)base;
            }
        } else {
            for (int x = 0; x < len; x++) {
                value[x] = (byte) (in.read(bits) + base);
            }
        }
        return value;
    }

    @Override
    public void write(BitOutputStream out, byte[] value) throws IOException {
        out.writeBoolean(value.length != 0);
        if (value.length != 0) {
            boolean fitsShort = value.length >= Short.MIN_VALUE && value.length <= Short.MAX_VALUE;
            out.writeBoolean(fitsShort);
            out.write(value.length, fitsShort ? 16 : 32);

            int base = min(value);
            int bits = bitMasks.bitsRequired(max(value) - base);
            out.write(bits, 3);
            out.write(base, 8);
            if (bits != 0) {
                for (int b : value) {
                    out.write((b & 0xff) - base, bits);
                }
            }
        }
    }

    private int min(byte[] data) {
        int value = 0xff;
        for (int b : data) {
            b &= 0xff;
            if (b <= value) {
                value = b;
            }
        }
        return value;
    }

    private int max(byte[] data) {
        int value = 0;
        for (int b : data) {
            b &= 0xff;
            if (b >= value) {
                value = b;
            }
        }
        return value;
    }
}
