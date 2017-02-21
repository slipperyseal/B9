package net.catchpole.B9.codec.transcoder;

import net.catchpole.B9.codec.stream.BitInputStream;
import net.catchpole.B9.codec.stream.BitOutputStream;

import java.io.IOException;

class DoubleTranscoder implements TypeTranscoder<Double> {
    public Double read(BitInputStream in) throws IOException {
        if (!in.readBoolean()) {
            return 0.0d;
        }
        if (in.readBoolean()) {
            // parse string version to avoid conversion errors
            return Double.parseDouble(new Float(Float.intBitsToFloat(in.read(32))).toString());
        } else {
            return Double.longBitsToDouble(in.readLong(64));
        }
    }

    public void write(BitOutputStream out, Double value) throws IOException {
        boolean zero = "0.0".equals(value.toString());
        out.writeBoolean(!zero);
        if (!zero) {
            Float floatValue = value.floatValue();
            boolean equalsFloat = value.toString().equals(floatValue.toString());
            out.writeBoolean(equalsFloat);
            if (equalsFloat) {
                out.write(Float.floatToIntBits(floatValue), 32);
            } else {
                out.writeLong(Double.doubleToLongBits(value), 64);
            }
        }
    }
}
