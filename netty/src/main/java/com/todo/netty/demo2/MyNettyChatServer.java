package com.todo.netty.demo2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;

import javax.xml.bind.SchemaOutputResolver;
import java.util.UUID;


/**
 *@description:聊天机器人+消息广播示例
 *
 *@author Sjh
 *@date 2019/6/17 15:22
 *@version 1.0.1
 */

public class MyNettyChatServer {

    public static void main(String[] args) throws Exception {
        //Boss（老板） 线程组，监听请求
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //工作（工人） 线程组，处理（老板）线程组派发的请求
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            //启封引导
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(new MyChatServerInitializer());

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

class MyChatServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {

        ChannelPipeline pipeline = socketChannel.pipeline();

        //添加需要的handlers
        pipeline.addLast("delimiterBasedFrameDecoder", new DelimiterBasedFrameDecoder(4096, Delimiters.lineDelimiter()));
        pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
        pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
        pipeline.addLast(new MyChatServerHandler());

    }

}

/**
 * 通信处理器
 */
class MyChatServerHandler extends SimpleChannelInboundHandler<String> {

    //保存channel对象（有性能瓶颈）
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

        Channel channel = ctx.channel();

        channelGroup.forEach(ch -> {
            if (channel != ch) {
                ch.writeAndFlush(channel.remoteAddress() + " 发送的消息 :" + msg + "\n");
            } else {
                ch.writeAndFlush(channel.remoteAddress() + " 【自己】 :" + msg + "\n");
            }
        });

    }

    /**
     * 客户端连接建立
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //新建立的连接channel
        Channel channel = ctx.channel();

        //向已保存的所有channel广播信息
        channelGroup.writeAndFlush("【服务器】-" + channel.remoteAddress() + "加入\n");

        //保存新建立连接的channel
        channelGroup.add(channel);
    }


    /**
     * 客户端连接断开
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        //断开的连接channel
        Channel channel = ctx.channel();

        //删除断开的channel
        //channelGroup.remove(channel);此代码无需手工调用

        //向其它已保存的所有channel广播信息
        channelGroup.writeAndFlush("【服务器】-" + channel.remoteAddress() + "离开\n");
        System.out.println(channelGroup.size());

    }

    /**
     * 连接活动
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress() + "上线");
    }

    /**
     * 连接不活动
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress() + "下线");
    }

    /**
     * 捕获异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}