package com.andela.webservice;


import org.json.JSONObject;

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

            //After setting the request mode but before writing the parameter

            JSONObject json = new JSONObject(p.getParams()); //we need to pass it an object that can be serialize
            // into json format and specifically use the Map of paramters, tha will be contained within the
            //request package
            /*
                    <?php
                             $json = $_POST["params"];
                             $params = json_decode($json);

                              echo "Parsed from JSON:\r\n";
                              foreach($params as $k => $v) {
                                echo "$k = $v\r\n";
                              }

                          ?>

             */
            //The php script is expecting a json of key type param

            String params = "params=" + json.toString();
            //now we have a value that we can send to the server
            if(p.getMethod().equals("POST")) {
                con.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
                writer.write(params); //instead of the encoded method, we write the params object
                //Now we are writing the params into the request body
                writer.flush();
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