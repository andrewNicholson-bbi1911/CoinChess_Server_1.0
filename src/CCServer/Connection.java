package CCServer;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Connection {

    static Connection connection = null;
    static String databaseMain = "";
    static String url = "jdbc:mysql://localhost:8889/"+databaseMain;

    static String username = "root";
    static String password="root";


    public static void main(String[] str) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        connection = (Connection) DriverManager.getConnection(url, username, password);
    }

}
