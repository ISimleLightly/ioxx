package com.vincent.nio;

import com.vincent.bio.Const;

import java.util.Scanner;

/**
 * @author Vincent
 * @version 1.0
 * @date 2019/4/30 11:37
 */
public class NioClient {

    public static void main(String[] args) {
        NioClientHandler nioClientHandler = new NioClientHandler(Const.HOST, Const.PORT);
        new Thread(nioClientHandler).start();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            nioClientHandler.sendMessage(scanner.next());
        }
    }
}
