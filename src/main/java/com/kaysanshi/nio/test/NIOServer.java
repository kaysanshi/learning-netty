package com.kaysanshi.nio.test;

import com.kaysanshi.nio.Buffer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @user: kaysanshi
 * @date:2021/3/9
 * @Description: NIO的server
 * 实现客户端与 服务端的通信（非阻塞）
 */
public class NIOServer {
    public static void main(String[] args) throws IOException {
        // 创建一个ServerSocketChannel -> ServerSocket
        ServerSocketChannel serverSocketChannel=ServerSocketChannel.open();
        // 得到一个Selector
        Selector selector = Selector.open();
        // 绑定端口再服务端进行监听
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
        // 设置为非阻塞IO
        serverSocketChannel.configureBlocking(false);
        // 把 severSocketChannel注册到selector
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true){
            if(selector.select(1000) ==0){
                // 等待1s没有任何事情发生，返回
                System.out.println("服务器等待了1s,没有其连接");
                continue;
            }
            /**
             * 如果返回的>0就获取到了相关的selectionKey集合
             * selector.selectedKeys() 返回关注事件的集合通过selectionsKeys() 反向获取通道
             */
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
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("客户端连接成功生成一个socketChannel"+socketChannel.hashCode());
                    // 将SocketChannel设置为非zuse
                    socketChannel.configureBlocking(false);
                    // 将socketChannel注册到selector事件为OP_READ同时给socketChannel关联一个buffer
                    socketChannel.register(selector,SelectionKey.OP_ACCEPT, ByteBuffer.allocate(1024));
                }
                if(key.isReadable()){
                    // 发生 OP_READ
                    // 通过key 反向获取对应的channel
                    SocketChannel channel = (SocketChannel) key.channel();
                    // 获取到该channel关联的buffer
                    ByteBuffer attachment =(ByteBuffer) key.attachment();
                    channel.read(attachment);
                    System.out.println("from客户端："+new String(attachment.array()));
                }
                // 手动从集合中移动当前的selectionKey防止重复操作
                iterator.remove();
            }
        }
    }
}
