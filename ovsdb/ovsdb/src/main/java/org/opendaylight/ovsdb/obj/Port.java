package org.opendaylight.ovsdb.obj;

public class Port extends Table {
    public enum Column implements org.opendaylight.ovsdb.obj.Column<Port> {
        interfaces,
        name,
        tag,
        trunks}

    Port() {
        super("Port");
    }


}
