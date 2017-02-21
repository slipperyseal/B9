package net.catchpole.B9.codec.transcoder;

import net.catchpole.B9.codec.stream.BitInputStream;
import net.catchpole.B9.codec.stream.BitOutputStream;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

class SetTranscoder implements TypeTranscoder<Set>  {
    private ObjectArrayTranscoder objectArrayTranscoder;

    public SetTranscoder(ObjectArrayTranscoder objectArrayTranscoder) {
        this.objectArrayTranscoder = objectArrayTranscoder;
    }

    public Set read(BitInputStream in) throws IOException {
        return new HashSet(Arrays.asList(objectArrayTranscoder.read(in)));
    }

    public void write(BitOutputStream out, Set value) throws IOException {
        objectArrayTranscoder.write(out, value.toArray());
    }
}
