package com.debamalya.shopapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "sub_category")
public class SubCategory implements Serializable {

    @PrimaryKey()
    private int Id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo (name = "image")
    private String image;

    @ColumnInfo (name = "gender")
    private String gender;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @ColumnInfo (name = "category")
    private String category;

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

    public SubCategory(int id, String name, String image, String gender, String category, String createdAt) {
        Id = id;
        this.name = name;
        this.image = image;
        this.gender = gender;
        this.category = category;
        this.createdAt = createdAt;
    }

    public SubCategory() {
    }
}
