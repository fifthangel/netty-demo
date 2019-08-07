package com.todo.nio.demo1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/*
 *@description:Nio  多端口监听 服务端
 *
 *@author Sjh
 *@date 2019/6/26 17:37
 *@version 1.0.1
 */

public class NioTest12 {

    public static void main(String[] args) throws IOException {

        int[] ports = new int[5];

        ports[0] = 2001;
        ports[1] = 2002;
        ports[2] = 2003;
        ports[3] = 2004;
        ports[4] = 2005;

        Selector selector = Selector.open();


        for (int i = 0; i < ports.length; i++) {
            //每个端口开启一个通道,open  SelectorProvider.provider().openServerSocketChannel();
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            //非阻塞
            serverSocketChannel.configureBlocking(false);

            //开启socket通信
            ServerSocket serverSocket = serverSocketChannel.socket();
            InetSocketAddress address = new InetSocketAddress(ports[i]);
            //监听端口
            serverSocket.bind(address);

            System.out.println("监听端口:" + ports[i]);

            //channel注册 到  selector 上
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        }


        while (true) {
            int numbers = selector.select();
            System.out.println(" selector.select() numbers" + numbers);
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            System.out.println("selectionkeys " + selectionKeys);
            Iterator<SelectionKey> iterator = selectionKeys.iterator();

            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                if (selectionKey.isAcceptable()) {
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();

                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    iterator.remove();

                    System.out.println("获得客户端连接:" + socketChannel);
                } else if (selectionKey.isReadable()) {

                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    int bytesRead = 0;


                    while (true) {
                        ByteBuffer byteBuffer = ByteBuffer.allocate(512);

                        byteBuffer.clear();

                        int read = socketChannel.read(byteBuffer);

                        byteBuffer.flip();

                        socketChannel.write(byteBuffer);

                        bytesRead += read;

                        System.out.println("读取： " + bytesRead + ",来自于:" + socketChannel);

                        iterator.remove();
                    }

                }
            }
        }


    }
}
