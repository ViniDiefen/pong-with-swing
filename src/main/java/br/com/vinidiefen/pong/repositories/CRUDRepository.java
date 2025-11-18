package br.com.vinidiefen.pong.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.vinidiefen.pong.repositories.connection.PostgresConnection;
import br.com.vinidiefen.pong.repositories.mapper.ResultSetMapper;
import br.com.vinidiefen.pong.repositories.metadata.EntityMetadata;
import br.com.vinidiefen.pong.repositories.metadata.FieldMetadata;
import br.com.vinidiefen.pong.repositories.sql.DMLGenerator;

/**
 * Generic CRUD repository
 */
public class CRUDRepository<T> {
    private final EntityMetadata<T> metadata;

    private CRUDRepository(Class<T> clazz) {
        this.metadata = new EntityMetadata<>(clazz);
    }

    /**
     * Factory method
     * Usage: CRUDRepository<PaddleModel> repo = CRUDRepository.of(PaddleModel.class);
     */
    public static <T> CRUDRepository<T> of(Class<T> clazz) {
        return new CRUDRepository<>(clazz);
    }

    public void create(T obj) {
        String sql = DMLGenerator.generateInsert(metadata);

        try (var conn = PostgresConnection.getInstance().getConnection();
             var stmt = conn.prepareStatement(sql)) {
            
            // Set parameters
            setParameters(stmt, obj, metadata.getFieldMetadataList());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create entity", e);
        }
    }

    public T read(Object id) {
        String sql = DMLGenerator.generateSelectById(metadata);

        try (var conn = PostgresConnection.getInstance().getConnection();
             var stmt = conn.prepareStatement(sql)) {
            
            // Set ID parameter
            stmt.setObject(1, id);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return ResultSetMapper.mapToEntity(rs, metadata);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read entity", e);
        }
    }

    /**
     * Updates an existing entity
     */
    public void update(T obj) {
        String sql = DMLGenerator.generateUpdate(metadata);
        
        try (var conn = PostgresConnection.getInstance().getConnection();
             var stmt = conn.prepareStatement(sql)) {
            
            // Set parameters (all fields except PK)
            List<FieldMetadata> nonPkFields = metadata.getFieldMetadataList().stream()
                    .filter(f -> !f.isPrimaryKey())
                    .toList();
            setParameters(stmt, obj, nonPkFields);
            
            // Set PK parameter at the end (for WHERE clause)
            FieldMetadata pkField = metadata.getPrimaryKeyField();
            stmt.setObject(nonPkFields.size() + 1, pkField.getValue(obj));
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update entity", e);
        }
    }

    /**
     * Deletes an entity by ID
     */
    public void delete(Object id) {
        String sql = DMLGenerator.generateDelete(metadata);
        
        try (var conn = PostgresConnection.getInstance().getConnection();
             var stmt = conn.prepareStatement(sql)) {
            
            stmt.setObject(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete entity", e);
        }
    }

    /**
     * Finds all entities
     */
    public List<T> findAll() {
        String sql = DMLGenerator.generateSelectAll(metadata);
        List<T> results = new ArrayList<>();
        
        try (var conn = PostgresConnection.getInstance().getConnection();
             var stmt = conn.createStatement();
             var rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                results.add(ResultSetMapper.mapToEntity(rs, metadata));
            }
            return results;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find all entities", e);
        }
    }

    /**
     * Helper method to set prepared statement parameters
     */
    private void setParameters(PreparedStatement stmt, T obj, List<FieldMetadata> fields) throws SQLException {
        for (int i = 0; i < fields.size(); i++) {
            FieldMetadata field = fields.get(i);
            Object value = field.getValue(obj);
            stmt.setObject(i + 1, value);
        }
    }

}
