package org.Http;


import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.spi.HttpServerProvider;
import org.Thread.RedisThread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;

public class MyHttpServer {

    public void myHttpserverServer() throws IOException {

        HttpServerProvider provider = HttpServerProvider.provider();
        HttpServer server = provider.createHttpServer(new InetSocketAddress(7777),100);
        server.createContext("/heartBeat",new MyHttpHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("start");
    }

    static class MyHttpHandler implements HttpHandler{

        public void handle(HttpExchange httpExchange) throws IOException {
            InputStream in = httpExchange.getRequestBody();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String temp = reader.readLine();

            System.out.println(temp);

            if (temp.equals("rework")){
                new RedisThread().start();
            }

            httpExchange.sendResponseHeaders(200,0);
            httpExchange.close();
        }
    }
}
