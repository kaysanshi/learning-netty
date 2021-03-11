package com.kaysanshi.nettytcp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @user: kaysanshi
 * @date:2021/3/11
 * @Description: netty TCP服务
 */
public class NettyClient {
    public static void main(String[] args)  {
        // 客户端需要一个事件循环组
        // NioEventLoopGroup ： 表示一个不断循环执行处理任务的线程，每一个NioEventLoopGroup都有一个selector，用于监听绑定在其上的socket网络通道
        // NioEventLoopGroup：内部采用串行化设计，从消息的读取-》解码-》处理-》编码-》发送，始终由IO线程NioEventLoop负责
        // NioEventLoopGroup下包含多个NioEventLoop
        // 每个NioEventLoop包含一个selector,一个taskQueue,每个Selector上可注册多个NioChannel,
        // 每个NioChannel只会绑定在唯一的NioEventLoop上，
        // 每个NioChannel都会绑定一个自己的ChannelPipeline
        EventLoopGroup group = new NioEventLoopGroup();
        // 创建客户端的启动对象
        Bootstrap bootstrap = new Bootstrap();
        // 设置相关的参数
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new NettyClientHandler());
                    }
                });
        System.out.println("客户端。。准备发射");
        // 启动客户端去链接服务器
        ChannelFuture channelFuture = null;
        try {
            channelFuture = bootstrap.connect("127.0.0.1", 9999).sync();
            // 给关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();
        }

    }
}
