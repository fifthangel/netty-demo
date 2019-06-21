package com.todo.netty.demo6.grpc;

import com.todo.netty.demo6.grpc.impl.StudentServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 *@description:Rpc Server  服务端
 *
 *@author Sjh
 *@date 2019/6/20 16:29
 *@version 1.0.1
 */

public class GrpcService {

    private Server server;

    public static void main(String[] args) throws Exception {

        GrpcService service = new GrpcService();
        service.start();
        service.awaitTermination();
    }


    private void start() throws IOException {
        this.server = ServerBuilder.forPort(8899).addService(new StudentServiceImpl()).build().start();

        System.out.println("server started");

        //回调钩子: jvm进程退出前执行
        Runtime.getRuntime().addShutdownHook(new Thread(()->{//初始化但尚未运行的线程
            System.out.println("关闭Jvm");
            GrpcService.this.stop();
        }));

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

    private void stop() {
        if (null != this.server) {
            this.server.shutdown();

        }
    }

    //需要阻塞住
    private void awaitTermination() throws Exception {
        if (null != this.server) {
            this.server.awaitTermination();
        }
    }
}
