package com.dhadiya.flow.dao;

import com.dhadiya.flow.model.WorkflowTableDataRowFieldProp;

import java.util.List;
import java.util.Map;

public interface WorkflowRowRecordsDao {
    Map<String, Object> createRow(String tableName, Map<String, Object> rowValues);
    Map<String, Object> updateRow(String tableName, Long id, Map<String, Object> rowValues);
    Map<String, Object> getRowById(String tableName, Long id);
    List<Map<String, Object>> getRows(String tableName, int limit, int offset, String sortBy, boolean ascending);
    long countRows(String tableName);
    void deleteRow(String tableName, Long id);
}
