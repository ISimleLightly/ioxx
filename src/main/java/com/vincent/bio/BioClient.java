package com.vincent.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author Vincent
 * @version 1.0
 * @date 2019/4/29 14:47
 */
public class BioClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket(Const.HOST, Const.PORT);
            System.out.println("请输入发送消息：");
            new Thread(() -> {
                BufferedReader bufferedReader = null;
                try {
                    bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        System.out.println(line);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("服务器断开了连接");
                } finally {
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            while (true) {
                PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
                printWriter.println(new Scanner(System.in).next());
                printWriter.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
