package br.com.vinidiefen.pong.infrastructure.persistence.repositories.sql;

import java.util.List;
import java.util.stream.Collectors;

import br.com.vinidiefen.pong.infrastructure.persistence.repositories.metadata.EntityMetadata;
import br.com.vinidiefen.pong.infrastructure.persistence.repositories.metadata.FieldMetadata;

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

}
