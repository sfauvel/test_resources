package org.spike.mockito.initialization;

import java.util.logging.Logger;

public class ServiceWithStaticFinalField {

    private static final String STATIC_FINAL_FIELD = "static final field";
    private static String STATIC_FIELD = "static field";
    private final String FINAL_FIELD = "final field";

    private Dao dao;

    public Dao getDao() {
        return dao;
    }

    public void setDao(Dao dao) {
        this.dao = dao;
    }
}

