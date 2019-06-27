package com.todo.thrift.demo5.impl;

import com.todo.thrift.demo5.thrift.DataException;
import com.todo.thrift.demo5.thrift.Person;
import com.todo.thrift.demo5.thrift.PersonService;
import org.apache.thrift.TException;

/**
 *@description:实现thrift生成的服务的接口(业务处理)
 *
 *@param
 *@author Sjh
 *@date 2019/6/19 16:55
 *@return
 *@version 1.0.1
 */
public class PersonServiceImpl implements PersonService.Iface {
    @Override
    public Person getPersonByUserName(String username) throws DataException, TException {
        System.out.println("get client param :" + username);
        Person person = new Person();
        person.setUsername("张天");
        person.setAge(20);
        person.setMarried(false);

        return person;
    }

    @Override
    public void savePerson(Person person) throws DataException, TException {

        System.out.println("svaePerson:" + person);
    }
}
