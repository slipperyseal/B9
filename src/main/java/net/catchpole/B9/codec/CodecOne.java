package net.catchpole.B9.codec;

import net.catchpole.B9.codec.one.BaseTypeTranscoder;
import net.catchpole.B9.codec.one.BitHeader;
import net.catchpole.B9.codec.one.TypeTranscoder;
import net.catchpole.B9.codec.one.Types;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

// CodecOne and the 'one' package is a legacy codec. It's not as cool as B9Codec
// It's currently in the code base as we still have some implementations using it.
// Once it is removed from those implementations it will be removed.
public class CodecOne implements Codec {
    private final BaseTypeTranscoder baseTypeTranscoder = new BaseTypeTranscoder(this);
    private final Types types = new Types();
    private final Map<Character, Integer> headerSizes = new HashMap<Character, Integer>();

    public void addType(Character character, Class clazz) {
        try {
            this.types.addType(character, clazz);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public byte[] encode(Object object) throws IOException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(128);
            DataOutputStream dataOutputStream = new DataOutputStream(baos);
            BitHeader bitHeader = new BitHeader();

            Character id = types.getId(object.getClass());
            if (id == null) {
                throw new IllegalArgumentException("No type encoding defined for " + object.getClass().getName());
            }
            for (Field field : types.getFields(object.getClass())) {
                Class type = field.getType();
                field.setAccessible(true);
                Object value = field.get(object);
                if (types.getId(type) != null) {
                    bitHeader.writeFlag(value != null);
                    if (value != null) {
                        baos.write(encode(value));
                    }
                } else {
                    TypeTranscoder typeTranscoder = baseTypeTranscoder.getTranscoder(field.getType());
                    if (typeTranscoder == null) {
                        throw new IllegalArgumentException("No transcoder for " + field.getType());
                    }

                    if (!type.isPrimitive()) {
                        bitHeader.writeFlag(value != null);
                    }
                    if (value != null) {
                        typeTranscoder.write(dataOutputStream, value, bitHeader);
                    } else {
                        // value is null, write not called - but we must push flag count
                        int flagCount = typeTranscoder.getFlagCount();
                        for (int x = 0; x < flagCount; x++) {
                            bitHeader.writeFlag(false);
                        }
                    }
                }
            }
            dataOutputStream.flush();

            // store size of bitheader which is required by the decoder
            byte[] header = bitHeader.toByteArray();
            if (!headerSizes.containsKey(id)) {
                headerSizes.put(id, header.length);
            }

            ByteArrayOutputStream result = new ByteArrayOutputStream(1 + header.length + baos.size());
            result.write((byte) id.charValue());
            result.write(header);
            baos.writeTo(result);
            return result.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Object decode(byte[] bytes) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        return decode(new DataInputStream(byteArrayInputStream));
    }

    public void encode(Object object, OutputStream outputStream) throws IOException {
        outputStream.write(encode(object));
    }

    public Object decode(InputStream inputStream) throws IOException {
        return decode(new DataInputStream(inputStream));
    }

    public Object decode(DataInputStream dataInputStream) throws IOException {
        try {
            char id = (char) dataInputStream.read();
            Class clazz = types.getType(id);
            if (clazz == null) {
                throw new IllegalArgumentException("No type found for id " + id);
            }
            if (headerSizes.get(id) == null) {
                // get header size by encoding an empty object. this also is a good test that type encoding is going to work
                encode(construct(clazz));
            }

            byte[] header = new byte[headerSizes.get(id)];
            dataInputStream.readFully(header);
            BitHeader bitHeader = new BitHeader(header);

            Object object = construct(clazz);
            for (Field field : types.getFields(clazz)) {
                Class type = field.getType();

                if (types.getId(type) != null) {
                    if (bitHeader.readFlag()) {
                        field.setAccessible(true);
                        field.set(object, decode(dataInputStream));
                    }
                } else {
                    TypeTranscoder typeTranscoder = baseTypeTranscoder.getTranscoder(field.getType());
                    if (typeTranscoder == null) {
                        throw new IllegalArgumentException("No transcoder for " + field.getType());
                    }

                    boolean hasValue = type.isPrimitive() || bitHeader.readFlag();
                    Object value = hasValue ? typeTranscoder.read(dataInputStream, bitHeader) : null;
                    if (!hasValue) {
                        // value is null, read not called - but we must push flag count
                        int flagCount = typeTranscoder.getFlagCount();
                        for (int x = 0; x < flagCount; x++) {
                            bitHeader.readFlag();
                        }
                    }
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
