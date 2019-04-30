package com.vincent.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Vincent
 * @version 1.0
 * @date 2019/4/29 15:17
 */
public class BioServer {

    private static ExecutorService executorService = Executors.newFixedThreadPool(5);

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(Const.PORT);
        System.out.println("服务器已启动...");
        while (true) {
            Socket accept = serverSocket.accept();
            System.out.println("有新的客户端连接");
            executorService.execute(() -> {
                BufferedReader bufferedReader = null;
                PrintWriter printWriter = null;
                try {
                    bufferedReader = new BufferedReader(new InputStreamReader(accept.getInputStream()));
                    printWriter = new PrintWriter(accept.getOutputStream(), true);
                    String line;
                    String result;
                    while ((line = bufferedReader.readLine()) != null) {
                        System.out.println("server accept message: " + line);
                        result = "server response: " + line + " ,time:" + new Date();
                        printWriter.println(result);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("客户端断开了连接");
                } finally {
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (printWriter != null) {
                        printWriter.close();
                    }
                    if (accept != null) {
                        try {
                            accept.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }
}
