package org.opendaylight.ovsdb.obj;


public class Bridge extends Table {

    Bridge() {
        super("Bridge");
    }

    public enum Column implements org.opendaylight.ovsdb.obj.Column<Bridge>{ controller, fail_mode, name, ports}

    public static final Bridge Table = new Bridge();
}
