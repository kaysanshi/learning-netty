package com.kaysanshi.nettytcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.util.CharsetUtil;

/**
 * @user:
 * @date:2021/3/11
 * @Description:
 * 自定义一个Handler
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * ChannelHandlerContext ：上下文对象，含有 管道pipeline，通道channel
     *
     * @param ctx
     * @param msg  客户端发送的数据
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //super.channelRead(ctx, msg);
        System.out.println("服务器读取线程：" +Thread.currentThread().getName());
        System.out.println("server ctx :"+ctx);
        Channel channel = ctx.channel();
        ChannelPipeline pipeline = channel.pipeline(); // 本质是一个双向的链，出栈入栈。

        // 将msg转成一个ByteBuf 这是netty提供的，不是NIO的 ByteBuffer
        ByteBuf byteBuf=(ByteBuf) msg;
        System.out.println("客户端发来的消息为："+byteBuf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端地址："+channel.remoteAddress());
    }

    /**
     * 数据读取完毕
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        super.channelReadComplete(ctx);
        // 将数据写入缓存，并刷新
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端",CharsetUtil.UTF_8));
    }

    /**
     * 有异常了，直接关闭通道
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        super.exceptionCaught(ctx, cause);
        ctx.close();
    }
}
