package org.opendaylight.ovsdb.obj;

public class Bridge extends Table<Bridge> {

    public static final Name<Bridge> NAME = new Name<Bridge>("Bridge"){};

    OvsDBSet controller;
    OvsDBSet<String> fail_mode;
    String name;
    OvsDBSet<UUID> ports;

    public Bridge() {
    }

    @Override
    public Name<Bridge> getName() {
        return NAME;
    }

    public OvsDBSet getController() {
        return controller;
    }

    public void setController(OvsDBSet controller) {
        this.controller = controller;
    }

    public OvsDBSet<String> getFail_mode() {
        return fail_mode;
    }

    public void setFail_mode(OvsDBSet<String> fail_mode) {
        this.fail_mode = fail_mode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OvsDBSet<UUID> getPorts() {
        return ports;
    }

    public void setPorts(OvsDBSet<UUID> ports) {
        this.ports = ports;
    }


    public enum Column implements org.opendaylight.ovsdb.obj.Column<Bridge>{ controller, fail_mode, name, ports}
}
