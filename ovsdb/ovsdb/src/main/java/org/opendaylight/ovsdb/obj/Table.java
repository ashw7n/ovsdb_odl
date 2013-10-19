package org.opendaylight.ovsdb.obj;

public abstract class Table<E extends Table> {

    public abstract Name<E> getName();

    public static abstract class Name<E extends Table> {
        String name;

        protected Name(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "Table:" + name;
        }
    }
}
