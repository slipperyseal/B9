package net.catchpole.B9.codec.one;

import net.catchpole.B9.codec.CodecOne;

import java.io.DataInputStream;
import java.io.DataOutputStream;
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
    private final CodecOne codec;

    public BaseTypeTranscoder(CodecOne codec) {
        this.codec = codec;
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
        public Boolean read(DataInputStream dis, Flags flags) throws IOException {
            return flags.readFlag();
        }

        public void write(DataOutputStream dos, Boolean value, Flags flags) throws IOException {
            flags.writeFlag(value);
        }

        public int getFlagCount() {
            return 1;
        }
    }
    
    class ByteTranscoder implements TypeTranscoder<Byte> {
        public Byte read(DataInputStream dis, Flags flags) throws IOException {
            return dis.readByte();
        }

        public void write(DataOutputStream dos, Byte value, Flags flags) throws IOException {
            dos.writeByte(value);
        }

        public int getFlagCount() {
            return 0;
        }
    }

    class ShortTranscoder implements TypeTranscoder<Short> {
        public Short read(DataInputStream dis, Flags flags) throws IOException {
            return dis.readShort();
        }

        public void write(DataOutputStream dos, Short value, Flags flags) throws IOException {
            dos.writeShort(value);
        }

        public int getFlagCount() {
            return 0;
        }
    }

    class CharacterTranscoder implements TypeTranscoder<Character> {
        public Character read(DataInputStream dis, Flags flags) throws IOException {
            return dis.readChar();
        }

        public void write(DataOutputStream dos, Character value, Flags flags) throws IOException {
            dos.writeChar(value);
        }

        public int getFlagCount() {
            return 0;
        }
    }

    class IntegerTranscoder implements TypeTranscoder<Integer> {
        public Integer read(DataInputStream dis, Flags flags) throws IOException {
            if (flags.readFlag()) {
                return Integer.valueOf(dis.readShort());
            } else {
                return dis.readInt();
            }
        }

        public void write(DataOutputStream dos, Integer value, Flags flags) throws IOException {
            short s = value.shortValue();
            if (s == value) {
                flags.writeFlag(true);
                dos.writeShort(s);
            } else {
                flags.writeFlag(false);
                dos.writeInt(value);
            }
        }

        public int getFlagCount() {
            return 1;
        }
    }

    class LongTranscoder implements TypeTranscoder<Long> {
        public Long read(DataInputStream dis, Flags flags) throws IOException {
            if (flags.readFlag()) {
                return Long.valueOf(dis.readShort());
            } else {
                return dis.readLong();
            }
        }

        public void write(DataOutputStream dos, Long value, Flags flags) throws IOException {
            short s = value.shortValue();
            if (s == value) {
                flags.writeFlag(true);
                dos.writeShort(s);
            } else {
                flags.writeFlag(false);
                dos.writeLong(value);
            }
        }

        public int getFlagCount() {
            return 1;
        }
    }

    class FloatTranscoder implements TypeTranscoder<Float> {
        public Float read(DataInputStream dis, Flags flags) throws IOException {
            if (flags.readFlag()) {
                return Float.valueOf(dis.readShort());
            } else {
                return dis.readFloat();
            }
        }

        public void write(DataOutputStream dos, Float value, Flags flags) throws IOException {
            short s = value.shortValue();
            if (s == value) {
                flags.writeFlag(true);
                dos.writeShort(s);
            } else {
                flags.writeFlag(false);
                dos.writeFloat(value);
            }
        }

        public int getFlagCount() {
            return 1;
        }
    }

    class DoubleTranscoder implements TypeTranscoder<Double> {
        public Double read(DataInputStream dis, Flags flags) throws IOException {
            if (flags.readFlag()) {
                return Double.valueOf(dis.readShort());
            } else {
                return dis.readDouble();
            }
        }

        public void write(DataOutputStream dos, Double value, Flags flags) throws IOException {
            short s = value.shortValue();
            if (s == value) {
                flags.writeFlag(true);
                dos.writeShort(s);
            } else {
                flags.writeFlag(false);
                dos.writeDouble(value);
            }
        }

        public int getFlagCount() {
            return 1;
        }
    }

    class StringTranscoder implements TypeTranscoder<String> {
        public String read(DataInputStream dis, Flags flags) throws IOException {
            int len = dis.readInt();
            byte[] data = new byte[len];
            dis.readFully(data);
            return new String(data, "utf-8");
        }

        public void write(DataOutputStream dos, String value, Flags flags) throws IOException {
            byte[] data = value.getBytes("utf-8");
            dos.writeInt(data.length);
            dos.write(data);
        }

        public int getFlagCount() {
            return 0;
        }
    }

    class ObjectArrayTranscoder implements TypeTranscoder<Object[]> {
        public Object[] read(DataInputStream dis, Flags flags) throws IOException {
            int len = dis.readInt();
            BitHeader bitHeader = new BitHeader(dis, len);
            Object[] objects = new Object[len];
            for (int x=0;x<len;x++) {
                if (bitHeader.readFlag()) {
                    try {
                        objects[x] = codec.decode(dis);
                    } catch (Exception e) {
                        throw new IOException(e);
                    }
                }
            }
            return objects;
        }

        public void write(DataOutputStream dos, Object[] value, Flags flags) throws IOException {
            dos.writeInt(value.length);
            BitHeader bitHeader = new BitHeader();
            for (Object o : value) {
                bitHeader.writeFlag(o != null);
            }
            dos.write(bitHeader.toByteArray());
            for (Object o : value) {
                if (o != null) {
                    try {
                        dos.write(codec.encode(o));
                    } catch (Exception e) {
                        throw new IOException(e);
                    }
                }
            }
        }

        public int getFlagCount() {
            return 0;
        }
    }

    class ListTranscoder implements TypeTranscoder<List>  {
        public List read(DataInputStream dis, Flags flags) throws IOException {
            return Arrays.asList(objectArrayTranscoder.read(dis, flags));
        }

        public void write(DataOutputStream dis, List value, Flags flags) throws IOException {
            objectArrayTranscoder.write(dis, value.toArray(), flags);
        }

        public int getFlagCount() {
            return 0;
        }
    }
}
