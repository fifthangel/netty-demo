package com.todo.nio.demo1;

import java.nio.ByteBuffer;

/**
 *@description:只读buffer
 *
 *@author Sjh
 *@date 2019/6/25 9:34
 *@version 1.0.1
 */

public class NioTest7 {

    public static void main(String[] args) {

        ByteBuffer buffer =ByteBuffer.allocate(10);

        for (int i =0; i<buffer.capacity();i++){
            buffer.put((byte)i);
        }

        ByteBuffer readOnlyBuffer = buffer.asReadOnlyBuffer();

        System.out.println(readOnlyBuffer.getClass());
    }
}
