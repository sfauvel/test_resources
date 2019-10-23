package org.sfvl.combinatorial;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 *
 */
public class DataBuilderDemoTer {

    public static class Data extends DataBuilder<Data> {
        Person person = new Person();

        @Override
        public List<MapSetterList> values() {
            return asList(
                    mapValues(__ -> person.setFirstName(__), asList("John", "Bob")),
                    mapValues(__ -> person.setLastName(__), asList("Doe", "Morane")),
                    mapValues(__ -> person.setActive(__), asList(true, false))
            );
        }
    }

    private static Stream<Arguments> generateWrongFilter() throws InstantiationException, IllegalAccessException {

        Predicate<Data> personReference = __ ->
                __.person.getFirstName().equals("John")
                        && __.person.getLastName().equals("Doe")
                        && __.person.isActive() == true;

        return new Data().buildValues().stream()
                .filter(personReference.negate())
                .map(Arguments::of);

    }

    @ParameterizedTest
    @MethodSource("generateWrongFilter")
    public void should_find_nothing_when_filter_not_match(Data wrongFilter) throws InstantiationException, IllegalAccessException {

        Service service = new Service();
        service.add(createPerson("John", "Doe", true));

        assertEquals(0, service.find(wrongFilter.person).size());
    }

    private Person createPerson(String firstName, String lastName, boolean active) {
        Person person = new Person();
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setActive(active);
        return person;
    }
}