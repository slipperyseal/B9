package net.catchpole.B9.codec.transcoder;

import net.catchpole.B9.codec.stream.BitInputStream;
import net.catchpole.B9.codec.stream.BitMasks;
import net.catchpole.B9.codec.stream.BitOutputStream;

import java.io.IOException;
import java.util.*;

public class BaseTypeTranscoder {
    private final BitMasks bitMasks = new BitMasks();
    private final Map<String,TypeTranscoder> transcoderMap = new HashMap<String, TypeTranscoder>();

    private final BooleanTranscoder booleanTranscoder = new BooleanTranscoder();
    private final ByteTranscoder byteTranscoder = new ByteTranscoder();
    private final ShortTranscoder shortTranscoder = new ShortTranscoder();
    private final CharacterTranscoder characterTranscoder = new CharacterTranscoder();
    private final IntegerTranscoder integerTranscoder = new IntegerTranscoder();
    private final LongTranscoder longTranscoder = new LongTranscoder();
    private final FloatTranscoder floatTranscoder = new FloatTranscoder();
    private final DoubleTranscoder doubleTranscoder = new DoubleTranscoder();
    private final StringTranscoder stringTranscoder = new StringTranscoder();
    private final TypeTranscoder[] baseTranscoders = new TypeTranscoder[] {
            booleanTranscoder, byteTranscoder, shortTranscoder,
            characterTranscoder, integerTranscoder, longTranscoder,
            floatTranscoder, doubleTranscoder, stringTranscoder
    };

    private final ListTranscoder listTranscoder = new ListTranscoder();
    private final SetTranscoder setTranscoder = new SetTranscoder();
    private final MapTranscoder mapTranscoder = new MapTranscoder();
    private final ByteArrayTranscoder byteArrayTranscoder = new ByteArrayTranscoder();
    private final ObjectArrayTranscoder objectArrayTranscoder = new ObjectArrayTranscoder();

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

    private int getBaseTranscoderIndex(TypeTranscoder typeTranscoder) {
        int x=0;
        for (TypeTranscoder t : baseTranscoders) {
            if (t == typeTranscoder) {
                return x;
            }
            x++;
        }
        throw new IllegalArgumentException(typeTranscoder.getClass().getName());
    }

    public TypeTranscoder getTranscoder(Class clazz) {
        if (clazz.isAssignableFrom(List.class)) {
            return listTranscoder;
        }
        if (clazz.isAssignableFrom(Set.class)) {
            return setTranscoder;
        }
        if (clazz.isAssignableFrom(Map.class)) {
            return mapTranscoder;
        }
        if (clazz.isArray()) {
            return objectArrayTranscoder;
        }
        return transcoderMap.get(clazz.getName());
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
            if (!in.readBoolean()) {
                return 0;
            }
            return (byte)in.read(8);
        }

        public void write(BitOutputStream out, Byte value) throws IOException {
            out.writeBoolean(value != 0);
            if (value != 0) {
                out.write(value & 0xff, 8);
            }
        }
    }

    class ShortTranscoder implements TypeTranscoder<Short> {
        public Short read(BitInputStream in) throws IOException {
            if (!in.readBoolean()) {
                return 0;
            }
            return (short)in.read(16);
        }

        public void write(BitOutputStream out, Short value) throws IOException {
            out.writeBoolean(value != 0);
            if (value != 0) {
                out.write(value & 0xffff, 16);
            }
        }
    }

    class CharacterTranscoder implements TypeTranscoder<Character> {
        public Character read(BitInputStream in) throws IOException {
            if (!in.readBoolean()) {
                return 0;
            }
            return (char)in.read(16);
        }

        public void write(BitOutputStream out, Character value) throws IOException {
            out.writeBoolean(value != 0);
            if (value != 0) {
                out.write(value & 0xffff, 16);
            }
        }
    }

    class IntegerTranscoder implements TypeTranscoder<Integer> {
        public Integer read(BitInputStream in) throws IOException {
            if (!in.readBoolean()) {
                return 0;
            }
            return in.readBoolean() ? in.readSigned(16) : in.readSigned(32);
        }

        public void write(BitOutputStream out, Integer value) throws IOException {
            out.writeBoolean(value != 0);
            if (value != 0) {
                boolean fitsShort = value >= Short.MIN_VALUE && value <= Short.MAX_VALUE;
                out.writeBoolean(fitsShort);
                out.write(value, fitsShort ? 16 : 32);
            }
        }
    }

    class LongTranscoder implements TypeTranscoder<Long> {
        public Long read(BitInputStream in) throws IOException {
            return integerTranscoder.read(in) | (((long)integerTranscoder.read(in)) << 32);
        }

        public void write(BitOutputStream out, Long value) throws IOException {
            integerTranscoder.write(out, value.intValue());
            integerTranscoder.write(out, (int) (value >> 32));
        }
    }

    class FloatTranscoder implements TypeTranscoder<Float> {
        public Float read(BitInputStream in) throws IOException {
            if (!in.readBoolean()) {
                return 0.0f;
            }
            return Float.intBitsToFloat(in.read(32));
        }

        public void write(BitOutputStream out, Float value) throws IOException {
            boolean zero = "0.0".equals(value.toString());
            out.writeBoolean(!zero);
            if (!zero) {
                out.write(Float.floatToIntBits(value), 32);
            }
        }
    }

    class DoubleTranscoder implements TypeTranscoder<Double> {
        public Double read(BitInputStream in) throws IOException {
            if (!in.readBoolean()) {
                return 0.0d;
            }
            if (in.readBoolean()) {
                return Double.parseDouble(new Float(Float.intBitsToFloat(in.read(32))).toString());
            } else {
                return Double.longBitsToDouble(in.readLong(64));
            }
        }

        public void write(BitOutputStream out, Double value) throws IOException {
            boolean zero = "0.0".equals(value.toString());
            out.writeBoolean(!zero);
            if (!zero) {
                Float floatValue = value.floatValue();
                boolean equalsFloat = value.toString().equals(floatValue.toString());
                out.writeBoolean(equalsFloat);
                if (equalsFloat) {
                    out.write(Float.floatToIntBits(floatValue), 32);
                } else {
                    out.writeLong(Double.doubleToLongBits(value), 64);
                }
            }
        }
    }

    class StringTranscoder implements TypeTranscoder<String> {
        public String read(BitInputStream in) throws IOException {
            return new String(byteArrayTranscoder.read(in), "utf-8");
        }

        public void write(BitOutputStream out, String value) throws IOException {
            byteArrayTranscoder.write(out, value.getBytes("utf-8"));
        }
    }

    class SimpleByteArrayTranscoder implements TypeTranscoder<byte[]> {
        @Override
        public byte[] read(BitInputStream in) throws IOException {
            if (!in.readBoolean()) {
                return new byte[0];
            }
            int len = integerTranscoder.read(in);
            byte[] value = new byte[len];
            for (int x = 0; x < len; x++) {
                value[x] = (byte)in.read(8);
            }
            return value;
        }

        @Override
        public void write(BitOutputStream out, byte[] value) throws IOException {
            out.writeBoolean(value.length != 0);
            if (value.length != 0) {
                integerTranscoder.write(out, value.length);
                for (int b : value) {
                    out.write((b & 0xff), 8);
                }
            }
        }

    }

    class ByteArrayTranscoder implements TypeTranscoder<byte[]> {
        @Override
        public byte[] read(BitInputStream in) throws IOException {
            if (!in.readBoolean()) {
                return new byte[0];
            }
            int len = integerTranscoder.read(in);
            byte[] value = new byte[len];

            int bits = in.read(3);
            int base = in.read(8);
            if (bits == 0) {
                for (int x = 0; x < len; x++) {
                    value[x] = (byte)base;
                }
            } else {
                for (int x = 0; x < len; x++) {
                    value[x] = (byte) (in.read(bits) + base);
                }
            }
            return value;
        }

        @Override
        public void write(BitOutputStream out, byte[] value) throws IOException {
            out.writeBoolean(value.length != 0);
            if (value.length != 0) {
                integerTranscoder.write(out, value.length);

                int base = min(value);
                int bits = bitMasks.bitsRequired(max(value) - base);
                out.write(bits, 3);
                out.write(base, 8);
                if (bits != 0) {
                    for (int b : value) {
                        out.write((b & 0xff) - base, bits);
                    }
                }
            }
        }

        private int min(byte[] data) {
            int value = 0xff;
            for (int b : data) {
                b &= 0xff;
                if (b <= value) {
                    value = b;
                }
            }
            return value;
        }

        private int max(byte[] data) {
            int value = 0;
            for (int b : data) {
                b &= 0xff;
                if (b >= value) {
                    value = b;
                }
            }
            return value;
        }
    }

    class ObjectArrayTranscoder implements TypeTranscoder<Object[]> {
        public Object[] read(BitInputStream in) throws IOException {
            int len = integerTranscoder.read(in);
            Object[] objects = new Object[len];
            for (int x=0;x<len;x++) {
                // has value
                if (in.readBoolean()) {
                    // base type
                    if (in.readBoolean()) {
                        TypeTranscoder baseTypeTranscoder = baseTranscoders[in.read(4)];
                        objects[x] = baseTypeTranscoder.read(in);
                    } else {
                        objects[x] = beanTranscoder.read(in);
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
                    TypeTranscoder baseTypeTranscoder = getTranscoder(element.getClass());
                    out.writeBoolean(baseTypeTranscoder != null);
                    // base type
                    if (baseTypeTranscoder != null) {
                        out.write(getBaseTranscoderIndex(baseTypeTranscoder), 4);
                        baseTypeTranscoder.write(out, element);
                    } else {
                        beanTranscoder.write(out, element);
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

    class SetTranscoder implements TypeTranscoder<Set>  {
        public Set read(BitInputStream in) throws IOException {
            return new HashSet(Arrays.asList(objectArrayTranscoder.read(in)));
        }

        public void write(BitOutputStream out, Set value) throws IOException {
            objectArrayTranscoder.write(out, value.toArray());
        }
    }

    class MapTranscoder implements TypeTranscoder<Map>  {
        public Map read(BitInputStream in) throws IOException {
            Object[] all = objectArrayTranscoder.read(in);
            Map value = new HashMap();
            for (int x=0;x<all.length;x+=2) {
                value.put(all[x],all[x+1]);
            }
            return value;
        }

        public void write(BitOutputStream out, Map value) throws IOException {
            Object[] values = new Object[value.size()*2];
            int x=0;
            Set<Map.Entry> entrySet = value.entrySet();
            for (Map.Entry entry : entrySet) {
                values[x++] = entry.getKey();
                values[x++] = entry.getValue();
            }
            objectArrayTranscoder.write(out, values);
        }
    }
}