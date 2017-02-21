package net.catchpole.B9.codec.transcoder;

import net.catchpole.B9.codec.stream.BitInputStream;
import net.catchpole.B9.codec.stream.BitOutputStream;

import java.io.IOException;

class ShortTranscoder implements TypeTranscoder<Short> {
    public Short read(BitInputStream in) throws IOException {
        if (!in.readBoolean()) {
            return 0;
        }
        return (short)in.read(16);
    }

    public void write(BitOutputStream out, Short value) throws IOException {
        out.writeBoolean(value != 0);
        if (value != 0) {
            out.write(value & 0xffff, 16);
        }
    }
}
