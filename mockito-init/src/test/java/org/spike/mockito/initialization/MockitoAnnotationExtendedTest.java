package org.spike.mockito.initialization;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.debugging.VerboseMockInvocationLogger;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.spike.mockito.MockitoAnnotationExtended;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * https://www.baeldung.com/mockito-annotations
 */
public class MockitoAnnotationExtendedTest {

    private VerboseMockInvocationLogger VERBOSE_LISTENER;
    private ByteArrayOutputStream out;

    public static class VerboseAnswer<T> implements Answer<T> {

        @Override
        public T answer(InvocationOnMock invocationOnMock) throws Throwable {
            return null;
        }
    }

    public static final Answer VERBOSE = new VerboseAnswer<Object>();


    @BeforeEach
    public void init() {
        out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        VERBOSE_LISTENER = new VerboseMockInvocationLogger(ps);
    }


    @Mock
    private Dao dao;

    @InjectMocks
    private Service service;


    @Test
    public void fields_should_be_created_and_valued_after_initialization() {
        MockitoAnnotationExtended.initMocks(this);

        assertNotNull(service);
        assertNotNull(dao);
        assertNotNull(service.getDao());
    }

    @Test
    public void fields_should_be_injected_without_recreate_them_when_already_instantiate() throws IllegalAccessException {
        service = new Service();
        dao = Mockito.mock(Dao.class);
        Mockito.when(dao.getId()).thenReturn("Initial mock");

        MockitoAnnotationExtended.initMocks(this);

        assertEquals("Initial mock", service.getDao().getId());
    }


    @Test
    public void fields_should_be_valued_after_initialization_with_mock_to_create_with_settings() throws IllegalAccessException {
        service = new Service();

        MockitoAnnotationExtended.initMocks(this, Mockito.withSettings().invocationListeners(VERBOSE_LISTENER));

        service.getDao().findById(4);

        String log = out.toString();
        String expectedString = "findById(4L)";
        assertTrue(log.contains(expectedString), "Log not contains '"+expectedString+"' in :" + log);
    }

    @Test
    public void fields_should_be_valued_after_initialization_with_mock_without_override_settings() throws IllegalAccessException {
        service = new Service();
        dao = Mockito.mock(Dao.class);

        MockitoAnnotationExtended.initMocks(this, Mockito.withSettings().invocationListeners(VERBOSE_LISTENER));

        service.getDao().findById(4);

        assertTrue(out.toString().isEmpty());
    }

    public static class MyClassWithStaticFinal {
        @Mock
        private Dao dao;

        @InjectMocks
        private ServiceWithStaticFinalField service = new ServiceWithStaticFinalField();
    }

    @Test
    public void fields_should_be_valued_after_initialization_even_some_final_static_field() throws IllegalAccessException {

        MyClassWithStaticFinal myClass = new MyClassWithStaticFinal();
        MockitoAnnotationExtended.initMocks(myClass);

        myClass.service.getDao().findById(4);

        assertTrue(out.toString().isEmpty());
    }

    public static class MyClassWithStatic {
        @Mock
        private Dao dao;

        @InjectMocks
        private ServiceWithStaticField service = new ServiceWithStaticField();
    }

    @Test
    public void static_fields_should_not_be_instantiate() throws IllegalAccessException {

        MyClassWithStatic myClass = new MyClassWithStatic();
        MockitoAnnotationExtended.initMocks(myClass);

        assertNull(myClass.service.getDao());
    }

    @Test
    public void should_inject_mock_that_is_an_anonymous_class() throws IllegalAccessException {
        service = new Service();
        dao = new Dao() {

            @Override
            public String getId() {
                return "Anonymous DAO";
            }

            @Override
            public List<Object> findAll() {
                return null;
            }

            @Override
            public Object findById(long id) {
                return null;
            }
        };

        MockitoAnnotationExtended.initMocks(this);

        assertEquals("Anonymous DAO", service.getDao().getId());

    }


}
