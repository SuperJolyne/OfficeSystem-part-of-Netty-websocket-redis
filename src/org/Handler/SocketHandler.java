package org.Handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;

import java.util.logging.Logger;

import static org.Handler.Global.map;

public class SocketHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger logger = Logger.getLogger(WebSocketServerHandshaker.class.getName());

    private WebSocketServerHandshaker handshaker;

    @Override
    public void channelActive(ChannelHandlerContext ctx){
        Global.group.add(ctx.channel());
        System.out.println("客户端与服务端连接开启");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx){
        Global.group.remove(ctx.channel());
        System.out.println("客户端与服务端连接关闭");
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof FullHttpRequest) {

            handleHttpRequest(ctx, ((FullHttpRequest) msg));

        } else if (msg instanceof WebSocketFrame) {

            handlerWebSocketFrame(ctx, (WebSocketFrame) msg);

        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx){
        ctx.flush();
    }

    public static void sendMessage(String id){

        if(map.get(id) != null) {
            ChannelHandlerContext ctx = map.get(id);
            TextWebSocketFrame text = new TextWebSocketFrame("picture");
            ctx.channel().writeAndFlush(text);
        }
    }

    public static void sendAllMessage(String s){

        //将客户端发过来的消息发送给所有的socket
        TextWebSocketFrame text = new TextWebSocketFrame(s);
        Global.group.writeAndFlush(text);
    }


    private void handlerWebSocketFrame(ChannelHandlerContext ctx,
                                       WebSocketFrame frame){

        if (frame instanceof CloseWebSocketFrame){
            handshaker.close(ctx.channel(),((CloseWebSocketFrame) frame).retain());
            System.out.println("==="+Global.map.keySet());
            if (true == Global.col.contains(ctx)){
                Global.col.remove(ctx);
            }
            System.out.println("==="+Global.map.keySet());
        } else

        if (frame instanceof PingWebSocketFrame){
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        } else
        if (!(frame instanceof TextWebSocketFrame)) {


            System.out.println("本例程仅支持文本消息，不支持二进制消息");

            throw new UnsupportedOperationException(String.format(
                    "%s frame types not supported", frame.getClass().getName()));
        } else
        if (frame instanceof TextWebSocketFrame) {
            //将客户端发过来的消息发送给所有的socket
            TextWebSocketFrame text = new TextWebSocketFrame(((TextWebSocketFrame) frame).text());
            Global.group.writeAndFlush(text);
        }

    }



    private void handleHttpRequest(ChannelHandlerContext ctx,
                                   FullHttpRequest req) {

        String[] strings = req.uri().split("/");
        String id = strings[2];
        map.put(id,ctx);

        if (!req.decoderResult().isSuccess()
                || (!"websocket".equals(req.headers().get("Upgrade")))) {

            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));

            return;
        }

        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                "ws://0.0.0.0:8080/websocket", null, false);

        handshaker = wsFactory.newHandshaker(req);

        if (handshaker == null) {
            WebSocketServerHandshakerFactory
                    .sendUnsupportedVersionResponse(ctx.channel());
        } else {
            handshaker.handshake(ctx.channel(), req);
        }

    }

    private static void sendHttpResponse(ChannelHandlerContext ctx,
                                         FullHttpRequest req, DefaultFullHttpResponse res) {

        // 返回应答给客户端
        if (res.status().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(),
                    CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
        }

        // 如果是非Keep-Alive，关闭连接
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!isKeepAlive(req) || res.status().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }
    private static boolean isKeepAlive(FullHttpRequest req) {

        return false;
    }
}
