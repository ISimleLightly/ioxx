package com.vincent.netty.serializable.msgpack;

import com.vincent.bio.Const;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.net.InetSocketAddress;

public class MsgPackServer {
    public static void main(String[] args) {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(eventLoopGroup)
                .channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(Const.PORT))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2));
                        socketChannel.pipeline().addLast(new MsgPackDecoder());
                        socketChannel.pipeline().addLast(new MsgPackServerHandler());
                    }
                });
        try {
            ChannelFuture future = serverBootstrap.bind().sync();
            System.out.println("服务器启动。。。");
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }
}
