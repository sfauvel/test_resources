package org.sfvl.combinatorial;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Service {

    List<Person> people = new ArrayList<Person>();

    public List<Person> find(Person filter) {

        List<Predicate<? super Person>> predicates = new ArrayList<>();

        if (filter.getFirstName() != null) {
            predicates.add(p -> filter.getFirstName().equals(p.getFirstName()));
        }
        if (filter.getLastName() != null) {
            predicates.add(p -> filter.getLastName().equals(p.getLastName()));
        }
//        if (filter.isActive()) {
            predicates.add(p -> filter.isActive()==p.isActive());
//        }


        return people.stream().filter(
                person -> predicates.stream().allMatch(predicate -> predicate.test(person))
        ).collect(Collectors.toList());
    }

    public void add(Person person) {
        people.add(person);
    }
}
