package net.catchpole.B9.codec.transcoder;

import net.catchpole.B9.codec.one.Types;
import net.catchpole.B9.codec.stream.BitInputStream;
import net.catchpole.B9.codec.stream.BitOutputStream;

import java.io.IOException;
import java.lang.reflect.Field;

public class BeanTranscoder implements TypeTranscoder<Object> {
    private final MagicConstructor magicConstructor = new MagicConstructor();
    private final BaseTypeTranscoder baseTypeTranscoder;
    private final Types beanTypes;

    public BeanTranscoder(BaseTypeTranscoder baseTypeTranscoder, Types beanTypes) {
        this.baseTypeTranscoder = baseTypeTranscoder;
        this.beanTypes = beanTypes;
    }

    @Override
    public Object read(BitInputStream in) throws IOException {
        try {
            char id = (char)in.read(8);
            Class clazz = beanTypes.getType(id);
            if (clazz == null) {
                throw new IllegalArgumentException("No type found for id " + id);
            }

            Object object = magicConstructor.construct(clazz);
            for (Field field : beanTypes.getFields(clazz)) {
                Class type = field.getType();

                if (beanTypes.getId(type) != null) {
                    if (in.readBoolean()) {
                        field.setAccessible(true);
                        field.set(object, read(in));
                    }
                } else {
                    TypeTranscoder typeTranscoder = baseTypeTranscoder.getTranscoder(field.getType());
                    if (typeTranscoder == null) {
                        throw new IllegalArgumentException("No transcoder for " + field.getType());
                    }
                    field.setAccessible(true);
                    field.set(object, type.isPrimitive() || in.readBoolean() ? typeTranscoder.read(in) : null);
                }
            }
            return object;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(BitOutputStream out, Object object) throws IOException {
        try {
            Character id = beanTypes.getId(object.getClass());
            if (id == null) {
                throw new IllegalArgumentException("No type encoding defined for " + object.getClass().getName());
            }
            out.write(id, 8);
            for (Field field : beanTypes.getFields(object.getClass())) {
                Class type = field.getType();
                field.setAccessible(true);
                Object value = field.get(object);

                if (beanTypes.getId(type) != null) {
                    out.writeBoolean(value != null);
                    if (value != null) {
                        write(out, value);
                    }
                } else {
                    TypeTranscoder typeTranscoder = baseTypeTranscoder.getTranscoder(field.getType());
                    if (typeTranscoder == null) {
                        throw new IllegalArgumentException("No transcoder for " + type.getName());
                    }

                    if (!type.isPrimitive()) {
                        out.writeBoolean(value != null);
                    }
                    if (value != null) {
                        typeTranscoder.write(out, value);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
