package com.todo.nio.demo1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 *@description:Nio Server
 *
 *@author Sjh
 *@date 2019/6/27 15:19
 *@version 1.0.1
 */
public class NioServer {

    //缓存注册进来的客户端
    private static Map<String, SocketChannel> clientChannelMap = new HashMap<>();

    public static void main(String[] args) throws Exception {

        //打开channel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //非阻塞
        serverSocketChannel.configureBlocking(false);
        //启动serverSocket,监听端口
        ServerSocket serverSocket = serverSocketChannel.socket();
        serverSocket.bind(new InetSocketAddress(8899));

        //打开selector
        Selector selector = Selector.open();
        //channel 注册 到 selectorc
        //此socketChannel 关注 连接事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            try {
                int select = selector.select();
                System.out.println("select number:" + select);

                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                System.out.println("selectionKeys:" + selectionKeys);

                selectionKeys.forEach(selectionKey -> {

                    final SocketChannel client;
                    try {
                        //新进来 的连接
                        if (selectionKey.isAcceptable()) {
                            //此socketChannel 读取事件
                            ServerSocketChannel server = (ServerSocketChannel) selectionKey.channel();
                            client = server.accept();
                            client.configureBlocking(false);
                            //注册读取 事件
                            client.register(selector, SelectionKey.OP_READ);

                            String key = "[" + UUID.randomUUID().toString() + "]";
                            clientChannelMap.put(key, client);
                        }//客户端新数据进来
                        else if (selectionKey.isReadable()) {
                            //强转，一定能拿到当前 client channel
                            client = (SocketChannel) selectionKey.channel();
                            ByteBuffer readBuffer = ByteBuffer.allocate(1024);

                            int count = client.read(readBuffer);
                            if (count > 0) {
                                readBuffer.flip();
                                Charset charset = Charset.forName("utf-8");
                                String receivedMessage = String.valueOf(charset.decode(readBuffer).array());
                                System.out.println(client + ":" + receivedMessage);

                                String senderKey = null;
                                for (Map.Entry<String, SocketChannel> entry : clientChannelMap.entrySet()) {
                                    if (client == entry.getValue()) {
                                        senderKey = entry.getKey();
                                        break;
                                    }
                                }
                                //广播
                                for (Map.Entry<String, SocketChannel> entry : clientChannelMap.entrySet()) {
                                    SocketChannel value = entry.getValue();

                                    ByteBuffer writeBuffer = ByteBuffer.allocate(1024);

                                    writeBuffer.put((senderKey + ":" + receivedMessage).getBytes());
                                    writeBuffer.flip();

                                    value.write(writeBuffer);
                                }


                            }
                        }
                        selectionKeys.clear();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }
}
