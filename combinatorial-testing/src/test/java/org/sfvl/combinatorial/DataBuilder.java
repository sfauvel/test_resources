package org.sfvl.combinatorial;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
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
public class DataBuilder<D> {
    private int nb = 0;

    static protected class MapSetterList<T> {

        private final Consumer<T> setter;
        private final List<T> values;

        public MapSetterList(Consumer<T> setter, List<T> values) {
            this.setter = setter;
            this.values = values;
        }
    }

    protected <T> DataBuilder.MapSetterList mapValues(Consumer<T> setter, List<T> values) {
        return new MapSetterList(setter, values);
    }

    public List<D> buildValues() {
        return buildValues(values());
    }

    protected List<D> buildValues(List<MapSetterList<Object>> mappers) {
        if (mappers.isEmpty()) {
            return Arrays.asList(build());
        } else {
            DataBuilder.MapSetterList<Object> mapper = mappers.get(0);
            ArrayList<D> objects = new ArrayList<>();
            for (Object value : mapper.values) {
                mapper.setter.accept(value);
                objects.addAll(buildValues(mappers.subList(1, mappers.size())));
            }
            return objects;
        }
    }

    protected D build() {
        try {
            D newInstance = newInstance();
            cloneValues(newInstance);
            return newInstance;
        } catch (InstantiationException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Create an instance of the object.
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    protected D newInstance() throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Class<?> clazz = this.getClass();

        // When there is no constructor (anonymous class) we could not create an instance.
        while (clazz.getConstructors().length == 0
                && clazz != DataBuilder.class) {
            clazz = clazz.getSuperclass();
        }

        return (D) clazz.getConstructor().newInstance();
    }

    private void cloneValues(Class<?> clazz, DataBuilder dataBuilder) {
        if (clazz.getSuperclass() != DataBuilder.class) {
            cloneValues((Class<DataBuilder>) clazz.getSuperclass(), dataBuilder);
        }

        Stream.of(clazz.getDeclaredFields())
                .peek(f -> f.setAccessible(true))
                .forEach(f -> {
                    cloneField(dataBuilder, f);
                });

    }

    private void cloneValues(Object dataBuilder) {
        for (Class clazz = dataBuilder.getClass();
             clazz != DataBuilder.class;
             clazz = clazz.getSuperclass()) {

            Stream.of(clazz.getDeclaredFields())
                    .peek(f -> f.setAccessible(true))
                    .forEach(f -> cloneField(dataBuilder, f)
                    );
        }

    }

    private void cloneField(Object dataBuilder, Field f) {
        try {
            f.set(dataBuilder, f.get(this));
        } catch (IllegalAccessException e) {
            throw new RuntimeException();
        }
    }

    //
    protected void showValues() {
        showValues(this.getClass());
    }

    private void showValues(Class clazz) {
        if (clazz.equals(DataBuilder.class) || clazz.equals(Object.class)) {
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

    public List values() {
        return Collections.emptyList();
    }
}