package com.kaysanshi.nio.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @user: kaysanshi
 * @date:2021/3/9
 * @Description:
 *  * 编写一个NIO群聊系统，实现服务器端与客户端的的户籍通信
 *  * 实现多人群聊
 *  * 服务端：可监听用户上线，离线，并实现转发
 *  * 客户端：通过channel可以无阻塞发送消息给其他所有客户，同时可以接收其他用户发送的消息（服务器的转发实现）
 */
public class GroupServerClient {

    private final  String HOST = "127.0.0.1";
    private final int PORT = 8898;
    private Selector selector;
    private SocketChannel socketChannel;
    private String username;

    public GroupServerClient(){
        try {
            selector=Selector.open();
            socketChannel = SocketChannel.open(new InetSocketAddress(HOST,PORT));
            // 设置为非阻塞的
            socketChannel.configureBlocking(false);
            // 将channel注册到selector
            socketChannel.register(selector, SelectionKey.OP_READ);
            // 得到usename
            username = socketChannel.getLocalAddress().toString().substring(1);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 向服务器发送消息
    public void sendInfo(String info){
        info = username + "说 ："+info;
        try {
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // 读取从服务端回复的消息
    public void readInfo(){
        try {
            int selectChannels = selector.select();
            if(selectChannels>0){
                // 有可以用的通道
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    if(key.isReadable()){
                        SocketChannel channel = (SocketChannel) key.channel();
                        // 获取到该channel关联的buffer
                        ByteBuffer attachment =ByteBuffer.allocate(1024);
                        // 读取buffer
                        channel.read(attachment);
                        System.out.println("from客户端："+new String(attachment.array()));
                    }
                }
                iterator.remove();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        GroupServerClient groupServerClient = new GroupServerClient();
        new Thread(){
            @Override
            public void run() {
                while (true){
                    groupServerClient.readInfo();
                    try {
                        Thread.currentThread().sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()){
            String s = scanner.nextLine();
            groupServerClient.sendInfo(s);
        }
    }
}
