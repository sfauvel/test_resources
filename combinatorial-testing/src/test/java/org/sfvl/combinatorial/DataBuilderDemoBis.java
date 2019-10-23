package org.sfvl.combinatorial;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 *
 */
public class DataBuilderDemoBis {

    public static class Data extends DataBuilder<Data> {
        Person person;

        /// To be display as test label.
        @Override
        public String toString() {
            return person.getFirstName() + " " + person.getLastName();
        }
    }

    private static Stream<Arguments> generateWrongFilter() throws InstantiationException, IllegalAccessException {

        Data data = new Data() {
            @Override
            public List<MapSetterList> values() {
                return Arrays.asList(
                        mapValues(__ -> person.setFirstName(__), Arrays.asList("John", "Bob")),
                        mapValues(__ -> person.setLastName(__), Arrays.asList("Doe", "Morane")),
                        mapValues(__ -> person.setActive(__), Arrays.asList(true, false))
                );
            }
        };


        Predicate<Data> personReference = d ->
                d.person.getFirstName().equals("John")
                && d.person.getLastName().equals("Doe")
                && d.person.isActive() == true;

        return data.buildValues().stream()
                .filter(personReference.negate())
                .map(d -> d.person)
                .map(Arguments::of);

    }

    @ParameterizedTest
    @MethodSource("generateWrongFilter")
    public void should_find_nothing_when_filter_not_match(Person wrongFilter) throws InstantiationException, IllegalAccessException {

        Service service = new Service();
        service.add(createPerson("John", "Doe", true));

        assertEquals(0, service.find(wrongFilter).size());
    }

    private Person createPerson(String firstName, String lastName, boolean active) {
        Person person = new Person();
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setActive(active);
        return person;
    }
}