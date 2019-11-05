package org.spike.mockito.initialization;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * https://www.baeldung.com/mockito-annotations
 */
public class WithAnnotation {

    @Mock
    private Dao dao;

    private Logger logger = Logger.getAnonymousLogger();

    @InjectMocks
    private Service service;

    @Test
    public void fields_should_be_null_before_initialization() {
        assertNull(service);
        assertNull(dao);
    }

    @Test
    public void fields_should_be_valued_after_initialization() {

        MockitoAnnotations.initMocks(this);
        assertNotNull(service);
        assertNotNull(dao);
    }

    @Test
    public void mocks_should_be_injected_into_field_annotates_with_InjectMocks() {

        MockitoAnnotations.initMocks(this);
        assertNotNull(service.getDao());
    }

    @Test
    public void without_annotation_fields_should_not_be_injected_into_field_annotates_with_InjectMocks() {

        MockitoAnnotations.initMocks(this);
        assertNull(service.getLogger());
    }
}
