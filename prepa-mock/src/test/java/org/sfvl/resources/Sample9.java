package org.sfvl.resources;

import java.sql.Connection;
import java.sql.SQLException;

public class Sample9 {

    public Connection connection;

    public void insert1() throws SQLException {
        connection.createStatement();
    }

    public void insertToRecord() throws SQLException {
        connection.close();
    }

    public void insert2() throws SQLException {
        connection.prepareStatement("select * from table");
    }
}
