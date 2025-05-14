package com.kanerika.Vendor.util;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

public class JdbcTypeMapper {
    private static final Map<Integer, String> typeMap = new HashMap<>();

    static {
        typeMap.put(Types.INTEGER, "INTEGER");
        typeMap.put(Types.VARCHAR, "VARCHAR");
        typeMap.put(Types.CHAR, "CHAR");
        typeMap.put(Types.BIGINT, "BIGINT");
        typeMap.put(Types.SMALLINT, "SMALLINT");
        typeMap.put(Types.TINYINT, "TINYINT");
        typeMap.put(Types.DECIMAL, "DECIMAL");
        typeMap.put(Types.NUMERIC, "NUMERIC");
        typeMap.put(Types.FLOAT, "FLOAT");
        typeMap.put(Types.DOUBLE, "DOUBLE");
        typeMap.put(Types.REAL, "REAL");
        typeMap.put(Types.DATE, "DATE");
        typeMap.put(Types.TIME, "TIME");
        typeMap.put(Types.TIMESTAMP, "TIMESTAMP");
        typeMap.put(Types.BIT, "BIT");
        typeMap.put(Types.BOOLEAN, "BOOLEAN");
        typeMap.put(Types.BINARY, "BINARY");
        typeMap.put(Types.VARBINARY, "VARBINARY");
        typeMap.put(Types.LONGVARBINARY, "LONGVARBINARY");
        typeMap.put(Types.LONGVARCHAR, "LONGVARCHAR");
        typeMap.put(Types.NVARCHAR, "NVARCHAR");
        typeMap.put(Types.NCHAR, "NCHAR");
        typeMap.put(Types.CLOB, "CLOB");
        typeMap.put(Types.BLOB, "BLOB");
        typeMap.put(Types.OTHER, "OTHER");
        // Add more as needed
    }

    public static String getTypeName(int jdbcTypeCode) {
        return typeMap.getOrDefault(jdbcTypeCode, "UNKNOWN(" + jdbcTypeCode + ")");
    }
}
