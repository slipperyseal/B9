package net.catchpole.B9.codec.transcoder;

import net.catchpole.B9.codec.stream.BitInputStream;
import net.catchpole.B9.codec.stream.BitOutputStream;

import java.io.IOException;

class ByteTranscoder implements TypeTranscoder<Byte> {
    public Byte read(BitInputStream in) throws IOException {
        if (!in.readBoolean()) {
            return 0;
        }
        return (byte)in.read(8);
    }

    public void write(BitOutputStream out, Byte value) throws IOException {
        out.writeBoolean(value != 0);
        if (value != 0) {
            out.write(value & 0xff, 8);
        }
    }
}
