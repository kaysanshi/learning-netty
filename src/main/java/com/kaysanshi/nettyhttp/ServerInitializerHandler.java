package com.kaysanshi.nettyhttp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 * @user:
 * @date:2021/3/11
 * @Description:
 * SimpleChannelInboundHandler是ChannelInboundHandlerAdapter的子类
 * 客户端和服务端相同铜通讯的数据装成HttpObject
 */
public class ServerInitializerHandler extends SimpleChannelInboundHandler<HttpObject> {
    /**
     * 读取客户端数据
     * @param channelHandlerContext
     * @param httpObject
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject httpObject) throws Exception {
        // 判断msg是不是httprequest请求
        if(httpObject instanceof HttpRequest){
            System.out.println("pipeline hash code "+channelHandlerContext.pipeline().hashCode() +" ServerInitializerHandler hash = "+this.hashCode());
            System.out.println("msg类型："+httpObject.getClass());
            System.out.println("客户端地址：："+channelHandlerContext.channel().remoteAddress());
            URI uri = new URI(((HttpRequest) httpObject).uri());
            if("/favicon.ico".equals(uri.getPath())){
                System.out.println("请求了 favicon,不做响应");
                return;
            }
            // 回复信息
            ByteBuf byteBuf = Unpooled.copiedBuffer("hello ,客户端", CharsetUtil.UTF_8);
            // 构造一个http响应
            FullHttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
            httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain");
            httpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH,byteBuf.readableBytes());

            // 将响应写入客户端
            channelHandlerContext.writeAndFlush(httpResponse);
        }


    }
}
