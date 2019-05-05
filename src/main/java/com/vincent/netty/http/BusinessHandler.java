package com.vincent.netty.http;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

public class BusinessHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String result;
        FullHttpRequest fullHttpRequest = (FullHttpRequest) msg;
        try {
            String uri = fullHttpRequest.uri();
            HttpMethod method = fullHttpRequest.method();
            String body = fullHttpRequest.content().toString(CharsetUtil.UTF_8);
            if (!"/test".equalsIgnoreCase(uri)) {
                result = "非法请求：" + uri;
                send(result, ctx, HttpResponseStatus.BAD_REQUEST);
                return;
            }
            if (method.equals(HttpMethod.GET)) {
                System.out.println("body: " + body);
                result = RespConstant.getNews();
                send(result, ctx, HttpResponseStatus.OK);
            }
            if (method.equals(HttpMethod.POST)) {
                //...
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fullHttpRequest.release();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("连接的客户端地址：" + ctx.channel().remoteAddress());
    }

    private void send(String content, ChannelHandlerContext ctx, HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, Unpooled.copiedBuffer(content, CharsetUtil.UTF_8));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=UTF-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}
