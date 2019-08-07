package com.todo.nio.demo1;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 *@description:buffer 直接 堆外内存，零拷贝
 *
 *@author Sjh
 *@date 2019/6/25 10:56
 *@version 1.0.1
 */

public class NioTest8 {


    public static void main(String[] args) throws IOException {


        FileInputStream inputStream = new FileInputStream("input2.txt");
        FileOutputStream outputStream = new FileOutputStream("output2.txt");


        FileChannel inputChannel = inputStream.getChannel();
        FileChannel outputChannel = outputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(512);

        while (true) {
            byteBuffer.clear();

            int read = inputChannel.read(byteBuffer);

            if (-1 == read) {
                break;
            }

            byteBuffer.flip();

            outputChannel.write(byteBuffer);

        }

        inputChannel.close();
        outputChannel.close();


    }
}
