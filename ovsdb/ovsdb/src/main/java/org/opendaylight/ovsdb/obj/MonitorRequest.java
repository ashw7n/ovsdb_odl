package org.opendaylight.ovsdb.obj;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.Lists;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MonitorRequest<E> {

    //@JsonSerialize(contentAs = ToStringSerializer.class)
    List<Column<E>> columns;

    MonitorSelect select;

    public List<? extends Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column<E>> columns) {
        this.columns = columns;
    }


    public MonitorSelect getSelect() {
        return select;
    }

    public void setSelect(MonitorSelect select) {
        this.select = select;
    }

    public MonitorRequest<E> column(Column<E> column) {
        if (null == columns) {
            columns = Lists.newArrayList();
        }
        columns.add(column);
        return this;
    }
}
