package org.spike.mockito.initialization;

public class ServiceWithStaticField {

    private static Dao dao;

    public Dao getDao() {
        return dao;
    }

    public void setDao(Dao dao) {
        this.dao = dao;
    }
}

