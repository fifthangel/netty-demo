package com.todo.netty.demo6.grpc.impl;

import com.todo.netty.demo6.proto.MyRequest;
import com.todo.netty.demo6.proto.MyResponse;
import com.todo.netty.demo6.proto.StudentServiceGrpc.StudentServiceImplBase;
import io.grpc.stub.StreamObserver;


/**
 *@description:自定义处理类
 *
 *@author Sjh
 *@date 2019/6/20 16:18
 *@version 1.0.1
 */
public class StudentServiceImpl extends StudentServiceImplBase {

    @Override
    public void getRealNameByUserName(MyRequest request, StreamObserver<MyResponse> responseObserver) {
        System.out.println("接受到客户端信息: " + request.getUsername());

        responseObserver.onNext(MyResponse.newBuilder().setRealname("zhangsan").build());
        responseObserver.onCompleted();

    }

}
