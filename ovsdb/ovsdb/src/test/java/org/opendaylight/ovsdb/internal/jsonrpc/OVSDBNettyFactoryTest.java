package org.opendaylight.ovsdb.internal.jsonrpc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ListenableFuture;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;
import junit.framework.TestCase;
import org.opendaylight.controller.sal.connection.ConnectionConstants;
import org.opendaylight.controller.sal.core.Node;
import org.opendaylight.ovsdb.database.DatabaseSchema;
import org.opendaylight.ovsdb.internal.ConnectionService;
import org.opendaylight.ovsdb.internal.MessageHandler;
import org.opendaylight.ovsdb.internal.jsonrpc.JsonRpcEndpoint;
import org.opendaylight.ovsdb.internal.ovsdb.OVSDB;
import org.opendaylight.ovsdb.obj.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


public class OVSDBNettyFactoryTest extends TestCase {

    public void testSome() throws InterruptedException, ExecutionException {

        //todo(ashwin): this is a big mess without a bean factory like spring or guice
        ConnectionService service = new ConnectionService();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonRpcEndpoint factory = new JsonRpcEndpoint(objectMapper, service);

        List<ChannelHandler> _handlers = Lists.newArrayList();
        _handlers.add(new LoggingHandler(LogLevel.INFO));
        _handlers.add(new JsonRpcDecoder(100000));
        _handlers.add(new StringEncoder(CharsetUtil.UTF_8));
        _handlers.add(new JsonRpcServiceBinderHandler(factory));

        service.setHandlers(_handlers);
        service.init();

        String identifier = "TEST";
        Node.NodeIDType.registerIDType("OVS", String.class);
        Map<ConnectionConstants, String> params = new HashMap<ConnectionConstants, String>();
        params.put(ConnectionConstants.ADDRESS, "192.168.111.129");
        params.put(ConnectionConstants.PORT, "6632");
        Node node = service.connect(identifier, params);

        OVSDB ovsdb = factory.getClient(node, OVSDB.class);

        MonitorRequestBuilder monitorReq = new MonitorRequestBuilder();

//        monitorReq.monitor(Tables.bridge)
//                .column(Bridge.Column.controller)
//                .column(Bridge.Column.fail_mode);
//
//        monitorReq.monitor(Tables.port)
//                .column(Port.Column.trunks);


        ovsdb.monitor(monitorReq);


        ListenableFuture<EchoResponse> some = ovsdb.echo();
        Object s = some.get();
        System.out.printf("!!!!!!!!!!!>>>>>>><<<<<<<<!!!!!!!!!!!\n");
        System.out.printf("Result of echo is %s \n", s);


        ListenableFuture<DatabaseSchema> dbSchemaF = ovsdb.list_dbs();
        DatabaseSchema databaseSchema = dbSchemaF.get();
        System.out.println(databaseSchema);


        Thread.sleep(9000);
        service.disconnect(node);
    }

}
