package com.andela.webservice;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Spykins on 15/06/16.
 */
public class RequestPackage {
    private String uri;
    private String method = "GET";
    private Map<String, String> params = new HashMap<>();

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    //This allows us to add parameter to the HashMap
    public void setParam(String key, String value) {
        params.put(key, value);
    }
    //we need to be able to receive the parameter as an encoded String

    public String getEncodedParams() {
        StringBuilder sb = new StringBuilder();
        for(String key : params.keySet()) {
            String value = null;
            try {
                value = URLEncoder.encode(params.get(key), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if(sb.length() > 0) {
                sb.append("&");
            }

            sb.append(key + "=");
            //encode the value
            sb.append(value);
        }
        return sb.toString();
    }

}
