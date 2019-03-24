package org.sfvl.resources;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Sample5 {

    public Connection connection;

    public void insert() throws SQLException {
        // No affectation of the return value
        connection.createStatement();

    }
}
