package org.sfvl.combinatorial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * An object that can generate a combination of values.
 *
 * @param <D>
 */
public class DataGeneratorRecursif<D> {
    private final Supplier<D> factory;
    private List<SetterWithValues> setters = new ArrayList<>();

    static protected class SetterWithValues<O, T> {

        private final BiConsumer<O, T> setter;
        private final List<T> values;

        public SetterWithValues(BiConsumer<O, T> setter, List<T> values) {
            this.setter = setter;
            this.values = values;
        }

        public void accept(O o, Object value) {
            setter.accept(o, (T) value);
        }
    }

    public DataGeneratorRecursif(Supplier<D> factory) {
        this.factory = factory;
    }

    public <T> DataGeneratorRecursif<D> withValues(BiConsumer<D, T> setter, List<T> values) {
        setters.add(new SetterWithValues(setter, values));
        return this;
    }

    public List<D> build() {
        return buildRecursive(factory, setters);
    }

    /**
     * A recursive combination build.
     * It's not used but it could.
     */
    public List<D> buildRecursive(Supplier<D> factory, List<SetterWithValues> mappers) {

        if (mappers.isEmpty()) {
            return Arrays.asList(factory.get());
        } else {
            SetterWithValues<D, ?> mapper = mappers.get(0);
            return mapper.values.stream()
                    .flatMap(value -> buildRecursive(factory, mappers.subList(1, mappers.size()))
                            .stream().peek(o -> mapper.accept(o, value))
                    )
                    .collect(Collectors.toList());

        }
    }

}