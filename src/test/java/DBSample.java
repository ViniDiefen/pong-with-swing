import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBSample {

    /** URL for DB connection */
    private String url;
    /** User for DB connection */
    private String user;
    /** Password for DB connection */
    private String password;

    public static void main(String[] args) {
        String url = System.getenv("DB_CONNECTION_URL");
        String user = System.getenv("DB_CONNECTION_USER");
        String password = System.getenv("DB_CONNECTION_PASSWORD");

        DBSample dbSample = new DBSample(url, user, password);
        
        dbSample.create();
        dbSample.read();
    }

    public DBSample(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public void create() {
        String sql = "INSERT INTO pws.paddle (id, x, y, is_left_player) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "f6a8f7de-5e4a-4b7d-9dc0-62fceac4b3a7");
            stmt.setInt(2, 200);
            stmt.setInt(3, 200);
            stmt.setBoolean(4, true);
            stmt.executeUpdate();
            System.out.println("User created successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void read() {
        String sql = "SELECT * FROM pws.paddle";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "f6a8f7de-5e4a-4b7d-9dc0-62fceac4b3a7");
            stmt.setInt(2, 200);
            stmt.setInt(3, 200);
            stmt.setBoolean(4, true);
            stmt.executeUpdate();
            System.out.println("User created successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
