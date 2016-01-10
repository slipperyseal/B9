package net.catchpole.B9.codec;

import net.catchpole.B9.codec.one.Types;
import net.catchpole.B9.codec.stream.BitInputStream;
import net.catchpole.B9.codec.stream.BitOutputStream;
import net.catchpole.B9.codec.two.BaseTypeTranscoder;
import net.catchpole.B9.codec.two.CodecStream;
import net.catchpole.B9.codec.two.TypeTranscoder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

// codec two uses bit streams and will replace codec one. but it's not finished
public class CodecTwo implements CodecStream {
    private final BaseTypeTranscoder baseTypeTranscoder = new BaseTypeTranscoder(this);
    private final Types types = new Types();

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
        encode(object, bitOutputStream);
        bitOutputStream.flush();
        return byteArrayOutputStream.toByteArray();
    }

    public void encode(Object object, BitOutputStream bitOutputStream) throws IOException {
        try {
            Character id = types.getId(object.getClass());
            if (id == null) {
                throw new IllegalArgumentException("No type encoding defined for " + object.getClass().getName());
            }
            bitOutputStream.write(id, 8);
            for (Field field : types.getFields(object.getClass())) {
                Class type = field.getType();
                field.setAccessible(true);
                Object value = field.get(object);

                if (types.getId(type) != null) {
                    bitOutputStream.writeBoolean(value != null);
                    if (value != null) {
                        encode(value, bitOutputStream);
                    }
                } else {
                    TypeTranscoder typeTranscoder = baseTypeTranscoder.getTranscoder(field.getType());
                    if (typeTranscoder == null) {
                        throw new IllegalArgumentException("No transcoder for " + field.getType());
                    }

                    if (!type.isPrimitive()) {
                        bitOutputStream.writeBoolean(value != null);
                    }
                    if (value != null) {
                        typeTranscoder.write(bitOutputStream, value);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Object decode(byte[] bytes) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        return decode(new BitInputStream(byteArrayInputStream));
    }

    public Object decode(BitInputStream bitInputStream) throws IOException {
        try {
            char id = (char)bitInputStream.read(8);
            Class clazz = types.getType(id);
            if (clazz == null) {
                throw new IllegalArgumentException("No type found for id " + id);
            }

            Object object = construct(clazz);
            for (Field field : types.getFields(clazz)) {
                Class type = field.getType();

                if (types.getId(type) != null) {
                    if (bitInputStream.readBoolean()) {
                        field.setAccessible(true);
                        Object value = decode(bitInputStream);
                        field.set(object, value);
                    }
                } else {
                    TypeTranscoder typeTranscoder = baseTypeTranscoder.getTranscoder(field.getType());
                    if (typeTranscoder == null) {
                        throw new IllegalArgumentException("No transcoder for " + field.getType());
                    }

                    boolean hasValue = type.isPrimitive() || bitInputStream.readBoolean();
                    Object value = hasValue ? typeTranscoder.read(bitInputStream) : null;
                    field.setAccessible(true);
                    field.set(object, value);
                }
            }
            return object;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object construct(Class clazz) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        Constructor[] constructors = clazz.getDeclaredConstructors();
        for (Constructor constructor : constructors) {
            if (constructor.getParameterTypes().length == 0) {
                // use default constructor if available
                return constructor.newInstance();
            }
        }
        // try a constructor and use "default" values which we will write over later
        Constructor constructor = constructors[0];
        Class[] paramClasses = constructor.getParameterTypes();
        Object[] params = new Object[paramClasses.length];
        for (int x=0;x<paramClasses.length;x++) {
            Class paramClass = paramClasses[x];
            if (paramClass.isPrimitive()) {
                params[x] = baseTypeTranscoder.getDefault(paramClass);
            }
        }
        return constructor.newInstance(params);
    }
}
