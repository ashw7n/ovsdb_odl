package org.opendaylight.ovsdb.database;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class DatabaseSchema {
    @JsonProperty("name")
    public String name;
    @JsonProperty("version")
    public String version;
    @JsonProperty("cksum")
    public String cksum;
    @JsonProperty("tables")
    public Map<String, TableSchema> tables;
}
