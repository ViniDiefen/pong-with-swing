package br.com.vinidiefen.pong.infrastructure.persistence.repositories.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark a field as a database column.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
    
    /**
     * The name of the database column.
     */
    String name();
    
    /**
     * The SQL data type of the column (e.g., "INTEGER", "VARCHAR(255)", "UUID").
     */
    String type();
    
    /**
     * Whether this column is a primary key.
     */
    boolean primaryKey() default false;
    
    /**
     * Whether this column has a NOT NULL constraint.
     */
    boolean notNull() default false;
    
    /**
     * Whether this column has a UNIQUE constraint.
     */
    boolean unique() default false;
    
    /**
     * The default value for this column (empty string means no default).
     */
    String defaultValue() default "";

}
