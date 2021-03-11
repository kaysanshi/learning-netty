package com.kaysanshi.nio.copy;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

/**
 * @user: kaysanshi
 * @date:2021/3/11
 * @Description:
 */
public class NIOClient {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel=SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("127.0.0.1",9092));
        String fileName="";
        // 得到文件
        FileChannel channel = new FileInputStream(fileName).getChannel();
        long l1 = System.currentTimeMillis();
        // transferTo底层采用的是零copy.
        // windows下一次调用transferTo之恶能发送8M，需要分段传输文件
        long l = channel.transferTo(0, channel.size(), socketChannel);
        System.out.println("发送的总的字节数="+l+"总耗时："+(System.currentTimeMillis()-l1));
        // 关闭
        channel.close();
    }
}
