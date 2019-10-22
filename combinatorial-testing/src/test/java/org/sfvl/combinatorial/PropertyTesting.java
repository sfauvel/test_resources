package org.sfvl.combinatorial;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

/*
 * Example:
 *  
    public static class Data {

        Integer x;
        Integer y;
        Boolean isImmaterial;
        
        public void x(Integer x) {
            this.x = x;
        }
        
        public void y(Integer y) {
            this.y = y;
        }
        

        public void isImmaterial(Boolean isImmaterial) {
            this.isImmaterial = isImmaterial;
        }
    }
  
  
    @SuppressWarnings("unchecked")
    public Object[] parametersForShould_execute() {
        final List<Data> all = getAll(Data.class,
                mapValues(Data::isImmaterial, Arrays.asList(true, false)),
                mapValues(Data::x, Arrays.asList(0, 5, 99)),
                mapValues(Data::y, Arrays.asList(0, 5, 49))
                        
        all.removeIf(data -> data.x == data.y);

        return all.toArray();
    }
    @Test
    @Parameters
    public void should_execute(Data data) throws Exception {
        // Use data for the should_update_value_when_key_was_found.
    }
 */
public class PropertyTesting {
    private int nb = 0;

    static protected class MapSetterList<T> {

        private final Consumer<T> setter;
        private final List<T> values;

        public MapSetterList(Consumer<T> setter, List<T> values) {
            this.setter = setter;
            this.values = values;
        }
    }

    protected <T> PropertyTesting.MapSetterList mapValues(Consumer<T> setter, List<T> values) {
        return new MapSetterList(setter, values);
    }

    // Option 1
    protected void forEach(Runnable r, PropertyTesting.MapSetterList<Object>... mappers) {
        if (mappers.length == 0) {
            r.run();
        } else {
            for (Object value : mappers[0].values) {
                mappers[0].setter.accept(value);
                forEach(r, Arrays.copyOfRange(mappers, 1, mappers.length));
            }
        }
    }

    // Option 2
    protected void forEachValues(List<MapSetterList<Object>> mappers) {
        if (mappers.isEmpty()) {
            if (filter()) {
                nb++;
                //System.out.print(".");
                try {
                    test();
                } catch (Throwable e) {
                    showValues(this.getClass());
                    throw e;
                }
            }
        } else {
            PropertyTesting.MapSetterList<Object> mapper = mappers.get(0);
            for (Object value : mapper.values) {
                mapper.setter.accept(value);
                forEachValues(mappers.subList(1, mappers.size()));
            }
        }
    }


    protected void showValues() {
        showValues(this.getClass());
    }

    private void showValues(Class clazz) {
        if (clazz.equals(PropertyTesting.class) || clazz.equals(Object.class)) {
            return;
        }
        
        Stream.of(clazz.getDeclaredFields())
            .peek(f -> f.setAccessible(true))
            .map(f -> f.getName() + ": " + getFieldValue(f))
            .forEach(System.out::println);
        
        showValues(clazz.getSuperclass());
    }

    public String getFieldValue(Field f) {
        try {
            return toString().valueOf(f.get(this));
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
            return "ERROR GETTING VALUE";
        }
        
    }

    public void runTest() {
        nb = 0;
        forEachValues(values());
        System.out.println();
        System.out.println("Tests: " + nb);
    }

    public void runMultiTest() {
        nb = 0;
        List<List> values = multiValues();
        for (List value : values) {
            forEachValues(value);
        }
        System.out.println();
        System.out.println("Tests: " + nb);
    }

    public List<List> multiValues() {
        return Collections.emptyList();
    }

    public void test() {
    }

    public boolean filter() {
        return true;
    }

    public List values() {
        return Collections.emptyList();
    }

    public List<String> randomStrings(int nb, int size) {
        Random random = new Random();
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < nb; i++) {
            strings.add(randomString(size));
        }
        return strings;
    }

    public String randomString(int size) {
        Random random = new Random();
        char[] text = new char[size];
        for (int i = 0; i < size; i++) {
            text[i] = (char) (random.nextInt(26) + (random.nextBoolean() ? 'a' : 'A'));
        }
        return String.valueOf(text);

    }
}