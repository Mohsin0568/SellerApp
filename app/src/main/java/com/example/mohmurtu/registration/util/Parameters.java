package com.example.mohmurtu.registration.util;

import org.json.JSONObject;

import java.net.URLEncoder;

/**
 * Created by mohmurtu on 9/24/2015.
 */
public class Parameters {

    public static String constructBodyParameters(String[] keys, Object[] values){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("?");
        for (int entry=0;entry<keys.length;entry++) {
            try{
                String key = URLEncoder.encode(keys[entry] == null ? "" : keys[entry], "UTF-8");
                Object value = URLEncoder.encode((String)values[entry] == null ? "" : (String)values[entry], "UTF-8");
                stringBuilder.append(key).append("=");
                stringBuilder.append(value).append("&");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return stringBuilder.toString();
    }

    public static String constructJsonParameters(String[] keys, Object[] values){
        JSONObject jsonObject = new JSONObject();

        for (int entry=0;entry<keys.length;entry++) {
            try{
                if(!keys[entry].equals("imagepath"))
                    jsonObject.put(keys[entry], values[entry]);
                else{

                    String key = URLEncoder.encode(keys[entry] == null ? "" : keys[entry], "UTF-8");
                    String value = (String)values[entry];
                    value = URLEncoder.encode(value == null ? "" :value, "UTF-8");
                    jsonObject.put(key, value);
                }

            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return jsonObject.toString();
    }
}
