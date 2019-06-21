package com.todo.com.todo.thrift.demo5;

import com.todo.com.todo.thrift.demo5.impl.PersonServiceImpl;
import com.todo.com.todo.thrift.demo5.thrift.PersonService;

import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TFastFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;

/**
 *@description:thrift Rpc 服务端
 *
 *@param
 *@author Sjh
 *@date 2019/6/18 13:58
 *@return
 *@version 1.0.1
 */

public class MyThriftServer {

    public static void main(String[] args) throws Exception {
        //非阻塞socket连接（TSimpleServer 单线程,TThreadPollServer 多线程阻塞IO ,TNonBlockingServer 多线程非阻塞IO ,THsHaServer 线程池异步IO同步处理）
        TNonblockingServerSocket socket = new TNonblockingServerSocket(8899);
        //hs半同步 ha半异步 服务器通信协议 socket 通信
        THsHaServer.Args arg = new THsHaServer.Args(socket).minWorkerThreads(2).maxWorkerThreads(4);

        PersonService.Processor<PersonServiceImpl> processor = new PersonService.Processor<>(new PersonServiceImpl());

        //协议层对象：二进制压缩格式（二进制-常用、压缩二进制-常用、Json格式、只写Json格式、调试/文本格式）
        arg.protocolFactory(new TCompactProtocol.Factory());
        //传输层对象：网络传输（TSocket-阻塞式、TFramedTransport-非阻塞、TFileTransport、xxxMerry...、xxxxZlib...）
        arg.transportFactory(new TFastFramedTransport.Factory());
        //处理层对象
        arg.processorFactory(new TProcessorFactory(processor));

        //hs半同步 ha半异步 线程池服务器
        TServer server =new THsHaServer(arg);

        System.out.println("Thrift Server Started");

        //死循环，异步非阻塞
        server.serve();

    }
}

