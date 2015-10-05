package net.catchpole.B9.codec;

import net.catchpole.B9.codec.field.BaseTypeTranscoder;
import net.catchpole.B9.codec.field.BitHeader;
import net.catchpole.B9.codec.field.TypeTranscoder;
import net.catchpole.B9.codec.field.Types;

import java.io.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Codec {
    private final BaseTypeTranscoder baseTypeTranscoder = new BaseTypeTranscoder();
    private final Types types = new Types();
    private final Map<Character, Integer> headerSizes = new HashMap<Character, Integer>();

    public void addType(Character character, Class clazz) throws IOException, InstantiationException, IllegalAccessException {
        this.types.addType(character, clazz);
    }

    public byte[] encode(Object object) throws IOException, IllegalAccessException {
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
                    int flagCount = typeTranscoder.getFlagCount();
                    for (int x = 0; x < flagCount; x++) {
                        bitHeader.writeFlag(false);
                    }
                }
            }
        }
        dataOutputStream.flush();

        byte[] header = bitHeader.toByteArray();
        if (!headerSizes.containsKey(id)) {
            headerSizes.put(id, header.length);
        }

        ByteArrayOutputStream result = new ByteArrayOutputStream(1 + header.length + baos.size());
        result.write((byte)id.charValue());
        result.write(header);
        baos.writeTo(result);
        return result.toByteArray();
    }

    public Object decode(byte[] bytes) throws IOException, IllegalAccessException, InstantiationException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        return decode(new DataInputStream(byteArrayInputStream));
    }

    public Object decode(DataInputStream dataInputStream) throws IOException, IllegalAccessException, InstantiationException {
        if (headerSizes.size() == 0) {
            profile();
        }

        char id = (char)dataInputStream.read();
        Class clazz = types.getType(id);
        if (clazz == null) {
            throw new IllegalArgumentException("No type found for id " + id);
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
    }

    private Object construct(Class clazz) throws InstantiationException, IllegalAccessException {
        return clazz.newInstance();
    }

    private void profile() throws IOException, InstantiationException, IllegalAccessException {
        // get header size by encoding an empty object. this also is a good test that type encoding is going to work
        for (Class type : types.getClasses()) {
            encode(construct(type));
        }
    }
}
