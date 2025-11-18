package br.com.vinidiefen.pong.infrastructure.persistence.repositories.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Singleton class for managing PostgreSQL database connections.
 * Uses environment variables or defaults to local Docker configuration.
 */
public class PostgresConnection {
    private static PostgresConnection instance;
    private final String url;
    private final String user;
    private final String password;

    private PostgresConnection() {
        this.url = Optional.ofNullable(System.getenv("POSTGRES_URL")).orElse("jdbc:postgresql://localhost:5432/pws");
        this.user = Optional.ofNullable(System.getenv("POSTGRES_USER")).orElse("docker");
        this.password = Optional.ofNullable(System.getenv("POSTGRES_PASSWORD")).orElse("docker");
    }

    /**
     * Gets the singleton instance of PostgresConnection.
     * 
     * @return the PostgresConnection instance
     */
    public static PostgresConnection getInstance() {
        if (instance == null) {
            instance = new PostgresConnection();
        }
        return instance;
    }

    /**
     * Gets a new database connection.
     * 
     * @return a Connection to the database
     * @throws SQLException if a database access error occurs
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
    
}
