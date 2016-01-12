package net.catchpole.B9.codec;

import net.catchpole.B9.codec.one.Types;
import net.catchpole.B9.codec.stream.BitInputStream;
import net.catchpole.B9.codec.stream.BitOutputStream;
import net.catchpole.B9.codec.transcoder.BaseTypeTranscoder;
import net.catchpole.B9.codec.transcoder.BeanTranscoder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

// codec two uses bit streams and will replace codec one. but it's not finished
public class CodecTwo {
    private final Types types = new Types();
    private final BaseTypeTranscoder baseTypeTranscoder = new BaseTypeTranscoder();
    private final BeanTranscoder beanTranscoder = new BeanTranscoder(baseTypeTranscoder, types);
    {
        baseTypeTranscoder.setBeanTranscoder(beanTranscoder);
    }

    public void addType(Character character, Class clazz) {
        try {
            this.types.addType(character, clazz);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public byte[] encode(Object object) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BitOutputStream bitOutputStream = new BitOutputStream(byteArrayOutputStream);
        beanTranscoder.write(bitOutputStream, object);
        bitOutputStream.flush();
        return byteArrayOutputStream.toByteArray();
    }

    public Object decode(byte[] bytes) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        return beanTranscoder.read(new BitInputStream(byteArrayInputStream));
    }
}
