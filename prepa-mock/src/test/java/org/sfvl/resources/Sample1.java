package org.sfvl.resources;

import java.sql.Connection;
import java.sql.SQLException;

public class Sample1 {

    public Connection connection;

    public void insert() throws SQLException {
        connection.close();

    }
}
