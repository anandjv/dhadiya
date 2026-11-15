package com.dhadiya.flow.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;

@Slf4j
@Repository
public class WorkflowRowRecordsDaoImpl implements WorkflowRowRecordsDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Map<String, Object> createRow(String tableName, Map<String, Object> rowValues) {
        String cleanTable = tableName.toLowerCase().replaceAll("[^a-z0-9_]", "_");
        List<String> columns = new ArrayList<>();
        List<String> placeholders = new ArrayList<>();
        List<Object> values = new ArrayList<>();

        for (Map.Entry<String, Object> entry : rowValues.entrySet()) {
            String colName = entry.getKey().toLowerCase().replaceAll("[^a-z0-9_]", "_");
            if ("id".equals(colName)) continue; // Auto-generated ID
            columns.add(colName);
            placeholders.add("?");
            values.add(entry.getValue());
        }

        String sql = "INSERT INTO " + cleanTable + " (" + String.join(", ", columns) + ") " +
                "VALUES (" + String.join(", ", placeholders) + ") RETURNING *";

        log.info("[DYNAMIC CRUD] Executing INSERT RETURNING *: {}", sql);
        return jdbcTemplate.queryForMap(sql, values.toArray());
    }

    @Override
    public Map<String, Object> updateRow(String tableName, Long id, Map<String, Object> rowValues) {
        String cleanTable = tableName.toLowerCase().replaceAll("[^a-z0-9_]", "_");
        List<String> setClauses = new ArrayList<>();
        List<Object> values = new ArrayList<>();

        for (Map.Entry<String, Object> entry : rowValues.entrySet()) {
            String colName = entry.getKey().toLowerCase().replaceAll("[^a-z0-9_]", "_");
            if ("id".equals(colName)) continue;
            setClauses.add(colName + " = ?");
            values.add(entry.getValue());
        }

        values.add(id);
        String sql = "UPDATE " + cleanTable + " SET " + String.join(", ", setClauses) +
                ", updated_at = CURRENT_TIMESTAMP WHERE id = ? RETURNING *";

        log.info("[DYNAMIC CRUD] Executing UPDATE RETURNING *: {}", sql);
        return jdbcTemplate.queryForMap(sql, values.toArray());
    }

    @Override
    public Map<String, Object> getRowById(String tableName, Long id) {
        String cleanTable = tableName.toLowerCase().replaceAll("[^a-z0-9_]", "_");
        String sql = "SELECT * FROM " + cleanTable + " WHERE id = ?";
        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, id);
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public List<Map<String, Object>> getRows(String tableName, int limit, int offset, String sortBy, boolean ascending) {
        String cleanTable = tableName.toLowerCase().replaceAll("[^a-z0-9_]", "_");
        String cleanSort = (sortBy != null && !sortBy.isBlank())
                ? sortBy.toLowerCase().replaceAll("[^a-z0-9_]", "_")
                : "id";
        String direction = ascending ? "ASC" : "DESC";

        String sql = "SELECT * FROM " + cleanTable + " ORDER BY " + cleanSort + " " + direction +
                " LIMIT ? OFFSET ?";
        return jdbcTemplate.queryForList(sql, limit, offset);
    }

    @Override
    public long countRows(String tableName) {
        String cleanTable = tableName.toLowerCase().replaceAll("[^a-z0-9_]", "_");
        String sql = "SELECT COUNT(*) FROM " + cleanTable;
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count != null ? count : 0L;
    }

    @Override
    public void deleteRow(String tableName, Long id) {
        String cleanTable = tableName.toLowerCase().replaceAll("[^a-z0-9_]", "_");
        String sql = "DELETE FROM " + cleanTable + " WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
