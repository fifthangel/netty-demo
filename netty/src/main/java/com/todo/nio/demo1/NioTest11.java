package com.todo.nio.demo1;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 *@description: buffer 的 Scattering 与Gathering
 *       客户端可以用telnet 访问
 *@author Sjh
 *@date 2019/6/25 13:52
 *@version 1.0.1
 */

public class NioTest11 {

    public static void main(String[] args) throws IOException {

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress socketAddress = new InetSocketAddress(8899);
        serverSocketChannel.socket().bind(socketAddress);

        int messageLength = 2 + 3 + 4;

        ByteBuffer[] byteBuffers = new ByteBuffer[3];

        byteBuffers[0] = ByteBuffer.allocate(2);
        byteBuffers[1] = ByteBuffer.allocate(3);
        byteBuffers[2] = ByteBuffer.allocate(4);

        SocketChannel socketChannel = serverSocketChannel.accept();

        while (true) {
            int bytesRead = 0;
            //一直在读，读
            while (bytesRead < messageLength) {
                long r = socketChannel.read(byteBuffers);
                bytesRead += r;
                System.out.println("bytesRead :" + bytesRead);
                Arrays.asList(byteBuffers).stream().forEach(buffer -> {
                    System.out.println("position :" + buffer.position() + ", limit " + buffer.limit());
                });
            }

            Arrays.asList(byteBuffers).forEach(buffer -> {
                buffer.flip();
                System.out.println("------------flip-----------position :" + buffer.position() + ", limit " + buffer.limit());
            });

            long bytesWritten = 0;
            while (bytesWritten < messageLength) {
                long r = socketChannel.write(byteBuffers);

                bytesWritten += r;
                System.out.println("bytesWritten :" + bytesWritten);
            }

            Arrays.asList(byteBuffers).forEach(buffer -> {
                buffer.clear();
                System.out.println("------------clear-----------position :" + buffer.position() + ", limit " + buffer.limit());
            });
            System.out.println("---------end-----------bytesRead:" + bytesRead + ",bytesWritten" + bytesWritten + ",messageLength:" + messageLength);

        }
    }
}
