package br.com.vinidiefen.pong.infrastructure.persistence.repositories.schema;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;

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

    private final String modelPackage;

    /**
     * Main method for standalone schema creation.
     */
    public static void main(String[] args) {
        SchemaManager manager = new SchemaManager();
        manager.recreateAll();
    }

    /**
     * Creates a SchemaManager for the default model package.
     */
    public SchemaManager() {
        this("br.com.vinidiefen.pong.infrastructure.persistence.models");
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
     * Scans for all entity classes and creates their tables.
     * Tables are created in dependency order (parent tables before child tables).
     */
    public void createAll() {
        Set<Class<?>> entityClasses = scanForEntityClasses();
        java.util.List<Class<?>> sortedClasses = DependencyAnalyzer.sortForCreation(entityClasses);

        for (Class<?> entityClass : sortedClasses) {
            createTable(entityClass);
        }
    }

    /**
     * Scans for all entity classes and drops their tables.
     * Tables are dropped in reverse dependency order (child tables before parent
     * tables).
     */
    public void dropAll() {
        Set<Class<?>> entityClasses = scanForEntityClasses();
        java.util.List<Class<?>> sortedClasses = DependencyAnalyzer.sortForDeletion(entityClasses);

        for (Class<?> entityClass : sortedClasses) {
            dropTable(entityClass);
        }
    }

    /**
     * Recreates all tables (drops and creates).
     */
    public void recreateAll() {
        dropAll();
        createAll();
    }

    /**
     * Creates a table for the given entity class.
     * 
     * @param entityClass the entity class
     */
    public void createTable(Class<?> entityClass) {
        EntityMetadata<?> metadata = new EntityMetadata<>(entityClass);
        String sql = DDLGenerator.generateCreateTable(metadata);

        System.out.println("Creating table: " + metadata.getTableName());
        executeSQL(sql);
    }

    /**
     * Drops a table for the given entity class.
     * 
     * @param entityClass the entity class
     */
    public void dropTable(Class<?> entityClass) {
        EntityMetadata<?> metadata = new EntityMetadata<>(entityClass);
        String sql = DDLGenerator.generateDropTable(metadata);

        System.out.println("Dropping table: " + metadata.getTableName());
        executeSQL(sql);
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
     */
    private void executeSQL(String sql) {
        try (Connection conn = PostgresConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("SQL execution error: " + e.getMessage());
        }
    }
}
