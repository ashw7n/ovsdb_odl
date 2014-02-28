package org.opendaylight.ovsdb.lib.meta;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author araveendrann
 */
public class TableSchema {
    protected static final Logger logger = LoggerFactory.getLogger(TableSchema.class);
    private Map<String, ColumnSchema> columns;

    public TableSchema(Map<String, ColumnSchema> columns) {
        this.columns = columns;
    }

    public static TableSchema fromJson(String tableName, JsonNode json) {

        if (!json.isObject() || !json.has("columns")) {
            //todo specific types of exception
            throw new RuntimeException("bad tableschema root, expected \"columns\" as child");
        }

        Map<String, ColumnSchema> columns = new HashMap<>();
        for (Iterator<Map.Entry<String, JsonNode>> iter = json.get("columns").fields(); iter.hasNext(); ) {
            Map.Entry<String, JsonNode> column = iter.next();
            logger.debug("%s:%s", tableName, column.getKey());
            columns.put(column.getKey(), ColumnSchema.fromJson(column.getKey(), column.getValue()));
        }

        return new TableSchema(columns);
    }

    public ColumnSchema column(String column) {
        //todo exception handling
        return columns.get(column);
    }
}
