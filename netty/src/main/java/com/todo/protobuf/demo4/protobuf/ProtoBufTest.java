package com.todo.protobuf.demo4.protobuf;

/**
 *@description:protobuf 为Rpc 【跨语言】提供【文件序列化反序列化】网络传输 核心代码
 *
 *@author Sjh
 *@date 2019/6/18 16:51
 *@version 1.0.1
 */
public class ProtoBufTest {


    public static void main(String[] args) throws Exception {

        //builder
        MyDataInfo.Person Person = MyDataInfo.Person.newBuilder().setName("Zahng san")
                                                                .setAge(20)
                                                                .setAddress("北京").build();
        //转换
        byte[] student2ByteArray = Person.toByteArray();

        MyDataInfo.Person person2 = MyDataInfo.Person.parseFrom(student2ByteArray);

        System.out.println(person2);
        System.out.println(person2.getName());
        System.out.println(person2.getAge());
        System.out.println(person2.getAddress());
    }
}
