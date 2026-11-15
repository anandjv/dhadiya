package com.dhadiya.flow.dao;

import com.dhadiya.flow.model.WorkflowTableField;
import com.dhadiya.flow.model.WorkflowTableFieldDataType;
import com.dhadiya.flow.model.WorkflowTableMaster;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class WorkflowTableMasterDaoImpl implements WorkflowTableMasterDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void createTable(WorkflowTableMaster tableMaster) {
        String tableName = tableMaster.getTableName().toLowerCase().replaceAll("[^a-z0-9_]", "_");
        List<String> fieldDefs = new ArrayList<>();
        boolean hasId = false;

        if (tableMaster.getVirtualTableFieldsList() != null) {
            for (WorkflowTableField field : tableMaster.getVirtualTableFieldsList()) {
                String colName = field.getFieldName().toLowerCase().replaceAll("[^a-z0-9_]", "_");
                if ("id".equals(colName)) {
                    fieldDefs.add("id BIGSERIAL PRIMARY KEY");
                    hasId = true;
                } else {
                    String colType = mapDataType(field);
                    fieldDefs.add(colName + " " + colType);
                }
            }
        }

        if (!hasId) {
            fieldDefs.add(0, "id BIGSERIAL PRIMARY KEY");
        }

        // Standard metadata columns
        fieldDefs.add("process_tracking_no VARCHAR(100)");
        fieldDefs.add("current_stage VARCHAR(100)");
        fieldDefs.add("created_by VARCHAR(100)");
        fieldDefs.add("created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP");
        fieldDefs.add("updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP");

        String ddl = "CREATE TABLE IF NOT EXISTS " + tableName + " (" + String.join(", ", fieldDefs) + ")";
        log.info("[DYNAMIC DDL] Creating table on routed tenant database: {}", ddl);
        this.jdbcTemplate.execute(ddl);
    }

    @Override
    public void addField(String tableName, WorkflowTableField field) {
        String cleanTable = tableName.toLowerCase().replaceAll("[^a-z0-9_]", "_");
        String colName = field.getFieldName().toLowerCase().replaceAll("[^a-z0-9_]", "_");
        String colType = mapDataType(field);

        String ddl = "ALTER TABLE " + cleanTable + " ADD COLUMN IF NOT EXISTS " + colName + " " + colType;
        log.info("[DYNAMIC DDL] Adding column: {}", ddl);
        this.jdbcTemplate.execute(ddl);
    }

    @Override
    public void removeField(String tableName, String fieldName) {
        String cleanTable = tableName.toLowerCase().replaceAll("[^a-z0-9_]", "_");
        String colName = fieldName.toLowerCase().replaceAll("[^a-z0-9_]", "_");

        String ddl = "ALTER TABLE " + cleanTable + " DROP COLUMN IF EXISTS " + colName;
        log.info("[DYNAMIC DDL] Dropping column: {}", ddl);
        this.jdbcTemplate.execute(ddl);
    }

    @Override
    public void deleteTable(String tableName) {
        String cleanTable = tableName.toLowerCase().replaceAll("[^a-z0-9_]", "_");
        String ddl = "DROP TABLE IF EXISTS " + cleanTable + " CASCADE";
        log.info("[DYNAMIC DDL] Dropping table: {}", ddl);
        this.jdbcTemplate.execute(ddl);
    }

    @Override
    public void truncateTable(String tableName) {
        String cleanTable = tableName.toLowerCase().replaceAll("[^a-z0-9_]", "_");
        String ddl = "TRUNCATE " + cleanTable + " RESTART IDENTITY CASCADE";
        this.jdbcTemplate.execute(ddl);
    }

    @Override
    public boolean tableExists(String tableName) {
        String cleanTable = tableName.toLowerCase().replaceAll("[^a-z0-9_]", "_");
        String sql = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema='public' AND table_name=?";
        Integer count = this.jdbcTemplate.queryForObject(sql, Integer.class, cleanTable);
        return count != null && count > 0;
    }

    @Override
    public List<String> getColumns(String tableName) {
        String cleanTable = tableName.toLowerCase().replaceAll("[^a-z0-9_]", "_");
        String sql = "SELECT column_name FROM information_schema.columns WHERE table_schema='public' AND table_name=? ORDER BY ordinal_position";
        return this.jdbcTemplate.queryForList(sql, String.class, cleanTable);
    }

    private String mapDataType(WorkflowTableField field) {
        WorkflowTableFieldDataType type = field.getFieldDataType();
        if (type == null) {
            return "VARCHAR(255)";
        }
        return switch (type) {
            case INTEGER -> "BIGINT";
            case NUMERIC -> "NUMERIC(15,2)";
            case TIMESTAMP -> "TIMESTAMP";
            case BOOLEAN -> "BOOLEAN";
            case TEXT -> "TEXT";
            case VARCHAR -> {
                int size = (field.getSize() != null && field.getSize() > 0) ? field.getSize() : 255;
                yield "VARCHAR(" + size + ")";
            }
        };
    }
}
