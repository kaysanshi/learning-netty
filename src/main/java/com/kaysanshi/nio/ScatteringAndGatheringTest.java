package com.kaysanshi.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * @user: kaysanshi
 * @date:2021/3/8
 * @Description: NIO支持多个Buffer完成读写操作
 */
public class ScatteringAndGatheringTest {
    public static void main(String[] args) {
        // 使用ServerSocketChannel 和 SocketChannel
        try {
            // ServerSocketChannel 功能类似与SeverSocket，在服务端监听新的客户端连接
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            InetSocketAddress inetSocketAddress = new InetSocketAddress(9999);
            // 从ServerSocketChannel获取socket并启动
            serverSocketChannel.socket().bind(inetSocketAddress);
            // 创建buffer数组
            ByteBuffer[] byteBuffers=new ByteBuffer[2];
            byteBuffers[0] = ByteBuffer.allocate(3);
            byteBuffers[1] = ByteBuffer.allocate(5);
            // 等待客户端进行连接
            // SocketChannel类是网络的IO通道具体负责读写操作，NIO把缓冲区的数据写入通道，或者把通道的数据写入缓冲区
            SocketChannel accept = serverSocketChannel.accept();
            int messageLength = 10 ; // 假定从客户端接收的字节数
            while (true){
                int byteRead = 0;
                while (byteRead<messageLength){
                    long l = accept.read(byteBuffers);
                    byteRead+=l;
                    System.out.println("byteRead="+ byteRead);
                    // 使用打印流，看看当前的这个buffer的position和limit
                    Arrays.stream(byteBuffers).map(buffer->"postion="+buffer.position()+",limit="+buffer.limit()).forEach(System.out::println);
                }
                // 将所有的buffer进行flip()
                Arrays.asList(byteBuffers).forEach(Buffer::flip);
                // 将数据读出显示到客户端
                long byteWrite =0;
                while(byteWrite <messageLength){
                    long l = accept.write(byteBuffers);
                    byteWrite+=l;
                }
                // 将所有的buffer进行clear()
                Arrays.asList(byteBuffers).forEach(Buffer::clear);
                System.out.println("byteRead="+byteRead+"byteWrite="+byteWrite+",messagelength"+messageLength);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
