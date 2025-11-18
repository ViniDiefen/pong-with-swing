package br.com.vinidiefen.pong.infrastructure.persistence.repositories.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import br.com.vinidiefen.pong.infrastructure.persistence.repositories.metadata.EntityMetadata;
import br.com.vinidiefen.pong.infrastructure.persistence.repositories.metadata.FieldMetadata;

/**
 * Maps ResultSet to entity objects
 * Similar to Hibernate's ResultTransformer
 */
public class ResultSetMapper {

    /**
     * Maps a ResultSet row to an entity object
     */
    public static <T> T mapToEntity(ResultSet rs, EntityMetadata<T> metadata) throws SQLException {
        try {
            // Create new instance of the entity
            T entity = metadata.getEntityClass().getDeclaredConstructor().newInstance();

            // Map each column to the corresponding field
            for (FieldMetadata fieldMetadata : metadata.getFieldMetadataList()) {
                Object value = getValueFromResultSet(rs, fieldMetadata);
                fieldMetadata.setValue(entity, value);
            }

            return entity;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Failed to map ResultSet to entity " + metadata.getEntityClass().getName(), e);
        }
    }

    private static Object getValueFromResultSet(ResultSet rs, FieldMetadata fieldMetadata) throws SQLException {
        String columnName = fieldMetadata.getColumnName();
        Class<?> fieldType = fieldMetadata.getField().getType();

        // Handle different types
        if (fieldType == int.class || fieldType == Integer.class) {
            return rs.getInt(columnName);
        } else if (fieldType == long.class || fieldType == Long.class) {
            return rs.getLong(columnName);
        } else if (fieldType == double.class || fieldType == Double.class) {
            return rs.getDouble(columnName);
        } else if (fieldType == float.class || fieldType == Float.class) {
            return rs.getFloat(columnName);
        } else if (fieldType == boolean.class || fieldType == Boolean.class) {
            return rs.getBoolean(columnName);
        } else if (fieldType == String.class) {
            return rs.getString(columnName);
        } else if (fieldType == UUID.class) {
            Object obj = rs.getObject(columnName);
            if (obj instanceof UUID) {
                return obj;
            } else if (obj instanceof String) {
                return UUID.fromString((String) obj);
            }
            return obj;
        } else {
            return rs.getObject(columnName);
        }
    }
}
