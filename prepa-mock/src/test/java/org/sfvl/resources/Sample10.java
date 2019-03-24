package org.sfvl.resources;

import java.sql.Connection;
import java.sql.SQLException;

public class Sample10 {

    public Connection connection;

    public void insert() throws SQLException {
        String sql = "select * from table";
        connection.prepareStatement(sql);
    }

}
