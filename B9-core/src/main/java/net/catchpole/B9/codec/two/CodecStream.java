package net.catchpole.B9.codec.two;

import net.catchpole.B9.codec.Codec;
import net.catchpole.B9.codec.stream.BitInputStream;
import net.catchpole.B9.codec.stream.BitOutputStream;

import java.io.IOException;

public interface CodecStream extends Codec {
    void addType(Character character, Class clazz);

    void encode(Object object, BitOutputStream bitOutputStream) throws IOException;

    Object decode(BitInputStream bitInputStream) throws IOException;
}