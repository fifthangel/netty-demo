package com.todo.netty.demo6.grpc;

import com.todo.netty.demo6.proto.MyRequest;
import com.todo.netty.demo6.proto.MyResponse;
import com.todo.netty.demo6.proto.StudentServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

/**
 *@description:Rpc Client 客户端
 *
 *@author Sjh
 *@date 2019/6/20 16:34
 *@version 1.0.1
 */
public class GrpcClient {

    public static void main(String[] args) {

        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 8899).usePlaintext(true).build();

        StudentServiceGrpc.StudentServiceBlockingStub blockingStub = StudentServiceGrpc.newBlockingStub(managedChannel);


        MyResponse myResponse = blockingStub.getRealNameByUserName(MyRequest.newBuilder().setUsername("zhangsan").build());

        System.out.println(myResponse.getRealname());

    }
}
