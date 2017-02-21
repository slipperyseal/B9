package net.catchpole.B9.codec.transcoder;

import net.catchpole.B9.codec.stream.BitInputStream;
import net.catchpole.B9.codec.stream.BitOutputStream;

import java.io.IOException;

class IntegerTranscoder implements TypeTranscoder<Integer> {
    public Integer read(BitInputStream in) throws IOException {
        if (!in.readBoolean()) {
            return 0;
        }
        return in.readBoolean() ? in.readSigned(16) : in.readSigned(32);
    }

    public void write(BitOutputStream out, Integer value) throws IOException {
        out.writeBoolean(value != 0);
        if (value != 0) {
            boolean fitsShort = value >= Short.MIN_VALUE && value <= Short.MAX_VALUE;
            out.writeBoolean(fitsShort);
            out.write(value, fitsShort ? 16 : 32);
        }
    }
}
