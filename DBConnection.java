
import java.sql.*;

class DBConnection {
    static final String DB_URL = "jdbc:mysql://localhost:3306/robotsDB";
    static final String USER = "root";
    static final String PASS = "Shan1234!";
    Connection conn;

    Connection getConnection() {
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
