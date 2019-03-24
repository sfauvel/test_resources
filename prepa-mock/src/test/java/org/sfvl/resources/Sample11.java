package org.sfvl.resources;

import java.sql.Connection;
import java.sql.SQLException;

public class Sample11 {

    public static class RequestBuilder {

        public void close() {
        }
    }
    
    public void insert(RequestBuilder builder) throws SQLException {
        builder.close();
    }
}
