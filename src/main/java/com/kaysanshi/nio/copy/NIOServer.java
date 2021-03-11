package com.kaysanshi.nio.copy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @user: kaysanshi
 * @date:2021/3/11
 * @Description:
 */
public class NIOServer {
    public static void main(String[] args) {
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(9092));
            //创建buffer
            ByteBuffer byteBuffer = ByteBuffer.allocate(4096);
            while (true){
                SocketChannel socketChannel = serverSocketChannel.accept();
                int readCount=0;
                while (-1!=readCount){
                    readCount = socketChannel.read(byteBuffer);
                }
                // 倒带。position=0,mark作废
                byteBuffer.rewind();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
