import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Database {
    public static Connection connect() {
        try {
            return DriverManager.getConnection("jdbc:sqlite:myapp.db");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void clearTable(Connection con) {
        try (Statement stmt = con.createStatement()) {

            stmt.executeUpdate("""
                        DELETE FROM HISTORY;
                    """);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void createTable(Connection conn) {
        try (Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("""
                        CREATE TABLE IF NOT EXISTS HISTORY (
                            RECORD TEXT NOT NULL,
                            DATE TEXT NOT NULL
                        );
                    """);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    static void main(String[] args) throws Exception {
        createTable(connect());

    }
}
