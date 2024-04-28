package com.debamalya.shopapp;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;

import java.util.List;
import java.util.Optional;

@Dao
public interface MainDAO {
    @Insert(onConflict = REPLACE)
    void insert(Product product);

    @Delete
    void delete(Product product);

    @Delete
    void reset(List<Product> mainData);

//    @Query("UPDATE product SET text = :sText WHERE ID = :sID")
//    void update(int sID , String sText);


    @Query("SELECT * FROM product")
    List<Product> getAllProduct();

    @Query("SELECT * FROM product WHERE featured LIKE '%yes%' ")
    List<Product> getAllFeatureProduct();

    @Query("SELECT * FROM product WHERE category LIKE :value ")
    List<Product> getAllProductByCategory(String value);

    @Query("SELECT * FROM product WHERE brand LIKE :value ")
    List<Product> getAllProductByBrand(String value);

    @Query("SELECT * FROM product WHERE title LIKE :value OR description LIKE :value OR occasion LIKE :value")
    List<Product> searchProduct(String value);

    @Query("SELECT * FROM product WHERE id = :value ")
    Optional<Product> getProduct(int value);

//    @Query("SELECT * FROM product WHERE category = :category AND gender = :gender AND price->>'$.amazon' <= 6500 OR price->>'$.other'<= 6500")
//    List<Product> getAllFilterProducts(String category,String gender,String price);

    @Query("SELECT * FROM product where :column = :value")
    List<Product> getAllByColumnAndColumnValue(String column, String value);


    @Query("DELETE FROM product")
    void deleteAllData();
}
