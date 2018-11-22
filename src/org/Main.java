package org;

import org.Thread.HttpThread;
import org.Thread.RedisThread;


public class Main {
    public static void main(String[] args) {

        new HttpThread().start();

        new RedisThread().start();

        new Netty().run();
    }
}
