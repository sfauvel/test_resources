package org.sfvl.combinatorial;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class DataBuilderTest {

    public static class Data extends DataBuilder<Data> {
        String firstName;
        String lastName;
        boolean isActive;
    }

    @Test
    public void should() {
        Data data = new Data() {
            @Override
            public List<MapSetterList> values() {
                return Arrays.asList(
                        mapValues(__ -> firstName = __, Arrays.asList("John", "Bob")),
                        mapValues(__ -> lastName = __, Arrays.asList("Doe")),
                        mapValues(__ -> isActive = __, Arrays.asList(true, false))
                );
            }
        };

        List<Data> dataGenerated = data.buildValues();
        assertEquals(4, dataGenerated.size());


        assertContains(dataGenerated, createPerson("John", "Doe", true));
        assertContains(dataGenerated, createPerson("Bob", "Doe", true));
        assertContains(dataGenerated, createPerson("John", "Doe", false));
        assertContains(dataGenerated, createPerson("Bob", "Doe", false));

    }

    private void assertContains(List<Data> dataGenerated, Person person) {
        assertTrue(dataGenerated.stream()
                .anyMatch(data -> data.firstName.equals(person.getFirstName())
                        && data.lastName.equals(person.getLastName())
                        && data.isActive == person.isActive()));
    }

    private Person createPerson(String firstName, String lastName, boolean active) {
        Person person = new Person();
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setActive(active);
        return person;
    }

}