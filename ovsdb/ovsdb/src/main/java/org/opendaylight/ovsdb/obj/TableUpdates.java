package org.opendaylight.ovsdb.obj;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Set;


public  class TableUpdates {

    Map<Table.Name, TableUpdate> map = Maps.newHashMap();

    public Set<Table.Name> availableUpdates() {
        return map.keySet();
    }

    @SuppressWarnings("unchecked")
    public <T extends Table> TableUpdate<T> getUpdate(Table.Name<T> name) {
        return map.get(name);
    }


    private <T extends Table> void put(Table.Name<T> name, TableUpdate<T> update) {
        map.put(name, update);
    }


    @JsonProperty("Interface")
    public TableUpdate<Interface> getInterfaceUpdate() {
        return getUpdate(Interface.NAME);
    }

    public void setInterfaceUpdate(TableUpdate<Interface> interfaceUpdate) {
        put(Interface.NAME, interfaceUpdate);
    }

    @JsonProperty("Bridge")
    TableUpdate<Bridge> getBridgeUpdate() {
        return getUpdate(Bridge.NAME);
    }

    public void setBridgeUpdate(TableUpdate<Bridge> bridgeUpdate) {
        put(Bridge.NAME, bridgeUpdate);
    }

    @JsonProperty("Port")
    TableUpdate<Port> getPortUpdate() {
        return getUpdate(Port.NAME);
    }

    void setPortUpdate(TableUpdate<Port> portUpdate) {
        put(Port.NAME, portUpdate);
    }
}
