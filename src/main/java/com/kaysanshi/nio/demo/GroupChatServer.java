package com.kaysanshi.nio.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @user: kaysanshi
 * @date:2021/3/9
 * @Description:
 * 编写一个NIO群聊系统，实现服务器端与客户端的的户籍通信
 * 实现多人群聊
 * 服务端：可监听用户上线，离线，并实现转发
 * 客户端：通过channel可以无阻塞发送消息给其他所有客户，同时可以接收其他用户发送的消息（服务器的转发实现）
 */
public class GroupChatServer {
    // 定义属性
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int PORT = 8898;

    // 构造器初始化工作
    public GroupChatServer(){
        try {
            // 得到 选则器
            selector = Selector.open();
            // 服务端监听打开
            listenChannel = ServerSocketChannel.open();
            // 绑定监听的端口
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            // 将channel设置为非阻塞模式
            listenChannel.configureBlocking(false);
            // 将listenChannel注册到selector
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void listen(){
        try {
            while (true) {
                int count = selector.select();
                if(count>0){
                    // 用事件处理
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    // 遍历Set<SelectionKey>
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()){
                        // 获取到SelectionKey
                        /**
                         * SelectionKey类表示Selector和网络通道的注册关系 的方法:
                         * isAcceptable(): 是否可以accpet
                         * isReadable() : 是否可以读
                         * isWritable() : 是否可以写
                         * attachment() : 得到与之关联的共享数据
                         * channel() : 得到与之关联的channel()
                         */
                        SelectionKey key = iterator.next();
                        // 根据key对应的通道发生的事件做相应的处理
                        if(key.isAcceptable()){
                            // 如果是OP_ACCEPT 有新的客户端连接
                            SocketChannel socketChannel = listenChannel.accept();
                            // 将socket设置为阻塞行的
                            socketChannel.configureBlocking(false);
                            System.out.println("客户端连接成功生成一个socketChannel"+socketChannel.hashCode());
                            // 将channel注册到selector
                            socketChannel.register(selector,SelectionKey.OP_READ);
                            System.out.println(socketChannel.getRemoteAddress()+" 上 线");
                        }
                        if(key.isReadable()){
                            // 发生 OP_READ
                            // 处理读
                            readData(key);
                        }
                        // 手动从集合中移动当前的selectionKey防止重复操作
                        iterator.remove();
                    }
                }else{
                    System.out.println("等待...");
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            // 最后执行逻辑
        }

    }

    /**
     * 读数据
     * @param key
     */
    private void readData(SelectionKey key) {
        // 获取到关联的channel
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        try {
            int count = channel.read(byteBuffer);
            if(count>0){
                String massage=new String(byteBuffer.array());
                // 把缓冲区的数据转成字符串
                System.out.println("from 客户端：" +massage);
                // 向其他客户端转发消息(去掉自己)
                sendMessageToOtherClients(massage,channel);
            }
        } catch (IOException e) {
            try {
                System.out.println(channel.getRemoteAddress()+"离线了。。。");
                // 取消注册
                key.cancel();
                channel.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            e.printStackTrace();
        }


    }

    /**
     * 向其他用户发送消息
     * @param massage
     * @param selfChannel
     */
    private void sendMessageToOtherClients(String massage, SocketChannel selfChannel) {
        System.out.println("服务器转发消息中。。");
        for(SelectionKey key: selector.keys()){
            // 通过key获取SocketChannel
            Channel targetChannel = key.channel();

            // 排除自己
            if(targetChannel instanceof  SocketChannel && targetChannel!=selfChannel){

                // 将massage存储到buffer
                ByteBuffer buffer = ByteBuffer.wrap(massage.getBytes());
                try {
                    ((SocketChannel) targetChannel).write(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        GroupChatServer server = new GroupChatServer();
        server.listen();
    }
    //~out
    /**
     * 客户端连接成功生成一个socketChannel668386784
     * /127.0.0.1:63394 上 线
     * from 客户端：127.0.0.1:63394说 ：aaa
     * 服务器转发消息中。。
     * 客户端连接成功生成一个socketChannel363771819
     * /127.0.0.1:63533 上 线
     * from 客户端：127.0.0.1:63533说 ：bbb
     * 服务器转发消息中。。
     * from 客户端：127.0.0.1:63533说 ：hello
     * 服务器转发消息中。。
     */
}
