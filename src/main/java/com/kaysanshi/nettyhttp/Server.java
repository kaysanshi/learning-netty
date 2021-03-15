package com.kaysanshi.nettyhttp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @user: kaysanshi
 * @date:2021/3/11
 * @Description:
 *  Netty服务器Http服务。
 *  Netty服务器在端口监听，
 *  n
 */
public class Server {
    public static void main(String[] args) {
        EventLoopGroup bossGroup= new NioEventLoopGroup(1);
        EventLoopGroup workGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup,workGroup) // 设置两个线程组
                    .channel(NioServerSocketChannel.class)
                .childHandler(new ServerInitializer());
        try {

            ChannelFuture channelFuture = serverBootstrap.bind(9999).sync();
            channelFuture.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
