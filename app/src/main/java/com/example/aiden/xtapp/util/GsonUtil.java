package com.example.aiden.xtapp.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;

/**
 * @auther by yushilei.
 * @time 2016/11/1-17:02
 * @desc
 */

public class GsonUtil {
    private GsonUtil() {
    }

    public static <T> T jsonToBean(String jsonUser, Class<T> tClass) {
        Gson gson = new Gson();
        return gson.fromJson(jsonUser, tClass);
    }

    public static String beanToJson(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    public static <T> ArrayList<T> jsonArrayToList(String jsonStr,Class<T> tClass){
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonArray Jarray = parser.parse(jsonStr).getAsJsonArray();
        ArrayList<T> list = new ArrayList<T>();
        for(JsonElement obj : Jarray ){
            T cse = gson.fromJson( obj , tClass);
            list.add(cse);
        }
        return list;
    }
}
