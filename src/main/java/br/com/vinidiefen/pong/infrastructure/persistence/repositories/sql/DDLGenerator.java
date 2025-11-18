package br.com.vinidiefen.pong.infrastructure.persistence.repositories.sql;

import java.util.ArrayList;
import java.util.List;

import br.com.vinidiefen.pong.infrastructure.persistence.repositories.metadata.EntityMetadata;
import br.com.vinidiefen.pong.infrastructure.persistence.repositories.metadata.FieldMetadata;

/**
 * Generates DDL (Data Definition Language) statements
 * Similar to Hibernate's SchemaExport
 */
public class DDLGenerator {

    /**
     * Generates CREATE TABLE statement
     */
    public static <T> String generateCreateTable(EntityMetadata<T> metadata) {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE IF NOT EXISTS ")
           .append(metadata.getTableName())
           .append(" (");

        // Collect column definitions and foreign key constraints
        List<String> columnDefinitions = new ArrayList<>();
        metadata.getFieldMetadataList().stream()
                .map(DDLGenerator::generateColumnDefinition)
                .forEach(columnDefinitions::add);
        metadata.getFieldMetadataList().stream()
                .filter(FieldMetadata::hasForeignKey)
                .map(DDLGenerator::generateForeignKeyConstraint)
                .forEach(columnDefinitions::add);

        sql.append(String.join(", ", columnDefinitions));
        sql.append(")");

        return sql.toString();
    }

    private static String generateColumnDefinition(FieldMetadata fieldMetadata) {
        StringBuilder definition = new StringBuilder();
        definition.append(fieldMetadata.getColumnName())
                  .append(" ")
                  .append(fieldMetadata.getColumnType());

        if (fieldMetadata.isPrimaryKey()) {
            definition.append(" PRIMARY KEY");
        }
        if (fieldMetadata.isNotNull() && !fieldMetadata.isPrimaryKey()) {
            definition.append(" NOT NULL");
        }
        if (fieldMetadata.isUnique() && !fieldMetadata.isPrimaryKey()) {
            definition.append(" UNIQUE");
        }
        if (!fieldMetadata.getDefaultValue().isEmpty()) {
            definition.append(" DEFAULT ").append(fieldMetadata.getDefaultValue());
        }

        return definition.toString();
    }

    private static String generateForeignKeyConstraint(FieldMetadata fieldMetadata) {
        StringBuilder constraint = new StringBuilder();
        constraint.append("FOREIGN KEY (")
                  .append(fieldMetadata.getColumnName())
                  .append(") REFERENCES ")
                  .append(fieldMetadata.getForeignKey().table())
                  .append("(")
                  .append(fieldMetadata.getForeignKey().column())
                  .append(")");
        return constraint.toString();
    }

    /**
     * Generates DROP TABLE statement
     */
    public static <T> String generateDropTable(EntityMetadata<T> metadata) {
        return "DROP TABLE IF EXISTS " + metadata.getTableName();
    }
}
