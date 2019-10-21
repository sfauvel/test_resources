package org.sfvl.combinatorial;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * This class give some tools to create a list of instance with just one value different from the other ones.
 *
 * The purpose of this tool is to verify that a test fail when only one value is wrong.
 */
public class CombinatorialTool {
    /**
     * Create a list of instance with only one wrong value set.
     * @param clazz
     * @param values
     * @param <O>
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    static <O> List<O> buildInstanceWithOneWrongValue(Class<O> clazz, List<Value<O, ?>> values) throws IllegalAccessException, InstantiationException {

        List<O> result = new ArrayList<>();

        for (Value<O, ?> value : values) {
            O obj = buildInstance(clazz, values);
            value.setBadValue(obj);
            result.add(obj);
        }
        return result;
    }

    /**
     * Create an instance and valuate it with all values.
     * @param clazz
     * @param values
     * @param <O>
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    static <O> O buildInstance(Class<O> clazz, List<Value<O, ?>> values) throws InstantiationException, IllegalAccessException {
        O p = clazz.newInstance();
        values.stream().forEach(v -> v.setRightValue(p));
        return p;
    }

    /**
     * Build a value.
     * @param setter
     * @param value
     * @param wrongValue
     * @param <O>
     * @param <T>
     * @return
     */
    public static <O, T> Value<O, T> $(BiConsumer<O, T> setter, T value, T wrongValue) {
        return createValue(setter, value, wrongValue);
    }

    /**
     * Build a value.
     * @param setter
     * @param value
     * @param wrongValue
     * @param <O>
     * @param <T>
     * @return
     */
    public static <O, T> Value<O, T> createValue(BiConsumer<O, T> setter, T value, T wrongValue) {
        return new Value(setter, value, wrongValue);
    }

    /**
     * Object containing good value, wrong value and setter to set it.
     * @param <O> Object on which wa hace to set value.
     * @param <T> Type of value.
     */
    public static class Value<O, T> {

        private final T goodValue;
        private final T badValue;
        private final BiConsumer<O, T> setter;

        public Value(BiConsumer<O, T> setter, T value, T wrongValue) {
            this.goodValue = value;
            this.badValue = wrongValue;
            this.setter = setter;
        }

        public T getGoodValue() {
            return goodValue;
        }

        public T getBadValue() {
            return badValue;
        }

        public BiConsumer<O, T> getSetter() {
            return setter;
        }

        public void setRightValue(O p) {
            setter.accept(p, getGoodValue());
        }

        public void setBadValue(O p) {
            setter.accept(p, getBadValue());
        }
    }
}
