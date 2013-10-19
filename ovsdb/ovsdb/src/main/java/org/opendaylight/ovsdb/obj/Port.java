package org.opendaylight.ovsdb.obj;

import java.math.BigInteger;

public class Port extends Table<Port> {

    public static final Name<Port> NAME = new Name<Port>("Port") {};

    String name;

    OvsDBSet<BigInteger> tag;
    OvsDBSet<BigInteger> trunks;
    OvsDBSet<UUID> interfaces;

    public Port() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public OvsDBSet<UUID> getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(OvsDBSet<UUID> interfaces) {
        this.interfaces = interfaces;
    }

    public OvsDBSet<BigInteger> getTag() {
        return tag;
    }

    public void setTag(OvsDBSet<BigInteger> tag) {
        this.tag = tag;
    }

    public OvsDBSet<BigInteger> getTrunks() {
        return trunks;
    }

    public void setTrunks(OvsDBSet<BigInteger> trunks) {
        this.trunks = trunks;
    }

    @Override
    public Name<Port> getName() {
        return NAME;
    }

    public enum Column implements org.opendaylight.ovsdb.obj.Column<Port> {
        interfaces,
        name,
        tag,
        trunks}




}
