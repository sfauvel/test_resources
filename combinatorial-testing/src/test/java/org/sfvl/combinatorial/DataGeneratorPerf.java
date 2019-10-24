package org.sfvl.combinatorial;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DataGeneratorPerf {

    /**
     * This method have to be executed from the main method to check
     * performance and must not be run with tests.
     */
    public void execute_with_a_large_number() {

        List<String> values = IntStream.range(0, 10)
                .mapToObj(Integer::toString)
                .collect(Collectors.toList());

        long begin = System.currentTimeMillis();
        List<Person> people = new DataGenerator<>(Person::new)
                .with(Person::setFirstName, values) // 10
                .with(Person::setFirstName, values) // 100
                .with(Person::setFirstName, values) // 1000
                .with(Person::setFirstName, values) // 10000
                .with(Person::setFirstName, values) // 100000
                .with(Person::setFirstName, values) // 1000000
                .with(Person::setFirstName, values) // 10000000
                .build();

        long end = System.currentTimeMillis();
        System.out.println("Nb: " + people.size());
        System.out.println("Time: " + (end - begin) + "ms");
    }

    public static void main(String... args) {
        new DataGeneratorPerf().execute_with_a_large_number();
    }

}