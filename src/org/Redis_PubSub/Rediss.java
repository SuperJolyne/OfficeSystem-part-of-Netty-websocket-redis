package org.Redis_PubSub;

import redis.clients.jedis.Jedis;

public class Rediss {
    public static final String SERVER_ADDRESS = "127.0.0.1"; // 服务器地址
    public static final Integer SERVER_PORT = 6379; // 端口

    private Jedis jedis;

    public void init(){
        System.out.println("====init====");
        jedis = new Jedis(SERVER_ADDRESS,SERVER_PORT);
    }

    public void dis(){
        System.out.println("====dis====");
        jedis.disconnect();
    }

    public void testSub(){
        System.out.println("====testSub====");
        JedisSub listener = new JedisSub();
        jedis.subscribe(listener, "__keyevent@0__:expired");
    }

    public void testPub() {
        jedis.publish("aaaa","aaaaaaa");
    }
}
