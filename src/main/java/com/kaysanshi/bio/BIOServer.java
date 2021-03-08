package com.kaysanshi.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @user:kaysanshi
 * @date:2021/3/8
 * @Description:
 * 1.创建一个线程池
 * 2.创建一个客户端连接，与之通信
 */
public class BIOServer {
     public static void main(String[] args) throws IOException {
        // 线程池机制
         ExecutorService executorService = Executors.newCachedThreadPool();
         // 创建ServerSocket
         ServerSocket serverSocket = new ServerSocket(8888);

         while (true){
             System.out.println("线程信息 id:"+Thread.currentThread().getId()+"  线程名字 name:"+Thread.currentThread().getName());
             // 监听
             System.out.println("等待连接");
             final Socket socket= serverSocket.accept();
             // 创建一个线程与之通信
             executorService.execute(new Runnable() {
                 @Override
                 public void run() {
                     // 重写方法
                     handler(socket);
                 }
             });
         }
    }

    /**
     * 与客户端通信的方法
     * @param socket
     */
    public static void  handler(Socket socket){
        System.out.println("线程信息 id:"+Thread.currentThread().getId()+"  线程名字 name:"+Thread.currentThread().getName());
        byte[] bytes= new byte[1024];
        //创建liu
        try {
            InputStream inputStream = socket.getInputStream();
            // 循环读取客户端发来的信息
            while(true){
                System.out.println("线程信息 id:"+Thread.currentThread().getId()+"  线程名字 name:"+Thread.currentThread().getName());
                System.out.println("read....");
                int read = inputStream.read(bytes);
                if (read!=-1){
                    System.out.println(new String(bytes,0, read));
                }else{
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
