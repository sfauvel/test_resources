package org.sfvl.combinatorial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * An object that can generate a combination of values.
 *
 * @param <D>
 */
public class DataGeneratorObject<D> {
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

    public DataGeneratorObject(Supplier<D> factory) {
        this.factory = factory;
    }

    public <T> DataGeneratorObject<D> withValues(BiConsumer<D, T> setter, List<T> values) {
        setters.add(new SetterWithValues(setter, values));
        return this;
    }

    public List<D> build() {
        return buildModuleOptim(factory, setters);
//        return buildRecursive(factory, setters);
    }

    private List<D> buildModuleOptim(Supplier<D> factory, List<SetterWithValues> setters) {
        ArrayList<Integer> combinationNumber = getCombinationNumber(setters);
        List<Integer> nbValuesPerField = nbValuesPerField(setters);

        int combinationSize = combinationSize(setters);
        List<D> combinations = new ArrayList<>(combinationSize);
        for (int index = 0; index < combinationSize; index++) {
            int[] combinationForIndex = getCombinationForIndex(combinationNumber, nbValuesPerField, index);
            combinations.add(buildObject(factory, setters, combinationForIndex));
        }
        return combinations;
    }

    private int combinationSize(List<SetterWithValues> setters) {
        return setters.stream().mapToInt(s -> s.values.size()).reduce(1, (a, b) -> a * b);
    }

    private List<Integer> nbValuesPerField(List<SetterWithValues> setters) {
        return setters.stream()
                .map(s -> s.values.size())
                .collect(Collectors.toList());
    }

    private ArrayList<Integer> getCombinationNumber(List<SetterWithValues> setters) {
        ArrayList<Integer> combinationNumber = new ArrayList<>();
        combinationNumber.add(1);
        setters.stream().forEach(setter -> combinationNumber.add(combinationNumber.get(Math.max(0, combinationNumber.size() - 1)) * setter.values.size()));
        return combinationNumber;
    }

    private D buildObject(Supplier<D> factory, List<SetterWithValues> setters, int[] ints) {
        D instance = factory.get();
        for (int i = 0; i < ints.length; i++) {
            SetterWithValues setterWithValues = setters.get(i);
            setterWithValues.setter.accept(instance, setterWithValues.values.get(ints[i]));
        }
        return instance;
    }

    private int[] getCombinationForIndex(ArrayList<Integer> combinationNumber, List<Integer> nbPerField, int globalIndex) {
        int[] combination = new int[nbPerField.size()];
        for (int i = 0; i < combination.length; i++) {
            combination[i] = ((globalIndex / combinationNumber.get(i)) % nbPerField.get(i));
        }
        return combination;
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