package com.todo.netty.demo5;

import com.todo.netty.demo5.impl.PersonServiceImpl;
import com.todo.thrift.PersonService;

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
        //非阻塞socket连接
        TNonblockingServerSocket socket = new TNonblockingServerSocket(8899);

        THsHaServer.Args arg = new THsHaServer.Args(socket).minWorkerThreads(2).maxWorkerThreads(4);

        PersonService.Processor<PersonServiceImpl> processor = new PersonService.Processor<>(new PersonServiceImpl());

        //协议层对象：二进制压缩协议
        arg.protocolFactory(new TCompactProtocol.Factory());
        //传输层对象：网络传输
        arg.transportFactory(new TFastFramedTransport.Factory());
        arg.processorFactory(new TProcessorFactory(processor));

        //hs半同步 ha半异步 服务器
        TServer server =new THsHaServer(arg);

        System.out.println("Thrift Server Started");

        //死循环，异步非阻塞
        server.serve();

    }
}

