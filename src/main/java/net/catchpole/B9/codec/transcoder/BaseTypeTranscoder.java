package net.catchpole.B9.codec.transcoder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BaseTypeTranscoder {
    private final Map<String,TypeTranscoder> transcoderMap = new HashMap<String, TypeTranscoder>();

    private final BooleanTranscoder booleanTranscoder = new BooleanTranscoder();
    private final ByteTranscoder byteTranscoder = new ByteTranscoder();
    private final ShortTranscoder shortTranscoder = new ShortTranscoder();
    private final CharacterTranscoder characterTranscoder = new CharacterTranscoder();
    private final IntegerTranscoder integerTranscoder = new IntegerTranscoder();
    private final LongTranscoder longTranscoder = new LongTranscoder(integerTranscoder);
    private final FloatTranscoder floatTranscoder = new FloatTranscoder();
    private final DoubleTranscoder doubleTranscoder = new DoubleTranscoder();
    private final ByteArrayTranscoder byteArrayTranscoder = new ByteArrayTranscoder();
    private final StringTranscoder stringTranscoder = new StringTranscoder(byteArrayTranscoder);
    private final ObjectArrayTranscoder objectArrayTranscoder = new ObjectArrayTranscoder(this, integerTranscoder);
    private final ListTranscoder listTranscoder = new ListTranscoder(objectArrayTranscoder);
    private final SetTranscoder setTranscoder = new SetTranscoder(objectArrayTranscoder);
    private final MapTranscoder mapTranscoder = new MapTranscoder(objectArrayTranscoder);

    private final TypeTranscoder[] baseTranscoders = new TypeTranscoder[] {
            booleanTranscoder, byteTranscoder, shortTranscoder, characterTranscoder,
            integerTranscoder, longTranscoder, floatTranscoder, doubleTranscoder,
            stringTranscoder, listTranscoder, setTranscoder, mapTranscoder,
            byteArrayTranscoder, objectArrayTranscoder
    };

    private BeanTranscoder beanTranscoder;

    public BaseTypeTranscoder() {
        transcoderMap.put(Boolean.class.getName(), booleanTranscoder);
        transcoderMap.put(Boolean.TYPE.getName(), booleanTranscoder);
        transcoderMap.put(Byte.class.getName(), byteTranscoder);
        transcoderMap.put(Byte.TYPE.getName(), byteTranscoder);
        transcoderMap.put(Short.class.getName(), shortTranscoder);
        transcoderMap.put(Short.TYPE.getName(), shortTranscoder);
        transcoderMap.put(Character.class.getName(), characterTranscoder);
        transcoderMap.put(Character.TYPE.getName(), characterTranscoder);
        transcoderMap.put(Integer.class.getName(), integerTranscoder);
        transcoderMap.put(Integer.TYPE.getName(), integerTranscoder);
        transcoderMap.put(Long.class.getName(), longTranscoder);
        transcoderMap.put(Long.TYPE.getName(), longTranscoder);
        transcoderMap.put(Float.class.getName(), floatTranscoder);
        transcoderMap.put(Float.TYPE.getName(), floatTranscoder);
        transcoderMap.put(Double.class.getName(), doubleTranscoder);
        transcoderMap.put(Double.TYPE.getName(), doubleTranscoder);
        transcoderMap.put(String.class.getName(), stringTranscoder);
    }

    public void setBeanTranscoder(BeanTranscoder beanTranscoder) {
        this.beanTranscoder = beanTranscoder;
    }

    public BeanTranscoder getBeanTranscoder() {
        return this.beanTranscoder;
    }

    public int getBaseTranscoderIndex(TypeTranscoder typeTranscoder) {
        int x=0;
        for (TypeTranscoder t : baseTranscoders) {
            if (t == typeTranscoder) {
                return x;
            }
            x++;
        }
        throw new IllegalArgumentException(typeTranscoder.getClass().getName());
    }

    public TypeTranscoder getBaseTranscoder(int index) {
        return baseTranscoders[index];
    }

    public TypeTranscoder getTranscoder(Class type) {
        if (List.class.isAssignableFrom(type)) {
            return listTranscoder;
        }
        if (Set.class.isAssignableFrom(type)) {
            return setTranscoder;
        }
        if (Map.class.isAssignableFrom(type)) {
            return mapTranscoder;
        }
        if (type.isArray()) {
            return objectArrayTranscoder;
        }
        return transcoderMap.get(type.getName());
    }

}