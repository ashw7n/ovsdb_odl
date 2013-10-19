package org.opendaylight.ovsdb.obj;

public class Interface extends Table<Interface> {

    public static Name<Interface> NAME = new Name<Interface>("Interface") {};

    String name;
    OvsDBMap<String, Object> options;
    String type;

    public void setName(String name) {
        this.name = name;
    }

    public OvsDBMap<String, Object> getOptions() {
        return options;
    }

    public void setOptions(OvsDBMap<String, Object> options) {
        this.options = options;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public Name<Interface> getName() {
        return NAME;
    }
}
