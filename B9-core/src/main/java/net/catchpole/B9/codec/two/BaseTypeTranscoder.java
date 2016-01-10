package net.catchpole.B9.codec.two;

import net.catchpole.B9.codec.stream.BitInputStream;
import net.catchpole.B9.codec.stream.BitOutputStream;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseTypeTranscoder {
    private final Map<String,TypeTranscoder> transcoderMap = new HashMap<String, TypeTranscoder>();
    private final Map<String,Object> defaults = new HashMap<String, Object>();
    private final ObjectArrayTranscoder objectArrayTranscoder = new ObjectArrayTranscoder();
    private final ListTranscoder listTranscoder = new ListTranscoder();
    private final CodecStream codecStream;

    public BaseTypeTranscoder(CodecStream codecStream) {
        this.codecStream = codecStream;
        transcoderMap.put(Boolean.class.getName(), new BooleanTranscoder());
        transcoderMap.put(Boolean.TYPE.getName(), new BooleanTranscoder());
        transcoderMap.put(Byte.class.getName(), new ByteTranscoder());
        transcoderMap.put(Byte.TYPE.getName(), new ByteTranscoder());
        transcoderMap.put(Short.class.getName(), new ShortTranscoder());
        transcoderMap.put(Short.TYPE.getName(), new ShortTranscoder());
        transcoderMap.put(Character.class.getName(), new CharacterTranscoder());
        transcoderMap.put(Character.TYPE.getName(), new CharacterTranscoder());
        transcoderMap.put(Integer.class.getName(), new IntegerTranscoder());
        transcoderMap.put(Integer.TYPE.getName(), new IntegerTranscoder());
        transcoderMap.put(Long.class.getName(), new LongTranscoder());
        transcoderMap.put(Long.TYPE.getName(), new LongTranscoder());
        transcoderMap.put(Float.class.getName(), new FloatTranscoder());
        transcoderMap.put(Float.TYPE.getName(), new FloatTranscoder());
        transcoderMap.put(Double.class.getName(), new DoubleTranscoder());
        transcoderMap.put(Double.TYPE.getName(), new DoubleTranscoder());
        transcoderMap.put(String.class.getName(), new StringTranscoder());

        defaults.put(Boolean.class.getName(), false);
        defaults.put(Boolean.TYPE.getName(), false);
        defaults.put(Byte.class.getName(), (byte)0);
        defaults.put(Byte.TYPE.getName(), (byte)0);
        defaults.put(Short.class.getName(), (short)0);
        defaults.put(Short.TYPE.getName(), (short)0);
        defaults.put(Character.class.getName(), (char)0);
        defaults.put(Character.TYPE.getName(), (char)0);
        defaults.put(Integer.class.getName(), 0);
        defaults.put(Integer.TYPE.getName(), 0);
        defaults.put(Long.class.getName(), 0l);
        defaults.put(Long.TYPE.getName(), 0l);
        defaults.put(Float.class.getName(), 0.0f);
        defaults.put(Float.TYPE.getName(), 0.0f);
        defaults.put(Double.class.getName(), 0.0d);
        defaults.put(Double.TYPE.getName(), 0.0d);
    }

    public TypeTranscoder getTranscoder(Class clazz) {
        if (clazz.isAssignableFrom(List.class)) {
            return listTranscoder;
        }
        if (clazz.isArray()) {
            return objectArrayTranscoder;
        }
        return transcoderMap.get(clazz.getName());
    }

    public Object getDefault(Class type) {
        return defaults.get(type.getName());
    }

    class BooleanTranscoder implements TypeTranscoder<Boolean> {
        public Boolean read(BitInputStream in) throws IOException {
            return in.readBoolean();
        }

        public void write(BitOutputStream out, Boolean value) throws IOException {
            out.writeBoolean(value);
        }
    }

    class ByteTranscoder implements TypeTranscoder<Byte> {
        public Byte read(BitInputStream in) throws IOException {
            return (byte)in.read(8);
        }

        public void write(BitOutputStream out, Byte value) throws IOException {
            out.write(value & 0xff, 8);
        }
    }

    class ShortTranscoder implements TypeTranscoder<Short> {
        public Short read(BitInputStream in) throws IOException {
            return (short)in.read(16);
        }

        public void write(BitOutputStream out, Short value) throws IOException {
            out.write(value & 0xffff, 16);
        }
    }

    class CharacterTranscoder implements TypeTranscoder<Character> {
        public Character read(BitInputStream in) throws IOException {
            return (char)in.read(16);
        }

        public void write(BitOutputStream out, Character value) throws IOException {
            out.write(value & 0xffff, 16);
        }
    }

    class IntegerTranscoder implements TypeTranscoder<Integer> {
        public Integer read(BitInputStream in) throws IOException {
            return in.read(32);
        }

        public void write(BitOutputStream out, Integer value) throws IOException {
            out.write(value, 32);
        }
    }

    class LongTranscoder implements TypeTranscoder<Long> {
        public Long read(BitInputStream in) throws IOException {
            return in.readLong(64);
        }

        public void write(BitOutputStream out, Long value) throws IOException {
            out.writeLong(value, 64);
        }
    }

    class FloatTranscoder implements TypeTranscoder<Float> {
        public Float read(BitInputStream in) throws IOException {
            return Float.intBitsToFloat(in.read(32));
        }

        public void write(BitOutputStream out, Float value) throws IOException {
            out.write(Float.floatToIntBits(value), 32);
        }
    }

    class DoubleTranscoder implements TypeTranscoder<Double> {
        public Double read(BitInputStream in) throws IOException {
            return Double.longBitsToDouble(in.readLong(64));
        }

        public void write(BitOutputStream out, Double value) throws IOException {
            out.writeLong(Double.doubleToLongBits(value), 64);
        }
    }

    class StringTranscoder implements TypeTranscoder<String> {
        public String read(BitInputStream in) throws IOException {
            int len = in.read(32);
            byte[] data = new byte[len];
            in.readFully(data);
            return new String(data, "utf-8");
        }

        public void write(BitOutputStream out, String value) throws IOException {
            byte[] data = value.getBytes("utf-8");
            out.write(data.length, 32);
            out.write(data);
        }
    }

    class ObjectArrayTranscoder implements TypeTranscoder<Object[]> {
        public Object[] read(BitInputStream in) throws IOException {
            int len = in.read(32);
            Object[] objects = new Object[len];
            for (int x=0;x<len;x++) {
                try {
                    if (in.readBoolean()) {
                        objects[x] = codecStream.decode(in);
                    }
                } catch (Exception e) {
                    throw new IOException(e);
                }
            }
            return objects;
        }

        public void write(BitOutputStream out, Object[] value) throws IOException {
            out.write(value.length, 32);
            for (Object o : value) {
                out.writeBoolean(o != null);
                if (o != null) {
                    try {
                        codecStream.encode(o, out);
                    } catch (Exception e) {
                        throw new IOException(e);
                    }
                }
            }
        }
    }

    class ListTranscoder implements TypeTranscoder<List>  {
        public List read(BitInputStream in) throws IOException {
            return Arrays.asList(objectArrayTranscoder.read(in));
        }

        public void write(BitOutputStream out, List value) throws IOException {
            objectArrayTranscoder.write(out, value.toArray());
        }
    }
}
