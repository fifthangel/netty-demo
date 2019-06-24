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
        IntBuffer buffer = IntBuffer.allocate(10);

        //buffer写入
        for (int i = 0; i < buffer.capacity(); i++) {
            int randomNumber = new SecureRandom().nextInt(20);
            buffer.put(randomNumber);
        }
        //读写反转
        buffer.flip();

        //buffer读取
        while (buffer.hasRemaining()) {
            System.out.println(buffer.get());
        }
    }
}
