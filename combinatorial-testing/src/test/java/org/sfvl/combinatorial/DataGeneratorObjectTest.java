package org.sfvl.combinatorial;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DataGeneratorObjectTest {

    @Test
    public void should_generate_using_lambda_for_setter() {

        List<Person> people = new DataGeneratorObject<Person>(Person::new) {{
            withValues((o, __) -> o.setFirstName(__), asList("John", "Bob"));
            withValues((o, __) -> o.setLastName(__), asList("Doe", "Morane"));
            withValues((o, __) -> o.setActive(__), asList(true, false));
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

        List<Person> people = new DataGeneratorObject<Person>(Person::new) {{
            withValues(Person::setFirstName, asList("John", "Bob"));
            withValues(Person::setLastName, asList("Doe", "Morane"));
            withValues(Person::setActive, asList(true, false));
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

        List<Person> people = new DataGeneratorObject<>(Person::new)
                .withValues(Person::setFirstName, asList("John", "Bob"))
                .withValues((o, __) -> o.setLastName(__), asList("Doe", "Morane"))
                .withValues(Person::setActive, asList(true, false))
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

        List<Person> people = new DataGeneratorObject<>(Person::new)
                .withValues(Person::setFirstName, asList("John", "Bob", "Kim"))
                .withValues((o, __) -> o.setLastName(__), asList("Doe", "Morane", "Lee", "Williams", "Marley"))
                .withValues(Person::setActive, asList(true, false))
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

    /**
     * This method have to be executed from the main method to check
     * performance and must not be run with tests.
     */
    public void execute_with_a_large_number() {

        List<String> values = IntStream.range(0, 10)
                .mapToObj(Integer::toString)
                .collect(Collectors.toList());

        long begin = System.currentTimeMillis();
        List<Person> people = new DataGeneratorObject<>(Person::new)
                .withValues(Person::setFirstName, values) // 10
                .withValues(Person::setFirstName, values) // 100
                .withValues(Person::setFirstName, values) // 1000
                .withValues(Person::setFirstName, values) // 10000
                .withValues(Person::setFirstName, values) // 100000
                .withValues(Person::setFirstName, values) // 1000000
                .withValues(Person::setFirstName, values) // 10000000
                .build();

        long end = System.currentTimeMillis();
        System.out.println("Nb: " + people.size());
        System.out.println("Time: " + (end - begin) + "ms");
    }

    public static void main(String... args) {
        new DataGeneratorObjectTest().execute_with_a_large_number();
    }

}