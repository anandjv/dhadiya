package com.dhadiya.flow.dao;

import com.dhadiya.flow.model.WorkflowTableDataRowFieldProp;
import com.dhadiya.flow.model.WorkflowTableFieldDataType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class WorkflowRowRecordsRowMapper implements RowMapper<Map<String, WorkflowTableDataRowFieldProp>> {

    @Override
    public Map<String, WorkflowTableDataRowFieldProp> mapRow(ResultSet rs, int rowNum) throws SQLException {
        Map<String, WorkflowTableDataRowFieldProp> row = new LinkedHashMap<>();
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();

        for (int i = 1; i <= columnCount; i++) {
            String columnName = rsmd.getColumnName(i);
            String tableName = rsmd.getTableName(i);
            WorkflowTableFieldDataType dataType = WorkflowTableFieldDataType.parse(rsmd.getColumnType(i));
            Object value = rs.getObject(i);

            WorkflowTableDataRowFieldProp prop = WorkflowTableDataRowFieldProp.builder()
                    .fieldName(columnName)
                    .tableName(tableName)
                    .fieldDataType(dataType)
                    .fieldValue(value)
                    .build();

            row.put(columnName, prop);
        }
        return row;
    }
}
