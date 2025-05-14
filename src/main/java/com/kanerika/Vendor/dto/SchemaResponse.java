package com.kanerika.Vendor.dto;

import java.util.List;

public class SchemaResponse {
    private List<ColumnSpec> columnSpec;

    public SchemaResponse(List<ColumnSpec> columnSpec) {
        this.columnSpec = columnSpec;
    }

    public List<ColumnSpec> getColumnSpec() {
        return columnSpec;
    }

    public void setColumnSpec(List<ColumnSpec> columnSpec) {
        this.columnSpec = columnSpec;
    }
}
