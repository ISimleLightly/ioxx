package com.vincent.netty.serializable.msgpack;

import com.vincent.bio.Const;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.LineBasedFrameDecoder;

import java.net.InetSocketAddress;

public class MsgPackClient {

    public static void main(String[] args) {
        EventLoopGroup eventExecutors = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventExecutors)
                .channel(NioSocketChannel.class)
                .remoteAddress(new InetSocketAddress(Const.HOST, Const.PORT))
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new LengthFieldPrepender(2));
                        socketChannel.pipeline().addLast(new MsgPackEncoder());
                        socketChannel.pipeline().addLast(new LineBasedFrameDecoder(1024));
                        socketChannel.pipeline().addLast(new MsgPackClientHandler(5));
                    }
                });
        try {
            ChannelFuture future = bootstrap.connect().sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            eventExecutors.shutdownGracefully();
        }
    }
}
