package com.debamalya.shopapp;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavoriteDAO {
    @Insert(onConflict = REPLACE)
    void insert(Favorite favorite);

    @Delete
    void delete(Favorite favorite);

    @Delete
    void reset(List<Favorite> allFavorites);

    @Query("SELECT productId FROM favorite")
    List<String> getAllFavorites();

    @Query("SELECT * FROM favorite where productId = :id")
    Favorite getFavoriteByProductId(String id);


}
