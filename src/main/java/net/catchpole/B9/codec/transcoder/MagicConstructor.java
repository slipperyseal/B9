package net.catchpole.B9.codec.transcoder;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class MagicConstructor {
    private final Map<String,Object> defaults = new HashMap<String, Object>();

    public MagicConstructor() {
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

    public Object construct(Class clazz) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        Constructor[] constructors = clazz.getDeclaredConstructors();
        for (Constructor constructor : constructors) {
            constructor.setAccessible(true);
            if (constructor.getParameterTypes().length == 0) {
                // use default constructor if available
                return constructor.newInstance();
            }
        }
        // try a constructor and use "default" values
        for (Constructor constructor : constructors) {
            constructor.setAccessible(true);
            try {
                Class[] paramClasses = constructor.getParameterTypes();
                Object[] params = new Object[paramClasses.length];
                for (int x = 0; x < paramClasses.length; x++) {
                    Class paramClass = paramClasses[x];
                    if (paramClass.isPrimitive()) {
                        params[x] = defaults.get(paramClass.getName());
                    }
                }
                return constructor.newInstance(params);
            } catch (Exception e) {
            }
        }
        throw new InstantiationError(clazz.getName());
    }
}
