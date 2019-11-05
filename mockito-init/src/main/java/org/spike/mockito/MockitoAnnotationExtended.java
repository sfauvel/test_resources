package org.spike.mockito;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockSettings;
import org.mockito.Mockito;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Mockito extension to init fields without recreate them.
 *
 * Mockito has a method to init mocks using annotation calling: MockitoAnnotations.initMocks(testClass);
 * see: https://static.javadoc.io/org.mockito/mockito-core/3.1.0/org/mockito/Mockito.html#mock_annotation
 *
 * MockitoAnnotationExtended change this method initMocks to create mocks only if they are not already existed.
 * It's now possible to initiate our own mock and inject it.
 */
public class MockitoAnnotationExtended {

    public static void initMocks(Object objectWithAnnotations) {
        new MockitoAnnotationExtended().init(objectWithAnnotations, Mockito.withSettings());
    }


    public static void initMocks(Object objectWithAnnotations, MockSettings mockSettings) {
        new MockitoAnnotationExtended().init(objectWithAnnotations, mockSettings);
    }

    public void init(Object objectWithAnnotations, MockSettings mockSettings) {
        instantiateMocks(objectWithAnnotations, mockSettings);
        instantiateInjectMocks(objectWithAnnotations);

        final Map<? extends Class<?>, Object> mockInstances = getMocksByType(objectWithAnnotations);

        getAnnotatedFields(objectWithAnnotations, InjectMocks.class).stream()
                .map(field -> getValue(objectWithAnnotations, field))
                .forEach(value -> injectsInto(value, mockInstances));
    }

    private void instantiateInjectMocks(Object instance) {
        List<Field> injectMockFields = getAnnotatedFields(instance, InjectMocks.class);
        injectMockFields.stream()
                .filter(field -> getValue(instance, field) == null)
                .forEach(field -> setValue(instance, field, newInstance(field)));
    }


    private void instantiateMocks(Object instance, MockSettings mockSettings) {
        List<Field> mockFields = getAnnotatedFields(instance, Mock.class);
        mockFields.stream()
                .filter(field -> getValue(instance, field) == null)
                .forEach(field -> setValue(instance, field, Mockito.mock(field.getType(), mockSettings)));
    }

    private Map<? extends Class<?>, Object> getMocksByType(Object objectWithAnnotations) {
        List<Field> mockFields = getAnnotatedFields(objectWithAnnotations, Mock.class);
        return mockFields.stream().collect(Collectors.toMap(
                f -> f.getType(),
                f -> getValue(objectWithAnnotations, f)
        ));
    }


    private List<Field> getAnnotatedFields(Object objectWithAnnotations, Class<? extends Annotation> annotationClass) {
        return filterFieldsWithAnnotation(objectWithAnnotations.getClass().getDeclaredFields(), annotationClass);
    }

    private List<Field> filterFieldsWithAnnotation(Field[] declaredFields, Class<? extends Annotation> annotationClass) {
        return Arrays.stream(declaredFields)
                .filter(field -> field.isAnnotationPresent(annotationClass))
                .collect(Collectors.toList());
    }

    private void injectsInto(Object instance, Map<? extends Class<?>, Object> mockValueByType) {
        Arrays.stream(instance.getClass().getDeclaredFields())
                .filter(field -> !isStatic(field))
                .forEach(field -> setValue(instance, field, mockValueByType.get(field.getType())));

//        Field[] declaredFields = instance.getClass().getDeclaredFields();
//        for (Field declaredField : declaredFields) {
//            setValue(instance, declaredField, mockValueByType.get(declaredField.getType()));
//        }
    }

    private boolean isStatic(Field field) {
        return Modifier.isStatic(field.getModifiers());
    }

    private Object newInstance(Field field) {
        try {
            return field.getType().getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private Object getValue(Object object, Field field) {
        try {
            field.setAccessible(true);
            return field.get(object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void setValue(Object object, Field field, Object value) {
        try {
            field.setAccessible(true);
            field.set(object, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
