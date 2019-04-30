package com.vincent.nio;

import com.vincent.bio.Const;

/**
 * @author Vincent
 * @version 1.0
 * @date 2019/4/30 11:50
 */
public class NioServer {
    public static void main(String[] args) {
        NioServerHandler nioServerHandler = new NioServerHandler(Const.PORT);
        new Thread(nioServerHandler).start();
    }
}
