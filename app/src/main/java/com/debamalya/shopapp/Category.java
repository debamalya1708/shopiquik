package com.debamalya.shopapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "category")
public class Category implements Serializable {

    @PrimaryKey()
    private int Id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo (name = "image")
    private String image;

    @ColumnInfo (name = "created_at")
    private String createdAt;

    public String getCreatedAt() {
        return createdAt;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Category(int id, String name, String image, String createdAt) {
        Id = id;
        this.name = name;
        this.image = image;
        this.createdAt = createdAt;
    }

    public Category() {
    }
}
