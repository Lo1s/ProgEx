package com.knabria.jiris.progex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by jiris on 27.09.2015.
 */
public class HttpManager {

    public static String getDataWithHttpURLConnection(String uri) {
        URL url;
        HttpURLConnection httpURLConnection;
        BufferedReader reader = null;

        try {
            url = new URL(uri);
            httpURLConnection = (HttpURLConnection) url.openConnection();

            StringBuilder builder = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line + "\n");
            }

            httpURLConnection.disconnect();
            return builder.toString();

        } catch (MalformedURLException urlEx) {
            urlEx.printStackTrace();
            return null;
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
