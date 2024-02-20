package com.debamalya.shopapp;

import androidx.room.TypeConverter;

import com.google.gson.Gson;

public class LinkTypeConverter {
    private static Gson gson = new Gson();

    @TypeConverter
    public static Link fromJson(String value) {
        return gson.fromJson(value, Link.class);
    }

    @TypeConverter
    public static String toJson(Link link) {
        return gson.toJson(link);
    }

}
