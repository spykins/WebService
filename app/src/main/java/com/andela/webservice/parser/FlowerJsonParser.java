package com.andela.webservice.parser;

import com.andela.webservice.model.Flower;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Spykins on 08/06/16.
 */
public class FlowerJsonParser {

    public static List<Flower> parseFeed(String content) {
        List<Flower> flowerList = new ArrayList<>();
        try {
            JSONArray ar = new JSONArray(content);
            // we can't use for  each loop on JSonArray
            for(int i = 0; i < ar.length(); i++) {
                JSONObject obj = ar.getJSONObject(i);
                Flower flower = new Flower();
                flower.setName(obj.getString("name"));
                flower.setProductId(obj.getInt("productId"));
                flower.setCategory(obj.getString("category"));
                flower.setInstructions(obj.getString("instructions"));
                flower.setPhoto(obj.getString("photo"));
                flower.setPrice(obj.getDouble("price"));
                flowerList.add(flower);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return flowerList;
    }
}
