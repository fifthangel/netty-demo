package com.todo.nio.demo1;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;


/**
 *@description: Nio test out stream
 *
 * The essential properties of a buffer are its capacity, limit, and position:
 * Buffer.capacity   : Buffer.Capacity不会被修改，不会为负数
 * Buffer.limit      : Buffer.limit   永远不超过 capacity,不会为负数
 * Buffer.postion    : Buffer.postion 永远不超过 limit,不会为负数
 *
 * 0<= mark <= position<= limit <= capacity
 *@author Sjh
 *@date 2019/6/24 15:24
 *@version 1.0.1
 */

public class NioTest3 {

    public static void main(String[] args) throws Exception {


        FileOutputStream fileOutputStream = new FileOutputStream("F:\\todo-todo\\demo\\netty-demo\\netty\\src\\NioTest2.txt");

        FileChannel fileChannel = fileOutputStream.getChannel();

        //实例buffer  allocate 单例
        ByteBuffer byteBuffer = ByteBuffer.allocate(18);

        //Buffer.Capacity不会被修改，不会为负数
        System.out.println("Buffer.Capacity :" + byteBuffer.capacity());


        byte[] messages = "hello word ! nihao".getBytes();

        for (int i = 0; i < messages.length; i++) {
            System.out.println("Buffer.limit :" + byteBuffer.limit());
            System.out.println("Buffer.postion :" + byteBuffer.position());
            byteBuffer.put(messages[i]);
        }
        System.out.println("-----------------------Buffer.flip ------------------------------------"  );
        byteBuffer.flip();// 修改limit=position 后 postion=0 和
        System.out.println("Buffer.limit :" + byteBuffer.limit());
        System.out.println("Buffer.postion :" + byteBuffer.position());

        //用channel 写
        fileChannel.write(byteBuffer);

        fileOutputStream.close();

    }
}
