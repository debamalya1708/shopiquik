package com.debamalya.shopapp;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SubCategoryDAO {
    @Insert(onConflict = REPLACE)
    void insert(SubCategory subCategory);

    @Delete
    void delete(SubCategory subCategory);

    @Delete
    void reset(List<SubCategory> allSubCategory);

//    @Query("UPDATE product SET text = :sText WHERE ID = :sID")
//    void update(int sID , String sText);

    @Query("SELECT * FROM sub_category")
    List<SubCategory> getAllSubCategory();

    @Query("SELECT * FROM sub_category where category like :value")
    List<SubCategory> getAllSubCategoryName(String value);


    @Query("SELECT * FROM sub_category where :column = :value")
    List<SubCategory> getAllByColumnAndColumnValue(String column, String value);

    @Query("DELETE FROM sub_category")
    void deleteAllData();
}
