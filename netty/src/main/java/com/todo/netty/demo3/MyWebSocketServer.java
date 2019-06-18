package com.todo.netty.demo3;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.net.InetSocketAddress;
import java.time.LocalDateTime;



/**
 *@description:  WebSocket  通信， 客户端在:test.html
 *
 *@param
 *@author Sjh
 *@date 2019/6/18 10:38
 *@return
 *@version 1.0.1
 */
public class MyWebSocketServer {

    public static void main(String[] args) throws Exception {
        //Boss（老板） 线程组，监听请求
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //工作（工人） 线程组，处理（老板）线程组派发的请求
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            //启封引导
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new MyWebSocketChannelInitializer());

            //异步监听端口
            ChannelFuture channelFuture = serverBootstrap.bind(new InetSocketAddress(8899)).sync();
            channelFuture.channel().closeFuture().sync();

        } finally {
            //优雅停机
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}

class MyWebSocketChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new ChunkedWriteHandler());
        pipeline.addLast(new HttpObjectAggregator(8192));
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));

        pipeline.addLast(new TextWebSocketFrameHandler());
    }
}


class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        System.out.println("收到客户端消息 :" + msg.text());
        ctx.channel().writeAndFlush(new TextWebSocketFrame("服务器时间 " + LocalDateTime.now()));//返回给客户端
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerAdded ：" + ctx.channel().id().asLongText());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerRemoved ：" + ctx.channel().id().asLongText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常发生");
        ctx.close();
    }
}