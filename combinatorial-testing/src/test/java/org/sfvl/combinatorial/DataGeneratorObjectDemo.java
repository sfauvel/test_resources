package org.sfvl.combinatorial;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DataGeneratorObjectDemo {

    private static final List<Boolean> BOOLEANS = asList(true, false);

    /**
     * We can redefine toString to be display on test.
     */
    public static class PersonWithName extends Person {
        @Override
        public String toString() {
            return this.getFirstName() + " " + this.getLastName();
        }
    }

    private static Stream<Arguments> generateValues() throws InstantiationException, IllegalAccessException {

        return new DataGenerator<Person>(PersonWithName::new) {{
            with(Person::setFirstName, asList("John", "Jack"));
            with(Person::setLastName, asList("Doe", "Morane"));
            with(Person::setActive, BOOLEANS);
        }}.build().stream()
                .filter(person -> true) // Filter some not wanted combinations.
                .map(Arguments::of);


    }

    @ParameterizedTest
    @MethodSource("generateValues")
    public void should_test_with_person(Person person) throws InstantiationException, IllegalAccessException {
        assertTrue(person.getFirstName().startsWith("J"));
    }
}