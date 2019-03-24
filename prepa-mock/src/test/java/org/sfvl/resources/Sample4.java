package org.sfvl.resources;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Sample4 {

    public Connection connection;

    public void insert() {
        try {
            Statement statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException("Exception", e);
        }

    }
}
