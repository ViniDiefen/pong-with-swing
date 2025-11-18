package br.com.vinidiefen.pong.infrastructure.persistence.repositories.metadata;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.vinidiefen.pong.infrastructure.persistence.repositories.annotations.Column;
import br.com.vinidiefen.pong.infrastructure.persistence.repositories.annotations.ForeignKey;
import br.com.vinidiefen.pong.infrastructure.persistence.repositories.annotations.Table;

/**
 * Stores metadata about an entity class (similar to Hibernate's EntityPersister)
 */
public class EntityMetadata<T> {
    private final Class<T> entityClass;
    private final Table table;
    private final List<FieldMetadata> fieldMetadataList;

    public EntityMetadata(Class<T> entityClass) {
        this.entityClass = entityClass;
        this.table = extractTableAnnotation(entityClass);
        this.fieldMetadataList = extractFieldMetadata(entityClass);
    }

    private Table extractTableAnnotation(Class<T> clazz) {
        Table tableAnnotation = clazz.getAnnotation(Table.class);
        if (tableAnnotation == null) {
            throw new IllegalArgumentException("Class " + clazz.getName() + " must be annotated with @Table");
        }
        return tableAnnotation;
    }

    private List<FieldMetadata> extractFieldMetadata(Class<?> clazz) {
        List<FieldMetadata> metadata = new ArrayList<>();
        List<Field> fields = getAllFields(clazz, new ArrayList<>());
        
        for (Field field : fields) {
            Column column = field.getAnnotation(Column.class);
            ForeignKey foreignKey = field.getAnnotation(ForeignKey.class);
            if (column != null) {
                metadata.add(new FieldMetadata(field, column, foreignKey));
            }
        }
        
        return metadata;
    }

    private List<Field> getAllFields(Class<?> clazz, List<Field> list) {
        // Go up the class hierarchy to get inherited fields
        if (clazz.getSuperclass() != null) {
            getAllFields(clazz.getSuperclass(), list);
        }
        // Get fields declared in the current class
        Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Column.class))
                .forEach(list::add);
        return list;
    }

    public Class<T> getEntityClass() {
        return entityClass;
    }

    public String getTableName() {
        return table.name();
    }

    public Table getTable() {
        return table;
    }

    public List<FieldMetadata> getFieldMetadataList() {
        return fieldMetadataList;
    }

    public FieldMetadata getPrimaryKeyField() {
        return fieldMetadataList.stream()
                .filter(FieldMetadata::isPrimaryKey)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No primary key found for entity " + entityClass.getName()));
    }

    public List<String> getColumnNames() {
        return fieldMetadataList.stream()
                .map(FieldMetadata::getColumnName)
                .toList();
    }
}
