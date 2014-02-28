package org.opendaylight.ovsdb.lib.meta;

import com.fasterxml.jackson.databind.JsonNode;
import org.opendaylight.ovsdb.lib.notation.Condition;
import org.opendaylight.ovsdb.lib.notation.Function;

/**
 * @author ashw7n
 */

public class ColumnSchema {
    String name;
    ColumnType type;
    boolean ephemeral;
    boolean mutable;

    public ColumnSchema(String name, ColumnType columnType) {
        this.name = name;
        this.type = columnType;
    }

    public static ColumnSchema fromJson(String name, JsonNode json) {
        if (!json.isObject() || !json.has("type")) {
            //todo specific types of exception
            throw new RuntimeException("bad column schema root, expected \"type\" as child");
        }

        return new ColumnSchema(name, ColumnType.fromJson(json.get("type")));
    }

    public String getName() {
        return name;
    }

    public Condition opEqual(String some) {
        return new Condition(this.getName(), Function.EQUALS, some);
    }

    public Condition opGreaterThan(Object val) {
        return new Condition(this.getName(), Function.GREATER_THAN, val);
    }

    public Condition opLesserThan(int val) {
        return new Condition(this.getName(), Function.GREATER_THAN, val);
    }

    public Condition opLesserThanOrEquals(Object val) {
        return new Condition(this.getName(), Function.LESS_THAN_OR_EQUALS, val);
    }

    @Override
    public String toString() {
        return "ColumnType [type=" + type + ", ephemeral=" + ephemeral
                + ", mutable=" + mutable + "]";
    }
}
