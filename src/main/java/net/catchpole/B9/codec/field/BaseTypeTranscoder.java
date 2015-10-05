package net.catchpole.B9.codec.field;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BaseTypeTranscoder {
    private Map<String,TypeTranscoder> transcoderMap = new HashMap<String, TypeTranscoder>();
    private Map<String,Object> defaults = new HashMap<String, Object>();

    public BaseTypeTranscoder() {
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
            return 0;
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
}
