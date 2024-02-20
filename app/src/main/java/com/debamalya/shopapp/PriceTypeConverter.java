package com.debamalya.shopapp;

import androidx.room.TypeConverter;

import com.google.gson.Gson;

public class PriceTypeConverter {
    private static Gson gson = new Gson();

    @TypeConverter
    public static Price fromJson(String value) {
        return gson.fromJson(value, Price.class);
    }

    @TypeConverter
    public static String toJson(Price price) {
        return gson.toJson(price);
    }

}
