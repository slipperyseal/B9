package net.catchpole.B9.codec.transcoder;

import net.catchpole.B9.codec.type.Types;
import net.catchpole.B9.codec.stream.BitInputStream;
import net.catchpole.B9.codec.stream.BitOutputStream;
import net.catchpole.B9.lang.Throw;

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
            boolean fitsSeven = in.readBoolean();
            char id = (char)in.read(fitsSeven ? 7 : 16);
            if (fitsSeven) {
                id &= 0x7f;
            }
            Class type = beanTypes.getType(id);
            if (type == null) {
                throw new IllegalArgumentException("No type found for id " + id);
            }

            Object object = magicConstructor.construct(type);
            for (Field field : beanTypes.getFields(type)) {
                Class fieldType = field.getType();

                if (beanTypes.getId(fieldType) != null) {
                    if (in.readBoolean()) {
                        field.setAccessible(true);
                        field.set(object, read(in));
                    }
                } else {
                    TypeTranscoder typeTranscoder = baseTypeTranscoder.getTranscoder(fieldType);
                    if (typeTranscoder == null) {
                        throw new IllegalArgumentException("No transcoder for " + fieldType);
                    }
                    if (fieldType.isPrimitive() || in.readBoolean()) {
                        Object value = typeTranscoder.read(in);
                        field.setAccessible(true);
                        if (typeTranscoder instanceof FieldInterceptor) {
                            value = ((FieldInterceptor)typeTranscoder).intercept(field.get(object), value);
                        }
                        field.set(object, value);
                    }
                }
            }
            return object;
        } catch (Exception e) {
            throw Throw.unchecked(e);
        }
    }

    @Override
    public void write(BitOutputStream out, Object object) throws IOException {
        try {
            Character id = beanTypes.getId(object.getClass());
            if (id == null) {
                throw new IllegalArgumentException("No type encoding defined for " + object.getClass().getName());
            }
            boolean fitsSeven = id <= 127;
            out.writeBoolean(fitsSeven);
            out.write(id & 0xffff, fitsSeven ? 7 : 16);

            for (Field field : beanTypes.getFields(object.getClass())) {
                Class fieldType = field.getType();
                field.setAccessible(true);
                Object value = field.get(object);

                if (beanTypes.getId(fieldType) != null) {
                    out.writeBoolean(value != null);
                    if (value != null) {
                        write(out, value);
                    }
                } else {
                    TypeTranscoder typeTranscoder = baseTypeTranscoder.getTranscoder(fieldType);
                    if (typeTranscoder == null) {
                        throw new IllegalArgumentException("No transcoder for " + fieldType.getName());
                    }
                    if (!fieldType.isPrimitive()) {
                        out.writeBoolean(value != null);
                    }
                    if (value != null) {
                        typeTranscoder.write(out, value);
                    }
                }
            }
        } catch (Exception e) {
            throw Throw.unchecked(e);
        }
    }
}
