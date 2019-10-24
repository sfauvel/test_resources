package org.sfvl.combinatorial;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DataGeneratorTest {

    static class ConcreteGenerator extends DataGenerator<Person> {
        public ConcreteGenerator() {
            super(Person::new);
            with((o, __) -> o.setFirstName(__), asList("John", "Bob"));
            with((o, __) -> o.setLastName(__), asList("Doe", "Morane"));
            with((o, __) -> o.setActive(__), asList(true, false));
        }
    }

    @Test
    public void should_generate_using_a_concrete_class() {

        List<Person> people = new ConcreteGenerator().build();

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


    @Test
    public void should_generate_using_lambda_for_setter() {

        List<Person> people = new DataGenerator<Person>(Person::new)
                .with((o, __) -> o.setFirstName(__), asList("John", "Bob"))
                .with((o, __) -> o.setLastName(__), asList("Doe", "Morane"))
                .with((o, __) -> o.setActive(__), asList(true, false))
                .build();

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

    @Test
    public void should_generate_using_lambda_for_setter_with_static_constructor() {

        List<Person> people = new DataGenerator<Person>(Person::new) {{
            with((o, __) -> o.setFirstName(__), asList("John", "Bob"));
            with((o, __) -> o.setLastName(__), asList("Doe", "Morane"));
            with((o, __) -> o.setActive(__), asList(true, false));
        }}.build();

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

    @Test
    public void should_generate_using_method_reference_for_setter() {

        List<Person> people = new DataGenerator<Person>(Person::new) {{
            with(Person::setFirstName, asList("John", "Bob"));
            with(Person::setLastName, asList("Doe", "Morane"));
            with(Person::setActive, asList(true, false));
        }}.build();

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

    @Test
    public void should_generate_using_both_lambda_and_method_reference_for_setters() {

        List<Person> people = new DataGenerator<>(Person::new)
                .with(Person::setFirstName, asList("John", "Bob"))
                .with((o, __) -> o.setLastName(__), asList("Doe", "Morane"))
                .with(Person::setActive, asList(true, false))
                .build();

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


    @Test
    public void should_generate_combination_with_many_values() {

        List<Person> people = new DataGenerator<>(Person::new)
                .with(Person::setFirstName, asList("John", "Bob", "Kim"))
                .with((o, __) -> o.setLastName(__), asList("Doe", "Morane", "Lee", "Williams", "Marley"))
                .with(Person::setActive, asList(true, false))
                .build();

        assertContains(people, createPerson("John", "Doe", false));
        assertContains(people, createPerson("Bob", "Morane", true));
        assertContains(people, createPerson("Bob", "Marley", false));
        assertContains(people, createPerson("Kim", "Lee", false));

        assertEquals(3 * 5 * 2, people.size());
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