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
import static org.sfvl.combinatorial.CombinatorialTool.$;


/**
 *
 */
public class CombinatorialServiceTest {

    private Service service = new Service();

    // Create list of values to use for each attribute with on good value and one bad value.
    private static List<CombinatorialTool.Value<Person, ?>> values = Arrays.asList(
            $(Person::setFirstName, "John", "Bob"),
            $(Person::setLastName, "Doe", "Morane"),
            $(Person::setActive, true, false));


    @BeforeEach
    public void setup() throws IllegalAccessException, InstantiationException {
        // Add an instance in service
        service.add(CombinatorialTool.buildInstance(Person.class, values));
    }

    @Test
    public void should_find_nothing_when_filter_not_match() throws InstantiationException, IllegalAccessException {

         // Generate filters replacing just one value and check, no record returned.
        List<Person> wrongFilters = CombinatorialTool.buildInstanceWithOneWrongValue(Person.class, values);
        for (Person wrongFilter : wrongFilters) {
            assertEquals(0, service.find(wrongFilter).size());
        }
    }

    private static Stream<Arguments> generateWrongFilter() throws InstantiationException, IllegalAccessException {
        return CombinatorialTool.buildInstanceWithOneWrongValue(Person.class, values).stream()
                .map(Arguments::of);
    }

    @ParameterizedTest
    @MethodSource("generateWrongFilter")
    public void should_find_nothing_when_filter_not_match(Person wrongFilter) throws InstantiationException, IllegalAccessException {
        assertEquals(0, service.find(wrongFilter).size());
    }

    @Test
    public void should_find_instance_when_filter_is_ok() throws InstantiationException, IllegalAccessException {

        // Find person using person itself as a filter.
        List<Person> people = service.find(CombinatorialTool.buildInstance(Person.class, values));
        assertEquals(1, people.size());
        assertEquals("John", people.get(0).getFirstName());
    }
}