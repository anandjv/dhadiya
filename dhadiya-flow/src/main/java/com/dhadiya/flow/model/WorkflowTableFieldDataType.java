package com.dhadiya.flow.model;

import java.sql.Types;

public enum WorkflowTableFieldDataType {
    INTEGER(Types.INTEGER),
    VARCHAR(Types.VARCHAR),
    TIMESTAMP(Types.TIMESTAMP),
    BOOLEAN(Types.BOOLEAN),
    NUMERIC(Types.NUMERIC),
    TEXT(Types.VARCHAR);

    private final int dataType;

    WorkflowTableFieldDataType(int dataType) {
        this.dataType = dataType;
    }

    public int getDataType() {
        return dataType;
    }

    public String getName() {
        return name();
    }

    public static WorkflowTableFieldDataType parse(int dataType) {
        for (WorkflowTableFieldDataType item : values()) {
            if (item.getDataType() == dataType) {
                return item;
            }
        }
        return VARCHAR;
    }
}
