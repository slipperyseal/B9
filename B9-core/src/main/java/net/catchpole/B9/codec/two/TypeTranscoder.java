package net.catchpole.B9.codec.two;

import net.catchpole.B9.codec.stream.BitInputStream;
import net.catchpole.B9.codec.stream.BitOutputStream;

import java.io.IOException;

public interface TypeTranscoder<V> {
    V read(BitInputStream in) throws IOException;

    void write(BitOutputStream out, V value) throws IOException;
}
