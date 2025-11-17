package br.com.vinidiefen.pong.repositories.metadata;

import java.lang.reflect.Field;
import java.util.Optional;

import br.com.vinidiefen.pong.repositories.annotations.Column;
import br.com.vinidiefen.pong.repositories.annotations.ForeignKey;

/**
 * Stores metadata about a single field (similar to Hibernate's PropertyAccessor)
 */
public class FieldMetadata {
    private final Field field;
    private final Column column;
    private final ForeignKey foreignKey;

    public FieldMetadata(Field field, Column column, ForeignKey foreignKey) {
        this.field = field;
        this.column = column;
        this.foreignKey = foreignKey;
        // Make field accessible for reflection
        this.field.setAccessible(true);
    }

    public Field getField() {
        return field;
    }

    public Column getColumn() {
        return column;
    }

    public ForeignKey getForeignKey() {
        return foreignKey;
    }

    public String getColumnName() {
        return column.name();
    }

    public String getColumnType() {
        return column.type();
    }

    public boolean isPrimaryKey() {
        return column.primaryKey();
    }

    public boolean isNotNull() {
        return column.notNull();
    }

    public boolean isUnique() {
        return column.unique();
    }

    public String getDefaultValue() {
        return column.defaultValue();
    }

    public boolean hasForeignKey() {
        return foreignKey != null && !foreignKey.table().isEmpty() && !foreignKey.column().isEmpty();
    }

    /**
     * Gets the value of this field from the given object
     */
    public Object getValue(Object obj) {
        try {
            return field.get(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to get value from field " + field.getName(), e);
        }
    }

    /**
     * Sets the value of this field in the given object
     */
    public void setValue(Object obj, Object value) {
        try {
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to set value to field " + field.getName(), e);
        }
    }
}
