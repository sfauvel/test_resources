package org.spike.mockito.initialization;

import java.util.List;

public interface Dao {
    String getId();
    List<Object> findAll();
    Object findById(long id);
}
