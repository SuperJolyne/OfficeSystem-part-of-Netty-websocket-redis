package org.Thread;

import org.Redis_PubSub.Rediss;

public class RedisThread extends Thread{

    public void run(){
        Rediss rediss = new Rediss();
        rediss.init();
        rediss.testSub();
    }
}
