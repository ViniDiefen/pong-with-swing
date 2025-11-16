package br.com.vinidiefen.pong.repositories.sql;

import java.util.List;
import java.util.stream.Collectors;

import br.com.vinidiefen.pong.repositories.metadata.EntityMetadata;
import br.com.vinidiefen.pong.repositories.metadata.FieldMetadata;

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

        List<String> columnDefinitions = metadata.getFieldMetadataList().stream()
                .map(DDLGenerator::generateColumnDefinition)
                .collect(Collectors.toList());

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

    /**
     * Generates DROP TABLE statement
     */
    public static <T> String generateDropTable(EntityMetadata<T> metadata) {
        return "DROP TABLE IF EXISTS " + metadata.getTableName();
    }
}
