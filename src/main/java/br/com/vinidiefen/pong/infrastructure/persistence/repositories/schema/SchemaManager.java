package br.com.vinidiefen.pong.infrastructure.persistence.repositories.schema;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.reflections.Reflections;

import br.com.vinidiefen.pong.infrastructure.persistence.repositories.annotations.Table;
import br.com.vinidiefen.pong.infrastructure.persistence.repositories.connection.PostgresConnection;
import br.com.vinidiefen.pong.infrastructure.persistence.repositories.metadata.EntityMetadata;
import br.com.vinidiefen.pong.infrastructure.persistence.repositories.sql.DDLGenerator;

/**
 * Manages database schema operations.
 * Scans for entity classes and creates/drops tables automatically.
 * 
 * This replaces the old CreateDBTables class with a cleaner architecture
 * that reuses the metadata and SQL generation infrastructure.
 */
public class SchemaManager {

    private static final Logger LOGGER = Logger.getLogger(SchemaManager.class.getName());
    private final String modelPackage;

    /**
     * Creates a SchemaManager for the default model package.
     */
    public SchemaManager() {
        this("br.com.vinidiefen.pong.models");
    }

    /**
     * Creates a SchemaManager for a specific package.
     * 
     * @param modelPackage the package to scan for entity classes
     */
    public SchemaManager(String modelPackage) {
        this.modelPackage = modelPackage;
    }

    /**
     * Main method for standalone schema creation.
     */
    public static void main(String[] args) {
        LOGGER.info("Starting database schema creation...");
        SchemaManager manager = new SchemaManager();
        manager.recreateAll();
        LOGGER.info("Database schema creation completed.");
    }

    /**
     * Scans for all entity classes and creates their tables.
     * Tables are created in dependency order (parent tables before child tables).
     */
    public void createAll() {
        Set<Class<?>> entityClasses = scanForEntityClasses();
        LOGGER.info("Found " + entityClasses.size() + " entity class(es) to create");

        // Sort by dependencies to ensure parent tables are created first
        java.util.List<Class<?>> sortedClasses = DependencyAnalyzer.sortForCreation(entityClasses);
        LOGGER.info("Tables will be created in dependency order");

        for (Class<?> entityClass : sortedClasses) {
            createTable(entityClass);
        }
    }

    /**
     * Scans for all entity classes and drops their tables.
     * Tables are dropped in reverse dependency order (child tables before parent tables).
     */
    public void dropAll() {
        Set<Class<?>> entityClasses = scanForEntityClasses();
        LOGGER.info("Found " + entityClasses.size() + " entity class(es) to drop");

        // Sort in reverse dependency order to ensure child tables are dropped first
        java.util.List<Class<?>> sortedClasses = DependencyAnalyzer.sortForDeletion(entityClasses);
        LOGGER.info("Tables will be dropped in reverse dependency order");

        for (Class<?> entityClass : sortedClasses) {
            dropTable(entityClass);
        }
    }

    /**
     * Recreates all tables (drops and creates).
     */
    public void recreateAll() {
        LOGGER.info("Recreating all tables...");
        dropAll();
        createAll();
        LOGGER.info("All tables recreated successfully.");
    }

    /**
     * Creates a table for the given entity class.
     * 
     * @param entityClass the entity class
     */
    public void createTable(Class<?> entityClass) {
        try {
            EntityMetadata<?> metadata = new EntityMetadata<>(entityClass);
            String sql = DDLGenerator.generateCreateTable(metadata);
            
            LOGGER.info("Creating table: " + metadata.getTableName());
            System.out.println("SQL: " + sql);

            executeSQL(sql);
            LOGGER.info("Successfully created table: " + metadata.getTableName());

        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.SEVERE, 
                "Invalid entity configuration for " + entityClass.getSimpleName() + ": " + e.getMessage(), e);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, 
                "Database error creating table for " + entityClass.getSimpleName() + ": " + e.getMessage(), e);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, 
                "Unexpected error creating table for " + entityClass.getSimpleName() + ": " + e.getMessage(), e);
        }
    }

    /**
     * Drops a table for the given entity class.
     * 
     * @param entityClass the entity class
     */
    public void dropTable(Class<?> entityClass) {
        try {
            EntityMetadata<?> metadata = new EntityMetadata<>(entityClass);
            String sql = DDLGenerator.generateDropTable(metadata);
            
            LOGGER.info("Dropping table: " + metadata.getTableName());
            LOGGER.fine("SQL: " + sql);

            executeSQL(sql);
            LOGGER.info("Successfully dropped table: " + metadata.getTableName());

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, 
                "Error dropping table for " + entityClass.getSimpleName() + ": " + e.getMessage(), e);
        }
    }

    /**
     * Scans the model package for classes annotated with @Table.
     * 
     * @return set of entity classes
     */
    private Set<Class<?>> scanForEntityClasses() {
        Reflections reflections = new Reflections(modelPackage);
        return reflections.getTypesAnnotatedWith(Table.class);
    }

    /**
     * Executes a SQL statement.
     * 
     * @param sql the SQL statement to execute
     * @throws SQLException if a database error occurs
     */
    private void executeSQL(String sql) throws SQLException {
        try (Connection conn = PostgresConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }
}
