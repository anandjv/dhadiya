package com.dhadiya.flow.dao;

import com.dhadiya.flow.model.WorkflowTableField;
import com.dhadiya.flow.model.WorkflowTableMaster;

import java.util.List;

public interface WorkflowTableMasterDao {
    void createTable(WorkflowTableMaster tableMaster);
    void addField(String tableName, WorkflowTableField field);
    void removeField(String tableName, String fieldName);
    void deleteTable(String tableName);
    void truncateTable(String tableName);
    boolean tableExists(String tableName);
    List<String> getColumns(String tableName);
}
