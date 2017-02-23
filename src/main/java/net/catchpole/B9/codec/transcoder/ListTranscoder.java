package net.catchpole.B9.codec.transcoder;

import net.catchpole.B9.codec.stream.BitInputStream;
import net.catchpole.B9.codec.stream.BitOutputStream;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

class ListTranscoder implements TypeTranscoder<List>, FieldInterceptor<List>  {
    private ObjectArrayTranscoder objectArrayTranscoder;

    public ListTranscoder(ObjectArrayTranscoder objectArrayTranscoder) {
        this.objectArrayTranscoder = objectArrayTranscoder;
    }

    public List read(BitInputStream in) throws IOException {
        return Arrays.asList(objectArrayTranscoder.read(in));
    }

    public void write(BitOutputStream out, List value) throws IOException {
        objectArrayTranscoder.write(out, value.toArray());
    }

    public List intercept(List currentValue, List newValue) {
        if (currentValue != null && currentValue.isEmpty()) {
            currentValue.addAll(newValue);
            return currentValue;
        } else {
            return newValue;
        }
    }
}
