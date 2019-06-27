package com.todo.nio.demo1;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 *@description:
 *
 *@author Sjh
 *@date 2019/6/24 16:49
 *@version 1.0.1
 */
public class NioTest4 {

    public static void main(String[] args) throws IOException {

        FileInputStream inputStream = new FileInputStream("input.txt");
        FileOutputStream outputStream = new FileOutputStream("output.txt");


        FileChannel inputChannel = inputStream.getChannel();
        FileChannel outputChannel = outputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(512);

        while (true) {
             byteBuffer.clear();
            System.out.println("---------------------clear--------------------------- :");
            System.out.println("Buffer.limit :" + byteBuffer.limit());
            System.out.println("Buffer.postion :" + byteBuffer.position());

            int read = inputChannel.read(byteBuffer);
            System.out.println("---------------------read--------------------------- :");
            System.out.println("Buffer.limit :" + byteBuffer.limit());
            System.out.println("Buffer.postion :" + byteBuffer.position());
            System.out.println("read---------|" + read);
            if (-1 == read) {
                break;
            }

            byteBuffer.flip();
            System.out.println("---------------------flip--------------------------- :");
            System.out.println("Buffer.limit :" + byteBuffer.limit());
            System.out.println("Buffer.postion :" + byteBuffer.position());
            outputChannel.write(byteBuffer);

        }

        inputChannel.close();
        outputChannel.close();


    }
}
