package net.catchpole.B9.codec;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Codec {
    void addType(Character character, Class clazz);

    byte[] encode(Object object) throws IOException;

    Object decode(byte[] bytes) throws IOException;

    void encode(Object object, OutputStream outputStream) throws IOException;

    Object decode(InputStream inputStream) throws IOException;
}