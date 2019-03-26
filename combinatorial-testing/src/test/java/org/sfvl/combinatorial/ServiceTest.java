package org.sfvl.combinatorial;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ServiceTest {

    Service service = new Service();


    @Test
    public void should_find_nothing_when_filter_not_match() {
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setActive(true);
        service.add(person);


        Person personFilter = new Person();
        personFilter.setFirstName("Bob");
        personFilter.setLastName("Morane");

        List<Person> people = service.find(personFilter);
        assertEquals(0, people.size());
    }

    @Test
    public void should_() {
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setActive(true);
        service.add(person);

        List<Person> people = service.find(person);
        assertEquals("John", people.get(0).getFirstName());
        assertEquals(1, people.size());
    }

    @Test
    public void should_filter_is_active() {
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setActive(true);
        service.add(person);

        Person personFilter = new Person();
        personFilter.setActive(true);

        List<Person> people = service.find(person);
        assertEquals("John", people.get(0).getFirstName());
        assertEquals(1, people.size());
    }

    @Test
    public void should_filter_is_inactive() {
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setActive(true);
        service.add(person);

        Person personFilter = new Person();
        personFilter.setActive(false);

        List<Person> people = service.find(person);
        assertEquals(0, people.size());
    }
}