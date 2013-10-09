package org.opendaylight.ovsdb.internal.json;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.TestCase;
import org.opendaylight.ovsdb.internal.jsonrpc.JsonRpc10Request;
import org.opendaylight.ovsdb.obj.*;

public class TestJsonDeser extends TestCase {

    public void test() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.ANY);
        mapper.getDeserializationContext();

        MonitorRequestBuilder monitors = new MonitorRequestBuilder();

        monitors.monitor(Tables.bridge)
                .column(Bridge.Column.controller)
                .column(Bridge.Column.fail_mode);

        monitors.monitor(Tables.port)
                .column(Port.Column.trunks);

        JsonRpc10Request request = new JsonRpc10Request("1");
        request.setMethod("monitor");
        request.setParams(monitors.params());

        System.out.println( mapper.writeValueAsString(request));

    }


}
