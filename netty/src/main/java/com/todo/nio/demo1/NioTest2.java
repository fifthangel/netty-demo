package com.todo.nio.demo1;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;


/**
 *@description:Nioo test input stream
 *
 *@author Sjh
 *@date 2019/6/24 15:06
 *@version 1.0.1
 */

public class NioTest2 {

    public static void main(String[] args) throws Exception {

        FileInputStream fileInputStream = new FileInputStream("F:\\todo-todo\\demo\\netty-demo\\netty\\src\\NioTest2.txt");

        FileChannel fileChannel =fileInputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
        //用channel 读
        fileChannel.read(byteBuffer);

        byteBuffer.flip();

        while (byteBuffer.remaining()>0){
            byte b=byteBuffer.get();
            System.out.println("Charcter: "+(char)b);
        }

        fileInputStream.close();
    }
}
