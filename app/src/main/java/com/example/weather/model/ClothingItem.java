package com.example.weather.model;

public class ClothingItem {
    private String imageUri;
    private String category;
    private String subCategory;
    private boolean isFavorite;
    private boolean isDefault;
    private int imageResId;

    // 사용자 옷용
    // 기본 이미지용 생성자
    public ClothingItem(String category, String subCategory, int imageResId, boolean isDefault) {
        this.category = category;
        this.subCategory = subCategory;
        this.imageResId = imageResId;
        this.isDefault = isDefault;
        this.imageUri = null;
        this.isFavorite = false;
    }


    public ClothingItem(String imageUri, String category, String subCategory, boolean isFavorite) {
        this.imageUri = imageUri;
        this.category = category;
        this.subCategory = subCategory;
        this.isFavorite = isFavorite;
        this.isDefault = false;
    }

    // 기본 이미지용
    public ClothingItem(String category, String subCategory, String imageUri, boolean isDefault, int imageResId) {
        this.category = category;
        this.subCategory = subCategory;
        this.imageUri = imageUri;
        this.isDefault = isDefault;
        this.imageResId = imageResId;
        this.isFavorite = false;
    }
    public ClothingItem(String imageUri, String category, String subCategory) {
        this(imageUri, category, subCategory, false);
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

    public boolean isFavorite() {
        return isFavorite;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setFavorite(boolean favorite) {
        this.isFavorite = favorite;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }
}
