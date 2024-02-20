package com.debamalya.shopapp;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BrandDAO {
    @Insert(onConflict = REPLACE)
    void insert(Brand brand);

    @Delete
    void delete(Brand brand);

    @Delete
    void reset(List<Brand> allBrand);

//    @Query("UPDATE product SET text = :sText WHERE ID = :sID")
//    void update(int sID , String sText);

    @Query("SELECT * FROM brand")
    List<Brand> getAllBrand();

    @Query("SELECT name FROM brand")
    List<String> getAllBrandName();


    @Query("SELECT * FROM brand where :column = :value")
    List<Brand> getAllByColumnAndColumnValue(String column, String value);

    @Query("DELETE FROM brand")
    void deleteAllData();
}
