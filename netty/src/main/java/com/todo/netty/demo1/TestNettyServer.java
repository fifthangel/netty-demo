package com.todo.netty.demo1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 *@description:Netty入门级demo--http服务器
 * ---------------------http服务端----------------------
 *@author Sjh
 *@date 2019/6/17 13:57
 *@version 1.0.1
 */
public class TestNettyServer {

    /**
     * Server端启动（引导）
     */
    public static void main(String[] args) throws Exception {
        //Boss（老板） 线程组，监听请求
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //工作（工人） 线程组，处理（老板）线程组派发的请求
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            //服务端启动引导
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            //注入启动时线程
            serverBootstrap.group(bossGroup, workerGroup)
                           .channel(NioServerSocketChannel.class)//bossGroup采用Nio
                           .childHandler(new TestServerInitializer());//workerGroup添加处理管道

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
 * w实例化处理管道
 */
class TestServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        //打开管道
        ChannelPipeline pipeline = socketChannel.pipeline();
        //添加到管道末端：Http编解码处理
        pipeline.addLast("httpServerCodec", new HttpServerCodec());
        //添加到管道末端：处理入站请求
        pipeline.addLast("httpServerHandler", new TestHttpServerHandler());
    }
}

/**
 * 入站请求处理
 */
class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    /**
     *读取入站信息
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        System.out.println(msg.getClass());
        System.out.println(ctx.channel().remoteAddress());

        if (msg instanceof HttpRequest) {

            HttpRequest httpRequest = (HttpRequest) msg;
            System.out.println("请示方法名:" + httpRequest.method().name());

            URI uri = new URI(httpRequest.uri());
            if ("/favicon.ico".equals(uri.getPath())) {
                System.out.println("请求favicon.ico");
                return;
            }
            //响应消息（数据）
            ByteBuf context = Unpooled.copiedBuffer("Message Form Server: hello word ", CharsetUtil.UTF_8);
            //Http响应对象
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, context);
            //Http头响应对象
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, context.readableBytes());
            //写入并刷缓冲
            ctx.writeAndFlush(response);
        }
    }
}