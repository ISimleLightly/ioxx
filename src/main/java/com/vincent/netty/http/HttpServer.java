package com.vincent.netty.http;

import com.vincent.bio.Const;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

public class HttpServer {
    private static EventLoopGroup eventExecutors = new NioEventLoopGroup();
    private static ServerBootstrap serverBootstrap = new ServerBootstrap();

    public static void main(String[] args) {
        serverBootstrap.group(eventExecutors)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast("encode", new HttpResponseEncoder());
                        socketChannel.pipeline().addLast("decode", new HttpRequestDecoder());
                        socketChannel.pipeline().addLast("aggregator", new HttpObjectAggregator(10 * 1024 * 1024));
                        socketChannel.pipeline().addLast("compressor", new HttpContentCompressor());
                        socketChannel.pipeline().addLast("business", new BusinessHandler());
                    }
                });
        try {
            ChannelFuture future = serverBootstrap.bind(Const.PORT).sync();
            System.out.println("服务器启动成功，端口：" + Const.PORT);
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            eventExecutors.shutdownGracefully();
        }

    }

}
