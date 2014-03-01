package org.opendaylight.ovsdb.lib.meta;

import com.fasterxml.jackson.databind.JsonNode;
import org.opendaylight.ovsdb.OpenVswitch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author araveendrann
 */
public class TableSchema<E extends TableSchema<E>> {
    protected static final Logger logger = LoggerFactory.getLogger(TableSchema.class);
    private Map<String, ColumnSchema> columns;

    public TableSchema() {
    }

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

       TableSchema tableSchema = new TableSchema(columns);
       return tableSchema;
    }

    public <E extends TableSchema<E>> E as(Class<E> clazz) {
        try {
            Constructor<E> e = clazz.getConstructor(TableSchema.class);
            return e.newInstance(this);
        } catch (Exception e) {
            throw new RuntimeException("exception constructing instance of clazz " + clazz, e);
        }
    }

    public OpenVswitch.Insert<E> insert() {
        return new OpenVswitch.Insert<>(this);
    }



    public <D> ColumnSchema<E, D> column(String column) {
        //todo exception handling
        return columns.get(column);
    }
}
