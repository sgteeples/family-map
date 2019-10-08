package edu.byu.cs240.FamilyMap.utils;

import com.google.gson.Gson;

public class Serializer {

    public String serialize(Object obj) {
        return new Gson().toJson(obj);
    }
}
