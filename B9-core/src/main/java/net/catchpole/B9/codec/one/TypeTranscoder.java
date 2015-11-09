package net.catchpole.B9.codec.one;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface TypeTranscoder<V> {
    V read(DataInputStream dis, Flags flags) throws IOException;

    void write(DataOutputStream dis, V value, Flags flags) throws IOException;

    int getFlagCount();
}
