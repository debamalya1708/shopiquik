package com.debamalya.shopapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.io.Serializable;

@Entity(tableName = "product")
//@TypeConverters({PriceTypeConverter.class,LinkTypeConverter.class})
public class Product implements Serializable {

    @PrimaryKey()
    private int Id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo (name = "description")
    private String description;

    @ColumnInfo(name = "price")
    private String price;

    @ColumnInfo (name = "link")
    private String link;

    @ColumnInfo(name = "country")
    private String country;

    @ColumnInfo (name = "images")
    private String images;

    @ColumnInfo(name = "category")
    private String category;

    @ColumnInfo (name = "subCategory")
    private String subCategory;

    @ColumnInfo(name = "featured")
    private String featured;

    @ColumnInfo (name = "rating")
    private String rating;

    @ColumnInfo(name = "occasion")
    private String occasion;

    @ColumnInfo (name = "brand")
    private String brand;

    @ColumnInfo (name = "created_at")
    private String createdAt;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getFeatured() {
        return featured;
    }

    public void setFeatured(String featured) {
        this.featured = featured;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getOccasion() {
        return occasion;
    }

    public void setOccasion(String occasion) {
        this.occasion = occasion;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Product(int id, String title, String description, String price, String link, String country, String images, String category, String subCategory, String featured, String rating, String occasion, String brand, String createdAt) {
        Id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.link = link;
        this.country = country;
        this.images = images;
        this.category = category;
        this.subCategory = subCategory;
        this.featured = featured;
        this.rating = rating;
        this.occasion = occasion;
        this.brand = brand;
        this.createdAt = createdAt;
    }

    public Product(){

    }
}
