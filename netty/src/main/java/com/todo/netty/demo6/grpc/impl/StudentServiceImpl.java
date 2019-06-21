package com.todo.netty.demo6.grpc.impl;

import com.todo.netty.demo6.proto.*;
import com.todo.netty.demo6.proto.StudentServiceGrpc.StudentServiceImplBase;
import io.grpc.stub.StreamObserver;

import java.util.UUID;


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
        System.out.println("getRealNameByUserName接受到客户端信息: " + request.getUsername());

        responseObserver.onNext(MyResponse.newBuilder().setRealname("zhangsan").build());
        responseObserver.onCompleted();

    }

    @Override
    public void getStudentsByAge(StudentRequest request, StreamObserver<StudentResponse> responseObserver) {

        System.out.println("getStudentsByAge接受到客户端信息: " + request.getAge());

        //流式数据响应
        responseObserver.onNext(StudentResponse.newBuilder().setName("zhangsan1").setAge(12).setCity("bj").build());
        responseObserver.onNext(StudentResponse.newBuilder().setName("zhangsan2").setAge(32).setCity("cd").build());
        responseObserver.onNext(StudentResponse.newBuilder().setName("zhangsan3").setAge(13).setCity("sc").build());
        responseObserver.onNext(StudentResponse.newBuilder().setName("zhangsan4").setAge(22).setCity("nj").build());
        responseObserver.onNext(StudentResponse.newBuilder().setName("zhangsan5").setAge(42).setCity("sh").build());
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<StudentRequest> getStudentsWrapperByAge(StreamObserver<StudentResponseList> responseObserver) {

        return new StreamObserver<StudentRequest>() {
            @Override
            public void onNext(StudentRequest studentRequest) {
                System.out.println("onNext:" + studentRequest.getAge());
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println(throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                StudentResponse studentResponse1 = StudentResponse.newBuilder().setName("zhangsan1").setAge(31).setCity("a").build();
                StudentResponse studentResponse2 = StudentResponse.newBuilder().setName("zhangsan2").setAge(32).setCity("b").build();
                StudentResponse studentResponse3 = StudentResponse.newBuilder().setName("zhangsan3").setAge(33).setCity("c").build();
                StudentResponse studentResponse4 = StudentResponse.newBuilder().setName("zhangsan4").setAge(34).setCity("d").build();
                StudentResponse studentResponse5 = StudentResponse.newBuilder().setName("zhangsan5").setAge(35).setCity("e").build();

                StudentResponseList studentResponseList = StudentResponseList.newBuilder().addStudentResponse(studentResponse1)
                        .addStudentResponse(studentResponse2)
                        .addStudentResponse(studentResponse3)
                        .addStudentResponse(studentResponse4)
                        .addStudentResponse(studentResponse5).build();

                responseObserver.onNext(studentResponseList);
                responseObserver.onCompleted();

            }
        };

    }


    @Override
    public StreamObserver<StreamRequest> biTalk(StreamObserver<StreamResponse> responseObserver) {
        return new StreamObserver<StreamRequest>() {
            @Override
            public void onNext(StreamRequest streamRequest) {
                System.out.println(streamRequest);
                responseObserver.onNext(StreamResponse.newBuilder().setResponseInfo(UUID.randomUUID().toString()).build());
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println(throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }
}
