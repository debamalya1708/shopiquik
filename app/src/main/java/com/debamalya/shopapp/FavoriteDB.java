package com.debamalya.shopapp;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Favorite.class},version = 2,exportSchema = false)

public abstract class FavoriteDB extends RoomDatabase {

    private static FavoriteDB database;

    private static String DATABASE_NAME = "favorite";

    public synchronized static FavoriteDB getInstance(Context context){
        if (database == null){

            database = Room.databaseBuilder(context.getApplicationContext(),
                    FavoriteDB.class , DATABASE_NAME).allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }
    public abstract FavoriteDAO favoriteDAO();

}