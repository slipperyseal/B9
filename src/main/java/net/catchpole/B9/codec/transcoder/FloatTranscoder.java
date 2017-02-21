package net.catchpole.B9.codec.transcoder;

import net.catchpole.B9.codec.stream.BitInputStream;
import net.catchpole.B9.codec.stream.BitOutputStream;

import java.io.IOException;

class FloatTranscoder implements TypeTranscoder<Float> {
    public Float read(BitInputStream in) throws IOException {
        if (!in.readBoolean()) {
            return 0.0f;
        }
        return Float.intBitsToFloat(in.read(32));
    }

    public void write(BitOutputStream out, Float value) throws IOException {
        boolean zero = "0.0".equals(value.toString());
        out.writeBoolean(!zero);
        if (!zero) {
            out.write(Float.floatToIntBits(value), 32);
        }
    }
}
