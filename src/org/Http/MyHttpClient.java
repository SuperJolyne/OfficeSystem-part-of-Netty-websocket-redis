package org.Http;

import java.net.HttpURLConnection;
import java.net.URL;

public class MyHttpClient {

    public void startWork()  {
        URL url = null;
        try {
            url = new URL("http://127.0.0.1:7778/heartBeat");
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            if(urlConn.getResponseCode()==200){
                urlConn.disconnect();
                }
            } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
