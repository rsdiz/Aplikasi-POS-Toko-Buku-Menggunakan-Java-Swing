package tokobuku.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Rosyid Iz
 */
public class ConnectionUtil {

    private static Connection dbConnection = null;
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost/toko_buku";
    private static final String USER = "root";
    private static final String PASS = "";

    public static Connection getDB() {
        try {
            // register driver yang akan digunakan
            Class.forName(JDBC_DRIVER);
            // mengkoneksikan database
            dbConnection = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver Tidak ditemukan: " + e.getMessage());
            return null;
        } catch (SQLException e) {
            System.out.println("Koneksi Database gagal: " + e.getMessage());
            return null;
        }
        
        return dbConnection;
    }

}