package com.todo.nio.demo1;

import java.nio.ByteBuffer;

public class NioTest5 {

    public static void main(String[] args) {

        ByteBuffer buffer= ByteBuffer.allocate(64);

        buffer.putInt(15);
        buffer.putLong(50000000L);
        buffer.putDouble(12.456789);
        buffer.putChar('你');

        buffer.clear();

        //写入顺序与取出顺序需一致
        System.out.println(buffer.getInt());
        System.out.println(buffer.getLong());
        System.out.println(buffer.getDouble());
        System.out.println(buffer.getChar());
    }
}
