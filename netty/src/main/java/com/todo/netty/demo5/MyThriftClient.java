package com.todo.netty.demo5;

import com.todo.netty.demo5.thrift.Person;
import com.todo.netty.demo5.thrift.PersonService;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

/**
 *@description:Thrift Rpc Client
 *
 *@param
 *@author Sjh
 *@date 2019/6/19 17:11
 *@return
 *@version 1.0.1
 */
public class MyThriftClient {

    public static void main(String[] args) {

        //传输层对象：网络传输
        TTransport transport = new TFramedTransport(new TSocket("localhost", 8899), 600);
        //协议层对象：二进制压缩协议
        TProtocol protocol = new TCompactProtocol(transport);

        PersonService.Client personClient = new PersonService.Client(protocol);

        try {
            transport.open();

            Person person = personClient.getPersonByUserName("张天");

            System.out.println(person);

            personClient.savePerson(person);

        } catch (Exception ex) {
            ex.getStackTrace();
        } finally {
            transport.close();
        }
    }
}
