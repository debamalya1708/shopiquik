package com.debamalya.shopapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "favorite")
public class Favorite implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int Id;

    @ColumnInfo(name = "productId")
    private String productId;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Favorite(String productId) {
        this.productId = productId;
    }

    public Favorite() {
    }
}
