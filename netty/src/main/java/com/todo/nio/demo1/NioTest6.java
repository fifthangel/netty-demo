package com.todo.nio.demo1;

import java.nio.ByteBuffer;

/**
 *@description: buffer 分片 buffer.slice() 共用原有buffer
 *
 *@author Sjh
 *@date 2019/6/25 9:23
 *@version 1.0.1
 */

public class NioTest6 {

    public static void main(String[] args) {

        ByteBuffer buffer = ByteBuffer.allocate(10);

        for (int i = 0; i < buffer.capacity(); i++) {
            buffer.put((byte) i);
        }

        buffer.position(2);
        buffer.limit(6);
        ByteBuffer sliceBuffer = buffer.slice();

        //修改分片的buffer
        for (int i = 0; i < sliceBuffer.capacity(); i++) {
            byte b = sliceBuffer.get(i);

            b *= 2;
            sliceBuffer.put(i, b);

        }

        buffer.position(0);
        buffer.limit(buffer.capacity());

        while (buffer.hasRemaining()){
            System.out.println(buffer.get());
        }


    }
}
