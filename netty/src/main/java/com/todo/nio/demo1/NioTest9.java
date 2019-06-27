package com.todo.nio.demo1;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;


/**
 *@description:堆外内存
 *
 *@author Sjh
 *@date 2019/6/25 13:45
 *@version 1.0.1
 */

public class NioTest9 {

    public static void main(String[] args) throws IOException {

        RandomAccessFile randomAccessFile = new RandomAccessFile("NioTest9.txt","rw");

        FileChannel fileChannel =randomAccessFile.getChannel();

        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE,0,5);

        mappedByteBuffer.put(0,(byte)'a');
        mappedByteBuffer.put(0,(byte)'b');

        randomAccessFile.close();

    }
}
