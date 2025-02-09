package com.debamalya.shopapp;

import java.util.List;
import java.util.Optional;

public class MainDAOImpl implements MainDAO{
    @Override
    public void insert(Product product) {

    }

    @Override
    public void delete(Product product) {

    }

    @Override
    public void reset(List<Product> mainData) {

    }

    @Override
    public List<Product> getAllProduct() {
        return null;
    }

    @Override
    public List<Product> getAllFeatureProduct() {
        return null;
    }

    @Override
    public List<Product> getAllProductByCategory(String value) {
        return null;
    }

    @Override
    public List<Product> getAllProductBySubCategory(String value, String category) {
        return null;
    }

    @Override
    public List<Product> getAllProductByCategoryAndGender(String value, String gender) {
        return null;
    }

    @Override
    public List<Product> getAllProductByBrand(String value) {
        return null;
    }

    @Override
    public List<Product> searchProduct(String query) {
        return null;
    }

    @Override
    public Optional<Product> getProduct(String value) {
        return Optional.empty();
    }

    @Override
    public List<Product> getAllByColumnAndColumnValue(String column, String value) {
        return null;
    }

    @Override
    public void deleteAllData() {

    }
}
