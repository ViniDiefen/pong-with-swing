package br.com.vinidiefen.pong.repositories.sql;

import java.util.List;
import java.util.stream.Collectors;

import br.com.vinidiefen.pong.repositories.metadata.EntityMetadata;
import br.com.vinidiefen.pong.repositories.metadata.FieldMetadata;

/**
 * Generates DML (Data Manipulation Language) statements
 * Similar to Hibernate's SQL generation
 */
public class DMLGenerator {

    /**
     * Generates INSERT statement
     */
    public static <T> String generateInsert(EntityMetadata<T> metadata) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ")
           .append(metadata.getTableName())
           .append(" (");

        List<String> columnNames = metadata.getColumnNames();
        sql.append(String.join(", ", columnNames));
        sql.append(") VALUES (");

        String placeholders = columnNames.stream()
                .map(name -> "?")
                .collect(Collectors.joining(", "));
        sql.append(placeholders);
        sql.append(")");

        return sql.toString();
    }

    /**
     * Generates SELECT by ID statement
     */
    public static <T> String generateSelectById(EntityMetadata<T> metadata) {
        FieldMetadata pkField = metadata.getPrimaryKeyField();
        
        return "SELECT * FROM " + metadata.getTableName() + 
               " WHERE " + pkField.getColumnName() + " = ?";
    }

    /**
     * Generates SELECT ALL statement
     */
    public static <T> String generateSelectAll(EntityMetadata<T> metadata) {
        return "SELECT * FROM " + metadata.getTableName();
    }

    /**
     * Generates UPDATE statement
     */
    public static <T> String generateUpdate(EntityMetadata<T> metadata) {
        FieldMetadata pkField = metadata.getPrimaryKeyField();
        
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE ")
           .append(metadata.getTableName())
           .append(" SET ");

        List<String> setStatements = metadata.getFieldMetadataList().stream()
                .filter(field -> !field.isPrimaryKey())
                .map(field -> field.getColumnName() + " = ?")
                .collect(Collectors.toList());

        sql.append(String.join(", ", setStatements));
        sql.append(" WHERE ").append(pkField.getColumnName()).append(" = ?");

        return sql.toString();
    }

    /**
     * Generates DELETE statement
     */
    public static <T> String generateDelete(EntityMetadata<T> metadata) {
        FieldMetadata pkField = metadata.getPrimaryKeyField();
        
        return "DELETE FROM " + metadata.getTableName() + 
               " WHERE " + pkField.getColumnName() + " = ?";
    }
}
