package com.todo.nio.demo1;

import java.io.IOException;
import java.nio.channels.Selector;

public class NioTest12 {

    public static void main(String[] args) throws IOException {

        int[] ports = new int[5];

        ports[0]=2001;
        ports[1]=2002;
        ports[2]=2003;
        ports[3]=2004;
        ports[4]=2005;

        Selector selector=Selector.open();



        selector.close();

    }
}
