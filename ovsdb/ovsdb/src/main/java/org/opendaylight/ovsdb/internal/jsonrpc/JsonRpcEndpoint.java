package org.opendaylight.ovsdb.internal.jsonrpc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.google.common.reflect.Reflection;
import com.google.common.reflect.TypeToken;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import org.opendaylight.controller.sal.core.Node;
import org.opendaylight.ovsdb.internal.Connection;
import org.opendaylight.ovsdb.internal.ConnectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JsonRpcEndpoint {

    protected static final Logger logger = LoggerFactory.getLogger(JsonRpcEndpoint.class);

    public class CallContext {
        Method method;
        JsonRpc10Request request;
        SettableFuture<Object> future;

        public CallContext(JsonRpc10Request request, Method method, SettableFuture<Object> future) {
            this.method = method;
            this.request = request;
            this.future = future;
        }

        public Method getMethod() {
            return method;
        }

        public JsonRpc10Request getRequest() {
            return request;
        }

        public SettableFuture<Object> getFuture() {
            return future;
        }
    }

    ObjectMapper objectMapper;
    ConnectionService service;
    Map<String, CallContext> methodContext = Maps.newHashMap();

    public JsonRpcEndpoint(ObjectMapper objectMapper, ConnectionService service) {
        this.objectMapper = objectMapper;
        this.service = service;
    }

    public <T> T getClient(final Node node, Class<T> klazz) {

        return Reflection.newProxy(klazz, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                JsonRpc10Request request = new JsonRpc10Request(UUID.randomUUID().toString());
                request.setMethod(method.getName());


                if (args != null && args.length != 0) {
                    List<Object> params = null;

                    if (args.length == 1) {
                        if (args[0] instanceof Params) {
                            params = ((Params) args[0]).params();
                        } else if (args[0] instanceof List) {
                            params = (List<Object>) args[0];
                        }

                        if (params == null) {
                            throw new RuntimeException("do not understand this argument yet");
                        }
                        request.setParams(params);
                    }
                }

                String s = objectMapper.writeValueAsString(request);
                logger.debug("{}", s);

                SettableFuture<Object> sf = SettableFuture.create();
                methodContext.put(request.getId(), new CallContext(request, method, sf));

                Connection connection = service.getConnection(node);
                connection.getChannel().writeAndFlush(s);

                return sf;
            }
        }
        );
    }

    public void processResult(JsonNode response) throws NoSuchMethodException {

        CallContext returnCtxt = methodContext.get(response.get("id").asText());

        Class<?> returnType = null;

        if (ListenableFuture.class == returnCtxt.getMethod().getReturnType()) {
            TypeToken<?> retType = TypeToken.of(
                    returnCtxt.getMethod().getGenericReturnType())
                    .resolveType(ListenableFuture.class.getMethod("get").getGenericReturnType());

            JsonNode result = response.get("result");
            Object result1 = objectMapper.convertValue(result, retType.getRawType());
            returnCtxt.getFuture().set(result1);

        } else {
            throw new RuntimeException("donno how to deal with this");
        }
    }

    public Map<String, CallContext> getMethodContext() {
        return methodContext;
    }
}
