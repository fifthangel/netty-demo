package com.todo.nio.demo1;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NioTest3 {

    public static void main(String[] args) throws Exception {


        FileOutputStream fileOutputStream = new FileOutputStream("F:\\todo-todo\\demo\\netty-demo\\netty\\src\\NioTest2.txt");

        FileChannel fileChannel = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(512);

        byte[] messages = "hello word ! nihao".getBytes();

        for (int i = 0; i < messages.length; i++) {
            byteBuffer.put(messages[i]);
        }

        byteBuffer.flip();

        //用channel 写
        fileChannel.write(byteBuffer);

        fileOutputStream.close();

    }
}
