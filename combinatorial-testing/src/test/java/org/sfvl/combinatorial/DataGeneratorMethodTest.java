package org.sfvl.combinatorial;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DataGeneratorMethodTest {

    @Test
    public void should_generate_combination_using_a_method() {

        DataGeneratorMethod<Person> generator = new DataGeneratorMethod<Person>();
        List<Person> people = DataGeneratorMethod.buildValues(
                Person::new,
                Arrays.asList(
                        generator.mapValues((o, __) -> o.setFirstName(__), Arrays.asList("John", "Bob")),
                        generator.mapValues((o, __) -> o.setLastName(__), Arrays.asList("Doe", "Morane")),
                        generator.mapValues((o, __) -> o.setActive(__), Arrays.asList(true, false))
                ));


        assertEquals(8, people.size());
        assertContains(people, createPerson("John", "Doe", true));
        assertContains(people, createPerson("Bob", "Doe", true));
        assertContains(people, createPerson("John", "Morane", true));
        assertContains(people, createPerson("Bob", "Morane", true));
        assertContains(people, createPerson("John", "Doe", false));
        assertContains(people, createPerson("Bob", "Doe", false));
        assertContains(people, createPerson("John", "Morane", false));
        assertContains(people, createPerson("Bob", "Morane", false));

    }


    private void assertContains(List<Person> dataGenerated, Person person) {
        assertTrue(dataGenerated.stream()
                .anyMatch(data -> data.getFirstName().equals(person.getFirstName())
                        && data.getLastName().equals(person.getLastName())
                        && data.isActive() == person.isActive()));
    }

    private Person createPerson(String firstName, String lastName, boolean active) {
        Person person = new Person();
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setActive(active);
        return person;
    }

}