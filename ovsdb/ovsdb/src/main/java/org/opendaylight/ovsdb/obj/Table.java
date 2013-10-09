package org.opendaylight.ovsdb.obj;


public class Table {

    private String name;

    protected Table(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
