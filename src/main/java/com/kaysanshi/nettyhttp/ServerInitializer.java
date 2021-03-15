package com.kaysanshi.nettyhttp;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @user:
 * @date:2021/3/11
 * @Description:
 */
public class ServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        // 向管道中加入处理器
        // 得到管道
        ChannelPipeline pipeline = socketChannel.pipeline();
        // 加入一个netty提供的HttpServerCodec codec=>[coder - decoder]
        // HttpServerCodec是netty提供处理http的编-解码器
        pipeline.addLast("MyhttpServerCodec",new HttpServerCodec());
        // 增加一个自定义的handler
        pipeline.addLast("MyTestHttpServerHandler",new ServerInitializerHandler());

    }
}
