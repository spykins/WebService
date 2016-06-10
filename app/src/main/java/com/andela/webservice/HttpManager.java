package com.andela.webservice;


import android.util.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Spykins on 08/06/16.
 */
public class HttpManager {

    public static String getData(String uri) {

        BufferedReader reader = null;
        try{
            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            StringBuilder sb = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;
            while((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            return sb.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*
            //More on HTTPURLCONNECTION

        HttpURLConnection connection = null;
        String responseContent = null;
        StringBuilder builder = new StringBuilder();

        try {
            URL url = new URL(uri);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            builder.append(connection.getResponseCode())
                    .append(" ")
                    .append(connection.getResponseMessage())
                    .append("\n");

            //This gets the header and prints the content of the Header.....
            Map<String, List<String>> map = connection.getHeaderFields();
            for (Map.Entry<String, List<String>> entry : map.entrySet())
            {
                if (entry.getKey() == null)
                    continue;
                builder.append( entry.getKey())
                        .append(": ");

                List<String> headerValues = entry.getValue();
                Iterator<String> it = headerValues.iterator();
                if (it.hasNext()) {
                    builder.append(it.next());

                    while (it.hasNext()) {
                        builder.append(", ")
                                .append(it.next());
                    }
                }

                builder.append("\n");
            }

                   return connection.getInputStream().toString();
            //This returns the

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(connection != null) {
                connection.disconnect();
            }
        }

        return builder.toString();
        }
        */
        return null;
    }

    public static String getData(String uri, String userName, String password) {

        BufferedReader reader = null;
        byte[] loginByte = (userName + ":" + password).getBytes();
        //Now we have a byte Array represent the credentials..
        StringBuilder loginBuilder = new StringBuilder()
                .append("Basic ")
                .append(Base64.encodeToString(loginByte, Base64.DEFAULT));
        //Now the loginBuilder contains the credential, encoded and ready to send to the webServer

        try{
            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            /*
                Before connecting with the connection Object, we need to set a request property
                The header value... The header value will have a name of Authorisation and a value
                created by the loginBuilder object
             */

            con.addRequestProperty("AUTHORIZATION", loginBuilder.toString());
            //Now as long as we connect with the correct userName and password...
            // you should be able to connect and get data back

            StringBuilder sb = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;
            while((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            return sb.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
