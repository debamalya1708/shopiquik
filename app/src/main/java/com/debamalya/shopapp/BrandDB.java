package com.debamalya.shopapp;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Brand.class},version = 1,exportSchema = false)

public abstract class BrandDB extends RoomDatabase {

    private static BrandDB database;

    private static String DATABASE_NAME = "brand";

    public synchronized static BrandDB getInstance(Context context){
        if (database == null){

            database = Room.databaseBuilder(context.getApplicationContext(),
                    BrandDB.class , DATABASE_NAME).allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }
    public abstract BrandDAO brandDAO();

}