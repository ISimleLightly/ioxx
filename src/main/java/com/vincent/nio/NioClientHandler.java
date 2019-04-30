package com.vincent.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Vincent
 * @version 1.0
 * @date 2019/4/30 10:29
 */
public class NioClientHandler implements Runnable {

    private String host;
    private int port;
    private SocketChannel socketChannel;
    private Selector selector;
    private volatile boolean start;

    public NioClientHandler(String host, int port) {
        this.host = host;
        this.port = port;
        try {
            socketChannel = SocketChannel.open();
            selector = Selector.open();
            socketChannel.configureBlocking(false);
            start = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop(){
        start = false;
    }

    @Override
    public void run() {
        doConnect();
        while (start) {
            try {
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    handle(key);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handle(SelectionKey key) {
        if (key.isValid()) {
            SocketChannel channel = (SocketChannel) key.channel();
            if (key.isConnectable()) {
                try {
                    if (!channel.finishConnect()) {
                        System.exit(1);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (key.isReadable()) {
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                try {
                    int i = channel.read(byteBuffer);
                    if (i > 0) {
                        byteBuffer.flip();
                        byte[] bytes = new byte[byteBuffer.remaining()];
                        byteBuffer.get(bytes);
                        System.out.println("client accept message: " + new String(bytes, StandardCharsets.UTF_8));
                    } else {
                        key.cancel();
                        key.channel().close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void doConnect() {
        try {
            if (socketChannel.connect(new InetSocketAddress(host, port))) {

            } else {
                socketChannel.register(selector, SelectionKey.OP_CONNECT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doWrite(SocketChannel socketChannel, String msg) {
        byte[] bytes = msg.getBytes();
        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
        byteBuffer.put(bytes);
        byteBuffer.flip();
        try {
            socketChannel.write(byteBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String msg) {
        try {
            socketChannel.register(selector, SelectionKey.OP_READ);
            doWrite(socketChannel, msg);
        } catch (ClosedChannelException e) {
            e.printStackTrace();
        }
    }
}
