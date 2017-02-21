package net.catchpole.B9.codec.transcoder;

import net.catchpole.B9.codec.stream.BitInputStream;
import net.catchpole.B9.codec.stream.BitOutputStream;

import java.io.IOException;

class StringTranscoder implements TypeTranscoder<String> {
    private ByteArrayTranscoder byteArrayTranscoder;

    public StringTranscoder(ByteArrayTranscoder byteArrayTranscoder) {
        this.byteArrayTranscoder = byteArrayTranscoder;
    }

    public String read(BitInputStream in) throws IOException {
        return new String(byteArrayTranscoder.read(in), "utf-8");
    }

    public void write(BitOutputStream out, String value) throws IOException {
        byteArrayTranscoder.write(out, value.getBytes("utf-8"));
    }
}
