package net.catchpole.B9.codec.transcoder;

import net.catchpole.B9.codec.one.Types;
import net.catchpole.B9.codec.stream.BitInputStream;
import net.catchpole.B9.codec.stream.BitOutputStream;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class BeanTranscoder implements TypeTranscoder<Object> {
    private final Map<String,Object> defaults = new HashMap<String, Object>();

    private final BaseTypeTranscoder baseTypeTranscoder;
    private final Types beanTypes;

    public BeanTranscoder(BaseTypeTranscoder baseTypeTranscoder, Types beanTypes) {
        this.baseTypeTranscoder = baseTypeTranscoder;
        this.beanTypes = beanTypes;

        this.defaults.put(Boolean.class.getName(), false);
        this.defaults.put(Boolean.TYPE.getName(), false);
        this.defaults.put(Byte.class.getName(), (byte)0);
        this.defaults.put(Byte.TYPE.getName(), (byte)0);
        this.defaults.put(Short.class.getName(), (short)0);
        this.defaults.put(Short.TYPE.getName(), (short)0);
        this.defaults.put(Character.class.getName(), (char)0);
        this.defaults.put(Character.TYPE.getName(), (char)0);
        this.defaults.put(Integer.class.getName(), 0);
        this.defaults.put(Integer.TYPE.getName(), 0);
        this.defaults.put(Long.class.getName(), 0l);
        this.defaults.put(Long.TYPE.getName(), 0l);
        this.defaults.put(Float.class.getName(), 0.0f);
        this.defaults.put(Float.TYPE.getName(), 0.0f);
        this.defaults.put(Double.class.getName(), 0.0d);
        this.defaults.put(Double.TYPE.getName(), 0.0d);
    }

    @Override
    public Object read(BitInputStream in) throws IOException {
        try {
            char id = (char)in.read(8);
            Class clazz = beanTypes.getType(id);
            if (clazz == null) {
                throw new IllegalArgumentException("No type found for id " + id);
            }

            Object object = construct(clazz);
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
                Object value = field.get(object);
                field.setAccessible(true);

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
                params[x] = defaults.get(paramClass.getName());
            }
        }
        return constructor.newInstance(params);
    }
}
