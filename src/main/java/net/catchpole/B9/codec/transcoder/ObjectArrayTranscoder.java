package net.catchpole.B9.codec.transcoder;

import net.catchpole.B9.codec.stream.BitInputStream;
import net.catchpole.B9.codec.stream.BitOutputStream;

import java.io.IOException;

class ObjectArrayTranscoder implements TypeTranscoder<Object[]> {
    private BaseTypeTranscoder baseTypeTranscoder;
    private IntegerTranscoder integerTranscoder;

    public ObjectArrayTranscoder(BaseTypeTranscoder baseTypeTranscoder, IntegerTranscoder integerTranscoder) {
        this.baseTypeTranscoder = baseTypeTranscoder;
        this.integerTranscoder = integerTranscoder;
    }

    public Object[] read(BitInputStream in) throws IOException {
        int len = integerTranscoder.read(in);
        Object[] objects = new Object[len];
        for (int x=0;x<len;x++) {
            // has value
            if (in.readBoolean()) {
                // base type
                if (in.readBoolean()) {
                    TypeTranscoder typeTranscoder = baseTypeTranscoder.getBaseTranscoder(in.read(4));
                    objects[x] = typeTranscoder.read(in);
                } else {
                    objects[x] = baseTypeTranscoder.getBeanTranscoder().read(in);
                }
            }
        }
        return objects;
    }

    public void write(BitOutputStream out, Object[] value) throws IOException {
        integerTranscoder.write(out, value.length);
        for (Object element : value) {
            // has value
            out.writeBoolean(element != null);
            if (element != null) {
                TypeTranscoder typeTranscoder = baseTypeTranscoder.getTranscoder(element.getClass());
                out.writeBoolean(typeTranscoder != null);
                // base type
                if (typeTranscoder != null) {
                    out.write(baseTypeTranscoder.getBaseTranscoderIndex(typeTranscoder), 4);
                    typeTranscoder.write(out, element);
                } else {
                    baseTypeTranscoder.getBeanTranscoder().write(out, element);
                }
            }
        }
    }
}
