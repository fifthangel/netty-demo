package com.todo.nio.demo1;

import java.nio.IntBuffer;
import java.security.SecureRandom;

/**
 *@description: Nio test
 *
 *  nio：selector  channel buffer
 *  channel 双向的，通过channel操作流，而io中流是单向的
 *  buffer 读写数据
 *
 *@author Sjh
 *@date 2019/6/24 14:23
 *@version 1.0.1
 */
public class NioTest1 {
    public static void main(String[] args) {

        //begin with jdk 1.4
//        The new buffer's position will be zero, its limit will be its capacity, its mark will be undefined, and each of its elements will be initialized to zero.
//        It will have a  array backing array  and its array offset  will be zero.
        IntBuffer buffer = IntBuffer.allocate(10);

        System.out.println("Buffer.Capacity :" + buffer.capacity());

        //buffer写入
        for (int i = 0; i < 5; i++) {
            int randomNumber = new SecureRandom().nextInt(20);
            buffer.put(randomNumber);
        }

        System.out.println("before flip Buffer.limit :" + buffer.limit());
        System.out.println("---------------------------flip------------------------------:" + buffer.limit());

        //读写反转
        //      in.read(buf);      // Read data into rest of buffer
        //       buf.flip();        // Flip buffer
        //      out.write(buf);    // Write header + data to channel
        buffer.flip();

        System.out.println("Buffer.limit :" + buffer.limit());

        //buffer读取
        while (buffer.hasRemaining()) {
            System.out.println("Buffer.Capacity :" + buffer.capacity());
            System.out.println("Buffer.limit :" + buffer.limit());
            System.out.println("Buffer.postion :" + buffer.position());
            System.out.println(buffer.get());
        }
    }
}
