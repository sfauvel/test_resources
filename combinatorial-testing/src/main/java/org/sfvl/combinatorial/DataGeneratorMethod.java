package org.sfvl.combinatorial;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Create a method that generate data.
 * @param <D>
 */
public class DataGeneratorMethod<D> {

    static protected class BiMapSetterList<O, T> {

        private final BiConsumer<O, T> setter;
        private final List<T> values;

        public BiMapSetterList(BiConsumer<O, T> setter, List<T> values) {
            this.setter = setter;
            this.values = values;
        }
    }

    protected <T> BiMapSetterList mapValues(BiConsumer<D, T> setter, List<T> values) {
        return new BiMapSetterList(setter, values);
    }


    public static <Obj, T> List<Obj> buildValues(Supplier<Obj> factory, List<BiMapSetterList<Obj, T>> mappers) {

        if (mappers.isEmpty()) {
            return Arrays.asList(factory.get());
        } else {
            BiMapSetterList<Obj, T> mapper = mappers.get(0);
            ArrayList<Obj> objects = new ArrayList<>();
            for (T value : mapper.values) {
                List<Obj> os = buildValues(factory, mappers.subList(1, mappers.size()));
                os.stream().forEach(o -> mapper.setter.accept(o, value));
                objects.addAll(os);
            }
            return objects;
        }
    }

}