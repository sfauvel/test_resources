package org.sfvl.combinatorial;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
public class DataBuilderDemo {

    public static class Data extends DataBuilder<Data> {
        public String firstName;
        String lastName;
        boolean isActive;

        /// To be display as test label.
        @Override
        public String toString() {
            return firstName + " " + lastName;
        }
    }

    static Person person;
    Service service;

    @BeforeAll
    public static void init() {
        person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setActive(true);
    }

    @BeforeEach
    public void setup() throws IllegalAccessException, InstantiationException {
        service = new Service();
        service.add(person);
    }

    private static Stream<Arguments> generateWrongFilter() throws InstantiationException, IllegalAccessException {

        Data data = new Data() {
            @Override
            public List<MapSetterList> values() {
                return Arrays.asList(
                        mapValues(__ -> firstName = __, Arrays.asList(person.getFirstName(), "Bob")),
                        mapValues(__ -> lastName = __, Arrays.asList(person.getLastName(), "Morane")),
                        mapValues(__ -> isActive = __, Arrays.asList(person.isActive(), false))
                );
            }
        };

        Predicate<Data> notPersonReference = d -> !(d.firstName.equals(person.getFirstName())
                && d.lastName.equals(person.getLastName())
                && d.isActive == person.isActive());

        return data.buildValues().stream()
                .filter(notPersonReference)
                .map(DataBuilderDemo::toPerson)
                .map(Arguments::of);

    }

    @ParameterizedTest
    @MethodSource("generateWrongFilter")
    public void should_find_nothing_when_filter_not_match(Person wrongFilter) throws InstantiationException, IllegalAccessException {
        assertEquals(0, service.find(wrongFilter).size());
    }

    private static Person toPerson(Data data) {
        Person person = new Person();
        person.setFirstName(data.firstName);
        person.setLastName(data.lastName);
        person.setActive(data.isActive);
        return person;
    }


}