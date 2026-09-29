package vn.fit.jakartaee_baitap2_tuan3.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mariadb://localhost:3306/storedb";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";

    private DBConnection() {}

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException("Không tìm thấy MariaDB hoặc MySQL JDBC Driver", ex);
            }
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
