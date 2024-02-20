package com.debamalya.shopapp;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CategoryDAO {
    @Insert(onConflict = REPLACE)
    void insert(Category category);

    @Delete
    void delete(Category category);

    @Delete
    void reset(List<Category> allCategory);

//    @Query("UPDATE product SET text = :sText WHERE ID = :sID")
//    void update(int sID , String sText);

    @Query("SELECT * FROM category")
    List<Category> getAllCategory();

    @Query("SELECT name FROM category")
    List<String> getAllCategoryName();


    @Query("SELECT * FROM category where :column = :value")
    List<Category> getAllByColumnAndColumnValue(String column, String value);

    @Query("DELETE FROM category")
    void deleteAllData();
}
