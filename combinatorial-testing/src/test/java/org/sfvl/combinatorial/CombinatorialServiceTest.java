package org.sfvl.combinatorial;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CombinatorialServiceTest {

    Service service = new Service();

    @Test
    public void should_find_nothing_when_filter_not_match() throws InstantiationException, IllegalAccessException {

        List<Value<Person, ?>> values = Arrays.asList(
                $(Person::setFirstName, "John", "Bob"),
                $(Person::setLastName, "Doe", "Morane"),
                $(Person::setActive, true, false));


        Person personToInject = buildInstance(Person.class, values);
        service.add(personToInject);

        // Find person using person itself has filter
        List<Person> people = service.find(personToInject);
        assertEquals(1, people.size());
        assertEquals("John", people.get(0).getFirstName());

        // Generate filters replacing just one value and chack, no record returned.
        List<Person> wrongFilters = buildFilter(Person.class, values);
        for (Person wrongFilter : wrongFilters) {
            System.out.println("Search: " + wrongFilter.toString());
            assertEquals(0, service.find(wrongFilter).size());
        }
       }

    <O, T> Value<O, T> $(BiConsumer<O, T> setter, T value, T wrongValue) {
        return createValue(setter, value, wrongValue);
    }
    <O, T> Value<O, T> createValue(BiConsumer<O, T> setter, T value, T wrongValue) {
        return new Value(setter, value, wrongValue);
    }

    class Value<O, T> {

        final private T value;
        final private T wrongValue;
        final private BiConsumer<O, T> setter;

        public Value(BiConsumer<O, T> setter, T value, T wrongValue) {
            this.value = value;
            this.wrongValue = wrongValue;
            this.setter = setter;
        }

        public T getValue() {
            return value;
        }

        public T getWrongValue() {
            return wrongValue;
        }

        public BiConsumer<O, T> getSetter() {
            return setter;
        }

        public void setRightValue(O p) {
            setter.accept(p, getValue());
        }

        public void setWrongValue(O p) {
            setter.accept(p, getWrongValue());
        }
    }

    private <O> List<O> buildFilter(Class<O> clazz, List<Value<O, ?>> values) throws IllegalAccessException, InstantiationException {

        List<O> result = new ArrayList<>();

        for (Value<O, ?> value : values) {
            O obj = buildInstance(clazz, values);
            value.setWrongValue(obj);
            result.add(obj);
        }
        return result;
    }

    private <O> O buildInstance(Class<O> clazz, List<Value<O, ?>> values) throws InstantiationException, IllegalAccessException {
        O p = clazz.newInstance();
        for (Value<O, ?> value : values) {
            value.setRightValue(p);
        }
        return p;
    }

}