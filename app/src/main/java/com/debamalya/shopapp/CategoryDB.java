package com.debamalya.shopapp;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Category.class},version = 1,exportSchema = false)

public abstract class CategoryDB extends RoomDatabase {

    private static CategoryDB database;

    private static String DATABASE_NAME = "category";

    public synchronized static CategoryDB getInstance(Context context){
        if (database == null){

            database = Room.databaseBuilder(context.getApplicationContext(),
                    CategoryDB.class , DATABASE_NAME).allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }
    public abstract CategoryDAO categoryDAO();

}