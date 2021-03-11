package com.kaysanshi.bio;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * @user:kaysanshi
 * @date:2021/3/8
 * @Description:
 * 客户端
 * 1.创建一个Socket实例
 * 2.利用I/O流与服务器进行通信
 * 3.关闭socket
 */
public class BIOClient {
    public static void main(String[] args) {
        try {
            // 创建与服务端的连接
            Socket socket = new Socket("127.0.0.1",8888);
            // 设置超时时间
            socket.setSoTimeout(5000);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
            outputStreamWriter.write("test connection...");
            outputStreamWriter.flush();
            Thread.sleep(500);
            outputStreamWriter.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
