package org.opendaylight.ovsdb.obj;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.opendaylight.ovsdb.internal.jsonrpc.Params;

import java.util.List;
import java.util.Map;

public class MonitorRequestBuilder implements Params {

    Map<String, MonitorRequest> requests = Maps.newLinkedHashMap();


    public <T extends Table> MonitorRequest<T> monitor(T table) {
        MonitorRequest<T> req = new MonitorRequest<T>();
        requests.put(table.getName(), req);
        return req;
    }

    public List<Object> params() {
        return Lists.newArrayList("Open_vSwitch", null, requests);
    }

}
