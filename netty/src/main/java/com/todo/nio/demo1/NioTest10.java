package com.todo.nio.demo1;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/**
 *@description:文件锁
 *
 *@author Sjh
 *@date 2019/6/25 13:45
 *@version 1.0.1
 */

public class NioTest10 {

    public static void main(String[] args) throws IOException {

        RandomAccessFile randomAccessFile = new RandomAccessFile("NioTest10.txt", "rw");

        FileChannel fileChannel = randomAccessFile.getChannel();

        FileLock fileLock = fileChannel.lock(3, 6, true);

        System.out.println("valid : " + fileLock.isValid());
        System.out.println("lock type :" + fileLock.isShared());

        fileLock.release();
        randomAccessFile.close();

    }
}
