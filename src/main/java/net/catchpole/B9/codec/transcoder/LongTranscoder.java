package net.catchpole.B9.codec.transcoder;

import net.catchpole.B9.codec.stream.BitInputStream;
import net.catchpole.B9.codec.stream.BitOutputStream;

import java.io.IOException;

class LongTranscoder implements TypeTranscoder<Long> {
    private IntegerTranscoder integerTranscoder;

    public LongTranscoder(IntegerTranscoder integerTranscoder) {
        this.integerTranscoder = integerTranscoder;
    }

    public Long read(BitInputStream in) throws IOException {
        long lower = ((long)integerTranscoder.read(in)) & 0xffffffffL;
        long upper = ((long)integerTranscoder.read(in)) & 0xffffffffL;
        return lower | (upper<<32);
    }

    public void write(BitOutputStream out, Long value) throws IOException {
        integerTranscoder.write(out, (int) (long) value);
        integerTranscoder.write(out, (int) (value >> 32));
    }
}
