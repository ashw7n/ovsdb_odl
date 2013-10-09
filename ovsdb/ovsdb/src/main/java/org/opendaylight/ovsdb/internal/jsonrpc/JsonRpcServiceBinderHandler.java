package org.opendaylight.ovsdb.internal.jsonrpc;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.SettableFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class JsonRpcServiceBinderHandler extends ChannelInboundHandlerAdapter {
    protected static final Logger logger = LoggerFactory.getLogger(JsonRpcServiceBinderHandler.class);
    Map<Object, SettableFuture<Object>> waitingForReply = Maps.newHashMap();
    JsonRpcEndpoint factory = null;

    public JsonRpcServiceBinderHandler(JsonRpcEndpoint factory) {
        this.factory = factory;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof JsonNode) {
            JsonNode jsonNode = (JsonNode) msg;

            if (jsonNode.has("result")) {
                factory.processResult(jsonNode);
            } else if (jsonNode.hasNonNull("method")) {
                if (jsonNode.has("id") && Strings.isNullOrEmpty(jsonNode.get("id").asText())) {

                }
            }

            return;
        }

        ctx.channel().close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }


}