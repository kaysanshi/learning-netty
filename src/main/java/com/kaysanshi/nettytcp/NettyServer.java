package com.kaysanshi.nettytcp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @user: kaysanshi
 * @date:2021/3/11
 * @Description: net TCP服务
 */
public class NettyServer {
    public static void main(String[] args) {
        /**
         * 1.创建两个线程组：BossGroup 和 WorkerGroup
         * 2.BossGroup只是处理连接请求，真正的和客户端的业务处理会交给workerGroup
         * 3.两个是无线循环，
         * 4.两者都含有NioEventLoop个数 默认实际为cpu核数*2
         */
        EventLoopGroup bossGroup= new NioEventLoopGroup(1);
        /**
         * NioEventLoopGroup:包含多个NioEventLoop
         * 每个NioEventLoop中包含一个Selector,一个taskQueue
         * 每个NioEventLoop的Selector上可以注册监听多个NioChannel
         * 每个NioChannel只会绑定一个唯一的NioEventLoop
         * 每个NioChannel绑定一个ChannelPipeline
         */
        EventLoopGroup workGroup = new NioEventLoopGroup();
        ServerBootstrap  bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup,workGroup) // 设置两个线程组
                .channel(NioServerSocketChannel.class) // 使用NioSocketChannel作为服务器的通道实现
                .option(ChannelOption.SO_BACKLOG,128) // 设置线程队列的连接个数
                .childOption(ChannelOption.SO_KEEPALIVE,true) // 设置保持活动连接状太
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    // 给pipeline设置处理器
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        // 使用自定义的nettyHandler
                        socketChannel.pipeline().addLast(new NettyServerHandler());
                    }
                });
        System.out.println("服务器已经准备...");
        try {
            // 绑定一个端口并且同步，生成一个ChannelFuture对象
            // 启动服务器
            ChannelFuture channelFuture=bootstrap.bind(9999).sync();
            // 对关闭的通道进行监听
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();

        }
    }
}
