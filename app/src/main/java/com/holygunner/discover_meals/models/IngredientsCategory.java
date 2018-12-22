package com.holygunner.discover_meals.models;

import java.util.List;

public class IngredientsCategory {
    private String mCategoryName;
    private List<Ingredient> mIngredients;

    IngredientsCategory(String categoryName, List<Ingredient> ingredients){
        mCategoryName = categoryName;
        mIngredients = ingredients;
    }

    public String getCategoryName() {
        return mCategoryName;
    }

    public List<Ingredient> getIngredients() {
        return mIngredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        mIngredients = ingredients;
    }
}