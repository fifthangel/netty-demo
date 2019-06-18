package com.todo.netty.demo4;

import com.todo.protobuf.DataInfo;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.logging.LoggingHandler;

/**
 *@description:服务端
 *
 *@param
 *@author Sjh
 *@date 2019/6/18 13:58
 *@return
 *@version 1.0.1
 */

public class MyNettyServer {

    public static void main(String[] args) throws Exception {
        //Boss（老板） 线程组，监听请求
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //工作（工人） 线程组，处理（老板）线程组派发的请求
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            //启动netty服务器引导
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            //
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class).handler(new LoggingHandler())// parent (acceptor) Boss
                    .childHandler(new MyServerInitializer());// child (client) Worker

            //异步监听端口
            ChannelFuture channelFuture = serverBootstrap.bind(8899).sync();
            channelFuture.channel().closeFuture().sync();

        } finally {
            //优雅停机
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}

/**
 * Initializer channel <SocketChannel>
 */
class MyServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        //打开管道
        ChannelPipeline pipeline = socketChannel.pipeline();

        //添加 protobuf 各个处理handler ----！！！！------
        pipeline.addLast(new ProtobufVarint32FrameDecoder());
        pipeline.addLast(new ProtobufDecoder(DataInfo.Person.getDefaultInstance()));
        pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
        pipeline.addLast(new ProtobufEncoder());

        //添加 自定义handler -入站处理
        pipeline.addLast(new MyServerHandler());
    }

    /**
     * 异常处理
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}

/**
 * 入站信息处理handler
 */
class MyServerHandler extends SimpleChannelInboundHandler<DataInfo.Person> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DataInfo.Person person) throws Exception {

        System.out.println(person);
        System.out.println(person.getName());
        System.out.println(person.getAge());
        System.out.println(person.getAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}