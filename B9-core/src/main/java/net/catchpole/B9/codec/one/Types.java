package net.catchpole.B9.codec.one;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class Types {
    private final Map<Character,Class> typesById = new HashMap<Character, Class>();
    private final Map<String,Character> typesByName = new HashMap<String,Character>();
    private final Map<String,List<Field>> fieldsByName = new HashMap<String, List<Field>>();

    public void addType(Character id, Class clazz) {
        if (typesById.get(id) != null) {
            throw new IllegalArgumentException("id " + id + " for " + clazz + " already mapped to " + typesById.get(id));
        }
        typesById.put(id, clazz);
        typesByName.put(clazz.getName(), id);

        Field[] fields = clazz.getDeclaredFields();
        Arrays.sort(fields, new Comparator<Field>() {
            public int compare(Field o1, Field o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        List<Field> fieldList = new ArrayList<Field>();
        for (Field field : fields) {
            if (!Modifier.isStatic(field.getModifiers())) {
                fieldList.add(field);
            }
        }
        fieldsByName.put(clazz.getName(), fieldList);
    }

    public Class getType(Character id) {
        return typesById.get(id);
    }

    public Character getId(Class clazz) {
        return typesByName.get(clazz.getName());
    }

    public List<Field> getFields(Class clazz) {
        return fieldsByName.get(clazz.getName());
    }
}