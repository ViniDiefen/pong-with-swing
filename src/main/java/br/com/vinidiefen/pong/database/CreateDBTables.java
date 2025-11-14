package br.com.vinidiefen.pong.database;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.reflections.Reflections;

import br.com.vinidiefen.pong.database.annotations.Column;
import br.com.vinidiefen.pong.database.annotations.Table;

/**
 * Utility class for creating database tables from annotated model classes.
 */
public class CreateDBTables {

    private static final Logger LOGGER = Logger.getLogger(CreateDBTables.class.getName());
    private static final String MODEL_PACKAGE = "br.com.vinidiefen.pong.models";

    public static void main(String[] args) {
        LOGGER.info("Starting database table creation...");
        CreateDBTables creator = new CreateDBTables();
        creator.execute();
        LOGGER.info("Database table creation completed.");
    }

    /**
     * Scans for model classes and creates corresponding database tables.
     */
    public void execute() {
        Set<Class<?>> tableClasses = scanForTableClasses();
        LOGGER.info("Found " + tableClasses.size() + " table(s) to create");

        for (Class<?> tableClass : tableClasses) {
            createTable(tableClass);
        }
    }

    /**
     * Scans the model package for classes annotated with @Table.
     * 
     * @return set of classes annotated with @Table
     */
    private Set<Class<?>> scanForTableClasses() {
        Reflections reflections = new Reflections(MODEL_PACKAGE);
        return reflections.getTypesAnnotatedWith(Table.class);
    }

    /**
     * Creates a database table for the given class.
     * 
     * @param tableClass the class annotated with @Table
     */
    private void createTable(Class<?> tableClass) {
        Table tableAnnotation = tableClass.getAnnotation(Table.class);
        String tableName = tableAnnotation.name();

        try {
            String sql = buildCreateTableQuery(tableClass);
            LOGGER.info("Creating table: " + tableName);
            LOGGER.fine("SQL: " + sql);

            executeQuery(sql);
            LOGGER.info("Successfully created table: " + tableName);

        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.SEVERE, "Invalid table configuration for " + tableName + ": " + e.getMessage(), e);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error creating table " + tableName + ": " + e.getMessage(), e);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error creating table " + tableName + ": " + e.getMessage(), e);
        }
    }

    /**
     * Builds a CREATE TABLE IF NOT EXISTS SQL query for the given class.
     * 
     * @param tableClass the class annotated with @Table
     * @return the SQL query string
     * @throws IllegalArgumentException if the class is not annotated with @Table
     */
    private String buildCreateTableQuery(Class<?> tableClass) {
        if (!tableClass.isAnnotationPresent(Table.class)) {
            throw new IllegalArgumentException(
                    "Class " + tableClass.getName() + " is not annotated with @Table");
        }

        Table tableAnnotation = tableClass.getAnnotation(Table.class);
        String tableName = tableAnnotation.name();

        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE IF NOT EXISTS ").append(tableName).append(" (");

        List<String> columnDefinitions = collectColumnDefinitions(tableClass);
        sql.append(String.join(", ", columnDefinitions));

        sql.append(")");
        return sql.toString();
    }

    /**
     * Recursively collects column definitions from a class and its superclasses.
     * 
     * @param clazz the class to process
     * @return list of SQL column definitions
     */
    private List<String> collectColumnDefinitions(Class<?> clazz) {
        List<String> columns = new ArrayList<>();

        // Process superclass first (so parent columns come before child columns)
        Class<?> superClass = clazz.getSuperclass();
        if (superClass != null && !superClass.equals(Object.class)) {
            columns.addAll(collectColumnDefinitions(superClass));
        }

        // Process current class fields
        List<String> currentClassColumns = Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Column.class))
                .map(this::buildColumnDefinition)
                .toList();

        columns.addAll(currentClassColumns);
        return columns;
    }

    /**
     * Builds a SQL column definition from a field annotated with @Column.
     * 
     * @param field the field to process
     * @return the SQL column definition string
     */
    private String buildColumnDefinition(Field field) {
        Column column = field.getAnnotation(Column.class);
        StringBuilder definition = new StringBuilder();

        definition.append(column.name()).append(" ").append(column.type());

        if (column.primaryKey()) definition.append(" PRIMARY KEY");
        if (column.notNull() && !column.primaryKey()) definition.append(" NOT NULL");
        if (column.unique() && !column.primaryKey()) definition.append(" UNIQUE");
        if (!column.defaultValue().isEmpty()) definition.append(" DEFAULT ").append(column.defaultValue());

        return definition.toString();
    }

    /**
     * Executes a SQL query.
     * 
     * @param sql the SQL query to execute
     * @throws SQLException if a database error occurs
     */
    private void executeQuery(String sql) throws SQLException {
        try (Connection conn = PostgresConnection.def().getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }
}
