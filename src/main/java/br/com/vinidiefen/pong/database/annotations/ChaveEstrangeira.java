package br.com.vinidiefen.pong.database.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark a field as a foreign key reference.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ChaveEstrangeira {
    
    /**
     * The name of the referenced table.
     */
    String table();
    
    /**
     * The name of the referenced column.
     */
    String column();

}
