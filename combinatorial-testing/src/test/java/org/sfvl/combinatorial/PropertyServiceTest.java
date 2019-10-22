package org.sfvl.combinatorial;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 *
 */
public class PropertyServiceTest {


    public static class Data extends PropertyTesting {
        String firstName;
        String lastName;
        boolean isActive;
    }

    Person person;

    @BeforeEach
    public void setup() throws IllegalAccessException, InstantiationException {
        person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setActive(true);
    }



    @Test
    public void should_check() {
        new Data() {
            @Override
            public List<MapSetterList> values() {
                return Arrays.asList(
                        mapValues(__ -> firstName = __, Arrays.asList(person.getFirstName(), "Bob")),
                        mapValues(__ -> lastName = __, Arrays.asList(person.getLastName(), "Morane")),
                        mapValues(__ -> isActive = __, Arrays.asList(person.isActive(), false))
                );
            }

            @Override
            public boolean filter() {
                return !(firstName.equals(person.getFirstName())
                        && lastName.equals(person.getLastName())
                        && isActive==person.isActive());
            }

            @Override
            public void test() {
                showValues();

                Person filter = new Person();
                filter.setFirstName(firstName);
                filter.setLastName(lastName);
                filter.setActive(isActive);

                Service service = new Service();
                service.add(person);
                assertEquals(0, service.find(filter).size());

            }
        }.runTest();
    }

}