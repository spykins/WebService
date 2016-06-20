package com.andela.webservice;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Spykins on 08/06/16.
 */
public class HttpManager {

    public static String getData(RequestPackage p) {

        BufferedReader reader = null;
        String uri = p.getUri();
        //We need to append parameter only when we are making a get request
        //if it's any other request, we will use the parameter in a different way

        if(p.getMethod().equals("GET")) {
            uri += "?" + p.getEncodedParams();
        }

        try {
            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(p.getMethod()); //This is value, GET, or POST
            //**** Note*** set the request method before writing the body
            if(p.getMethod().equals("POST")) {
                con.setDoOutput(true);
                // This allows to output content to the body of the request
                OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
                //creating a write object that can write to the output stream and send information to the connection
                writer.write(p.getEncodedParams()); // This gets the same param as the Get, with the & and the = sign
                //all in the same place and the value encoded to send over the web... but instead of appending it to d uri
                //we will place it in the body of the request
                writer.flush(); //This flush anything written to the memory
            }
            StringBuilder sb = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }

    }

}