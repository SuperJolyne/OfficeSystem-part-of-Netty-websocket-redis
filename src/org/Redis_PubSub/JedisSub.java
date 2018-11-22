package org.Redis_PubSub;

import org.Http.MyHttpClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import static org.Handler.SocketHandler.sendAllMessage;
import static org.Handler.SocketHandler.sendMessage;
import static org.Redis_PubSub.Rediss.SERVER_ADDRESS;
import static org.Redis_PubSub.Rediss.SERVER_PORT;

public class JedisSub extends JedisPubSub {

    Jedis jedis = new Jedis(SERVER_ADDRESS,SERVER_PORT);

    MyHttpClient httpClient = new MyHttpClient();

    @Override
    public void onMessage(String channel, String message) {
        String[] strings = message.split(":");
        //单个学生,socket key为1,2非01,02
        if (strings[0].equals("key")){
            sendMessage(strings[1]);
        }
        //所有学生
        if (strings[0].equals("order")){
            sendAllMessage(strings[1]);
        }
        //心跳
        if (strings[0].equals("heart")){
            httpClient.startWork();
        }
    }
}
