package com.example.weather.model;

public class ClothingItem {
    private String imageUri;
    private String category;
    private String subCategory;

    public ClothingItem(String imageUri, String category, String subCategory) {
        this.imageUri = imageUri;
        this.category = category;
        this.subCategory = subCategory;
    }

    public String getImageUri() {
        return imageUri;
    }

    public String getCategory() {
        return category;
    }

    public String getSubCategory() {
        return subCategory;
    }
}
