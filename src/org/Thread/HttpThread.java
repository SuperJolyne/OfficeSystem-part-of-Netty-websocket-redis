package org.Thread;

import org.Http.MyHttpServer;

import java.io.IOException;

public class HttpThread extends Thread{

    public void run() {
        MyHttpServer myHttpServer = new MyHttpServer();
        try {
            myHttpServer.myHttpserverServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
