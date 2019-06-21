package com.todo.grpc.demo6.grpc;

import com.todo.grpc.demo6.proto.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.time.LocalDateTime;
import java.util.Iterator;

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

        //阻塞（同步）方式
        // StudentServiceGrpc.StudentServiceBlockingStub blockingStub = StudentServiceGrpc.newBlockingStub(managedChannel);
        //1.
//        MyResponse myResponse = blockingStub.getRealNameByUserName(MyRequest.newBuilder().setUsername("zhangsan").build());


        //2.
//        System.out.println(myResponse.getRealname() + "--------------------------------------");
//
//        //流式数据响应处理
//        Iterator<StudentResponse> iterator = blockingStub.getStudentsByAge(StudentRequest.newBuilder().setAge(20).build());
//
//        while (iterator.hasNext()) {
//            StudentResponse studentResponse = iterator.next();
//            System.out.println(studentResponse);
//        }

        //非阻塞（异步）方式
        StudentServiceGrpc.StudentServiceStub serviceStub = StudentServiceGrpc.newStub(managedChannel);

        //3.流式请求， 异步的
//        StreamObserver<StudentResponseList> studentResponseListStreamObserver = new StreamObserver<StudentResponseList>() {
//            @Override
//            public void onNext(StudentResponseList studentResponseList) {
//                studentResponseList.getStudentResponseList().forEach(studentResponse -> {
//                    System.out.println(studentResponse);
//                    System.out.println("**************");
//                });
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//                System.out.println(throwable.getMessage());
//            }
//
//            @Override
//            public void onCompleted() {
//                System.out.println("completed~!");
//            }
//        };
//
//        StreamObserver<StudentRequest> studentRequestStreamObserver = serviceStub.getStudentsWrapperByAge(studentResponseListStreamObserver);
//        studentRequestStreamObserver.onNext(StudentRequest.newBuilder().setAge(21).build());
//        studentRequestStreamObserver.onNext(StudentRequest.newBuilder().setAge(22).build());
//        studentRequestStreamObserver.onNext(StudentRequest.newBuilder().setAge(23).build());
//        studentRequestStreamObserver.onNext(StudentRequest.newBuilder().setAge(24).build());
//        studentResponseListStreamObserver.onCompleted();

        //4.双向流式
        StreamObserver<StreamRequest> requestStreamObserver = serviceStub.biTalk(new StreamObserver<StreamResponse>() {
            @Override
            public void onNext(StreamResponse streamResponse) {
                System.out.println(streamResponse.getResponseInfo());
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println(throwable.getMessage());

            }

            @Override
            public void onCompleted() {
                System.out.println("onCompledted");
            }
        });

        for (int i = 0; i < 10; i++) {
            requestStreamObserver.onNext(StreamRequest.newBuilder().setRequestInfo(LocalDateTime.now().toString()).build());
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
