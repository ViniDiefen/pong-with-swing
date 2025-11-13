package br.com.vinidiefen.pong.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

public class PostgresConnection {
    private static PostgresConnection instance;
    private String url;
    private String user;
    private String password;

    private PostgresConnection() {
        Optional<String> url = Optional.ofNullable(System.getenv("POSTGRES_URL"));
        Optional<String> user = Optional.ofNullable(System.getenv("POSTGRES_USER"));
        Optional<String> password = Optional.ofNullable(System.getenv("POSTGRES_PASSWORD"));

        this.url = url.orElse("jdbc:postgresql://localhost:5432/pws");
        this.user = user.orElse("docker");
        this.password = password.orElse("docker");
    }

    public static PostgresConnection def() {
        if (instance == null) {
            instance = new PostgresConnection();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}