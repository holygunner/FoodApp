package com.holygunner.cocktailsapp_test.new_models;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.Contract;

public class Ingredient {

    @SerializedName("strIngredient")
    private String mName;
    private String mCategory;
    private boolean mIsFill;
    private String mMeasure;
    @SerializedName("idIngredient")
    private String id;
    @SerializedName("strDescription")
    private String description;
    @SerializedName("strType")
    private String type;

    public Ingredient(){}

    public Ingredient(String ingredientName, String ingredientMeasure){
        this.mName = ingredientName;
        this.mMeasure = ingredientMeasure;
    }

    public Ingredient(String ingredientName){
        this.mName = ingredientName;
    }

    public String getName() {
        return mName;
    }

    public String getMeasure() {
        return mMeasure;
    }

    @Contract(value = "null -> false", pure = true)
    @Override
    public boolean equals(Object ingredient) {
        return ingredient instanceof Ingredient
                && this.mName.toLowerCase().equals(((Ingredient) ingredient).mName.toLowerCase());
    }

    public String getCategory() {
        return mCategory;
    }

    public boolean isFill() {
        return mIsFill;
    }

    public void setFill(boolean fill) {
        mIsFill = fill;
    }

    public void setCategory(String category) {
        this.mCategory = category;
    }
}
