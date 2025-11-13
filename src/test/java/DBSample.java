import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import br.com.vinidiefen.pong.database.PostgresConnection;

public class DBSample {

    /** URL for DB connection */
    private String url;
    /** User for DB connection */
    private String user;
    /** Password for DB connection */
    private String password;

    public static void main(String[] args) {
        create();
        read();
    }

    public static void create() {
        String sql = "INSERT INTO paddle (id, x, y, is_left_player) VALUES (?, ?, ?, ?)";

        try (Connection conn = PostgresConnection.def().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            UUID uuid = UUID.randomUUID();
            stmt.setObject(1, uuid);
            stmt.setInt(2, 200);
            stmt.setInt(3, 200);
            stmt.setBoolean(4, true);
            stmt.executeUpdate();
            System.out.println("User created successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void read() {
        String sql = "SELECT * FROM paddle";

        try (Connection conn = PostgresConnection.def().getConnection();
                Statement stmt = conn.createStatement()) {
            ResultSet results = stmt.executeQuery(sql);
            System.out.print("Users: ");
            while (results.next()) {
                System.out.println(results.getObject("id") + ", " +
                        results.getInt("x") + ", " +
                        results.getInt("y") + ", " +
                        results.getBoolean("is_left_player"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
