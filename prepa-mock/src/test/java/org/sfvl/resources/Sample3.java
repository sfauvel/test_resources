package org.sfvl.resources;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Sample3 {

    public Connection connection;

    public void insert() throws SQLException {
        Statement statement = connection.createStatement();

    }
}
