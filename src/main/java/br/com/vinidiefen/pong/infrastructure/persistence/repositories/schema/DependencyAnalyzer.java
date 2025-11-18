package br.com.vinidiefen.pong.infrastructure.persistence.repositories.schema;

import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Logger;

import br.com.vinidiefen.pong.infrastructure.persistence.repositories.annotations.ForeignKey;
import br.com.vinidiefen.pong.infrastructure.persistence.repositories.annotations.Table;

/**
 * Analyzes entity dependencies based on @ForeignKey annotations
 * and provides topological sorting for correct table creation/deletion order.
 */
public class DependencyAnalyzer {

    private static final Logger LOGGER = Logger.getLogger(DependencyAnalyzer.class.getName());

    /**
     * Sorts entity classes in dependency order (dependencies first).
     * Uses topological sort to ensure parent tables are created before child tables.
     * 
     * @param entityClasses the set of entity classes to sort
     * @return list of classes in correct creation order
     */
    public static List<Class<?>> sortForCreation(Set<Class<?>> entityClasses) {
        Map<String, Class<?>> tableNameToClass = buildTableNameMap(entityClasses);
        Map<Class<?>, Set<String>> dependencies = analyzeDependencies(entityClasses);
        
        return topologicalSort(entityClasses, dependencies, tableNameToClass);
    }

    /**
     * Sorts entity classes in reverse dependency order (dependents first).
     * Used for dropping tables - child tables must be dropped before parent tables.
     * 
     * @param entityClasses the set of entity classes to sort
     * @return list of classes in correct deletion order
     */
    public static List<Class<?>> sortForDeletion(Set<Class<?>> entityClasses) {
        List<Class<?>> creationOrder = sortForCreation(entityClasses);
        Collections.reverse(creationOrder);
        return creationOrder;
    }

    /**
     * Builds a map from table name to entity class.
     */
    private static Map<String, Class<?>> buildTableNameMap(Set<Class<?>> entityClasses) {
        Map<String, Class<?>> map = new HashMap<>();
        
        for (Class<?> entityClass : entityClasses) {
            Table tableAnnotation = entityClass.getAnnotation(Table.class);
            if (tableAnnotation != null) {
                map.put(tableAnnotation.name(), entityClass);
            }
        }
        
        return map;
    }

    /**
     * Analyzes dependencies for each entity class.
     * Returns a map where keys are entity classes and values are sets of table names they depend on.
     */
    private static Map<Class<?>, Set<String>> analyzeDependencies(Set<Class<?>> entityClasses) {
        Map<Class<?>, Set<String>> dependencies = new HashMap<>();
        
        for (Class<?> entityClass : entityClasses) {
            Set<String> deps = new HashSet<>();
            
            // Check all fields for @ForeignKey annotations
            for (Field field : entityClass.getDeclaredFields()) {
                ForeignKey fk = field.getAnnotation(ForeignKey.class);
                if (fk != null) {
                    deps.add(fk.table());
                }
            }
            
            dependencies.put(entityClass, deps);
            
            if (!deps.isEmpty()) {
                LOGGER.fine("Class " + entityClass.getSimpleName() + 
                    " depends on tables: " + deps);
            }
        }
        
        return dependencies;
    }

    /**
     * Performs topological sort using Kahn's algorithm.
     * Returns classes in an order where dependencies appear before dependents.
     */
    private static List<Class<?>> topologicalSort(
            Set<Class<?>> entityClasses,
            Map<Class<?>, Set<String>> dependencies,
            Map<String, Class<?>> tableNameToClass) {
        
        List<Class<?>> result = new ArrayList<>();
        Set<Class<?>> visited = new HashSet<>();
        Map<Class<?>, Integer> inDegree = new HashMap<>();
        
        // Calculate in-degree for each class
        for (Class<?> entityClass : entityClasses) {
            inDegree.put(entityClass, 0);
        }
        
        for (Class<?> entityClass : entityClasses) {
            Table tableAnnotation = entityClass.getAnnotation(Table.class);
            String tableName = tableAnnotation.name();
            
            // For each class that depends on this table, increment its in-degree
            for (Map.Entry<Class<?>, Set<String>> entry : dependencies.entrySet()) {
                if (entry.getValue().contains(tableName)) {
                    inDegree.put(entry.getKey(), inDegree.get(entry.getKey()) + 1);
                }
            }
        }
        
        // Start with classes that have no dependencies
        Queue<Class<?>> queue = new LinkedList<>();
        for (Map.Entry<Class<?>, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.offer(entry.getKey());
            }
        }
        
        // Process queue
        while (!queue.isEmpty()) {
            Class<?> current = queue.poll();
            result.add(current);
            visited.add(current);
            
            Table tableAnnotation = current.getAnnotation(Table.class);
            String tableName = tableAnnotation.name();
            
            // Reduce in-degree for classes that depend on this table
            for (Map.Entry<Class<?>, Set<String>> entry : dependencies.entrySet()) {
                Class<?> dependent = entry.getKey();
                if (!visited.contains(dependent) && entry.getValue().contains(tableName)) {
                    int newDegree = inDegree.get(dependent) - 1;
                    inDegree.put(dependent, newDegree);
                    
                    if (newDegree == 0) {
                        queue.offer(dependent);
                    }
                }
            }
        }
        
        // Check for circular dependencies
        if (result.size() != entityClasses.size()) {
            LOGGER.warning("Circular dependency detected! Some tables may not be created in correct order.");
            // Add remaining classes
            for (Class<?> entityClass : entityClasses) {
                if (!visited.contains(entityClass)) {
                    result.add(entityClass);
                }
            }
        }
        
        return result;
    }
}
