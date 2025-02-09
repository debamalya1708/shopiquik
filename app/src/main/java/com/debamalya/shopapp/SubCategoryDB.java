package com.debamalya.shopapp;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {SubCategory.class},version = 1,exportSchema = false)

public abstract class SubCategoryDB extends RoomDatabase {

    private static SubCategoryDB database;

    private static String DATABASE_NAME = "sub_category";

    public synchronized static SubCategoryDB getInstance(Context context){
        if (database == null){

            database = Room.databaseBuilder(context.getApplicationContext(),
                    SubCategoryDB.class , DATABASE_NAME).allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }
    public abstract SubCategoryDAO subCategoryDAO();

}