package net.catchpole.B9.codec;

import java.io.IOException;

public interface Codec {
    void addType(Character character, Class clazz);

    byte[] encode(Object object) throws IOException;

    Object decode(byte[] bytes) throws IOException;
}