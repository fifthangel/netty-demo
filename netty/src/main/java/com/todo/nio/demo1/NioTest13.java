package com.todo.nio.demo1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

/**
 *@description:编解码
 *
 *@author Sjh
 *@date 2019/6/27 15:19
 *@version 1.0.1
 */

public class NioTest13 {

    public static void main(String[] args) throws IOException {
        String inputFile = "F:\\todo-todo\\demo\\netty-demo\\netty\\src\\NioTest13_In.txt";
        String outputFile = "F:\\todo-todo\\demo\\netty-demo\\netty\\src\\NioTest13_Out.txt";

        RandomAccessFile inputRandomAccessFile = new RandomAccessFile(inputFile, "r");
        RandomAccessFile outputRandomAccessFile = new RandomAccessFile(outputFile, "rw");

        long inputLength = new File(inputFile).length();

        FileChannel inputFileChannel = inputRandomAccessFile.getChannel();
        FileChannel outPutFileChannel = outputRandomAccessFile.getChannel();

        MappedByteBuffer inputData = inputFileChannel.map(FileChannel.MapMode.READ_ONLY, 0, inputLength);

        System.out.println("========格式列表===========");
        Charset.availableCharsets().forEach((k, v) -> {
            System.out.println(k + "v" + v);
        });
        System.out.println("===================");

//        Charset charset = Charset.forName("utf-8");
        Charset charset = Charset.forName("iso-8859-1");
        CharsetDecoder decoder = charset.newDecoder();
        CharsetEncoder encoder = charset.newEncoder();

        CharBuffer charBuffer = decoder.decode(inputData);
        ByteBuffer outputData = encoder.encode(charBuffer);

        outPutFileChannel.write(outputData);

        inputRandomAccessFile.close();
        outputRandomAccessFile.close();


    }


}

