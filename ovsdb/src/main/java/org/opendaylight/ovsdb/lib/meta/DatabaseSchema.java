package org.opendaylight.ovsdb.lib.meta;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author araveendrann
 */
public class DatabaseSchema {

    public static String OPEN_VSWITCH_SCHEMA_NAME = "Open_vSwitch";

    public Map<String, TableSchema> tables;

    public DatabaseSchema(Map<String, TableSchema> tables) {
        this.tables = tables;
    }


    public static DatabaseSchema fromJson(JsonNode json) {
        if (!json.isObject() || !json.has("tables")) {
            //todo specific types of exception
            throw new RuntimeException("bad databaseschema root, expected \"tables\" as child");
        }

        Map<String, TableSchema> tables = new HashMap<>();
        //Iterator<Map.Entry<String,JsonNode>> fields = json.fields();
        for(Iterator<Map.Entry<String,JsonNode>> iter = json.get("tables").fields(); iter.hasNext();) {
            Map.Entry<String, JsonNode> table = iter.next();
            System.out.println("table = " + table.getKey());
            System.out.println("table.getValue() = " + table.getValue());

            tables.put(table.getKey(), TableSchema.fromJson(table.getKey(), table.getValue()));
        }

        return new DatabaseSchema(tables);
    }

    public TableSchema table(String bridge) {
        //todo : error handling
        return tables.get(bridge);
    }
}
