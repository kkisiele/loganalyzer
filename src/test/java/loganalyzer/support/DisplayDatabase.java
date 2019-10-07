package loganalyzer.support;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class DisplayDatabase {
    public static void main(String[] args) throws Exception {
        try(Connection connection = DriverManager.getConnection("jdbc:hsqldb:file:eventdb", "SA", "")) {
            ResultSet rs = connection.createStatement().executeQuery("select count(*) from events");
            rs.next();
            System.out.println(rs.getInt(1));
        }
    }
}
