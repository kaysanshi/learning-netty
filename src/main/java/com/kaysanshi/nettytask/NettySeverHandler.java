package com.kaysanshi.nettytask;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * @user: kaysanshi
 * @date:2021/3/11
 * @Description: 任务队列Task有三种典型的使用场景
 * 1.用户程序自定义的普通任务
 * 2.用户自定义定时任务
 * 3.非当前Reactor线程掉用Channel的各种方法。
 *      例如在推送业务，根据用户标识，找到对应的channel引用，然后调用Write类方法向该用户推送消息，就会进入这种场景，最终Write徽提交到任务队列中后被异步消费
 */
public class NettySeverHandler extends ChannelInboundHandlerAdapter {
    /**
     * 读取实际数据
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        super.channelRead(ctx, msg);
        // 1.用户程序自定义的普通任务
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5*1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello client", CharsetUtil.UTF_8));
                    System.out.println("channel code = "+ ctx.channel().hashCode());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("异常");
                }
            }
        });
        // 2.用户自定义定时任务-》 该任务提交给scheduledTaskQueue中
        ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5*1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello client", CharsetUtil.UTF_8));
                    System.out.println("channel code = "+ ctx.channel().hashCode());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("异常");
                }
            }
        },5, TimeUnit.SECONDS);
    }

    /**
     * 数据读取完毕
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        super.channelReadComplete(ctx);
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端", CharsetUtil.UTF_8));
    }

    /**
     * 出现异常
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
