package net.catchpole.B9.codec.transcoder;

import net.catchpole.B9.codec.stream.BitInputStream;
import net.catchpole.B9.codec.stream.BitOutputStream;

import java.io.IOException;

class BooleanTranscoder implements TypeTranscoder<Boolean> {
    public Boolean read(BitInputStream in) throws IOException {
        return in.readBoolean();
    }

    public void write(BitOutputStream out, Boolean value) throws IOException {
        out.writeBoolean(value);
    }
}
