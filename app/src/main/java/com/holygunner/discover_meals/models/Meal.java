package com.holygunner.discover_meals.models;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;

public class Meal {
    @SerializedName("idMeal")
    private int id;
    @SerializedName("strMeal")
    private String name;
    @SerializedName("strCategory")
    private String category;
    @SerializedName("strArea")
    private String area;
    @SerializedName("strInstructions")
    private String instruction;
    @SerializedName("strMealThumb")
    private String urlImage;
    @SerializedName("strTags")
    private String tags;
    @SerializedName("strYoutube")
    private String youtubeUrl;

    private String strIngredient1;
    private String strIngredient2;
    private String strIngredient3;
    private String strIngredient4;
    private String strIngredient5;
    private String strIngredient6;
    private String strIngredient7;
    private String strIngredient8;
    private String strIngredient9;
    private String strIngredient10;
    private String strIngredient11;
    private String strIngredient12;
    private String strIngredient13;
    private String strIngredient14;
    private String strIngredient15;
    private String strIngredient16;
    private String strIngredient17;
    private String strIngredient18;
    private String strIngredient19;
    private String strIngredient20;

    private String strMeasure1;
    private String strMeasure2;
    private String strMeasure3;
    private String strMeasure4;
    private String strMeasure5;
    private String strMeasure6;
    private String strMeasure7;
    private String strMeasure8;
    private String strMeasure9;
    private String strMeasure10;
    private String strMeasure11;
    private String strMeasure12;
    private String strMeasure13;
    private String strMeasure14;
    private String strMeasure15;
    private String strMeasure16;
    private String strMeasure17;
    private String strMeasure18;
    private String strMeasure19;
    private String strMeasure20;

    private List<Ingredient> mIngredientsList;
    private List<Ingredient> mChosenIngredients;
    private boolean mIsFavourite;

    public Meal(){
        mChosenIngredients = new ArrayList<>();
    }

    private void initIngredients(){
        Ingredient[] ingredients = new Ingredient[20];
        ingredients[0] = new Ingredient(strIngredient1, strMeasure1);
        ingredients[1] = new Ingredient(strIngredient2, strMeasure2);
        ingredients[2] = new Ingredient(strIngredient3, strMeasure3);
        ingredients[3] = new Ingredient(strIngredient4, strMeasure4);
        ingredients[4] = new Ingredient(strIngredient5, strMeasure5);
        ingredients[5] = new Ingredient(strIngredient6, strMeasure6);
        ingredients[6] = new Ingredient(strIngredient7, strMeasure7);
        ingredients[7] = new Ingredient(strIngredient8, strMeasure8);
        ingredients[8] = new Ingredient(strIngredient9, strMeasure9);
        ingredients[9] = new Ingredient(strIngredient10, strMeasure10);
        ingredients[10] = new Ingredient(strIngredient11, strMeasure11);
        ingredients[11] = new Ingredient(strIngredient12, strMeasure12);
        ingredients[12] = new Ingredient(strIngredient13, strMeasure13);
        ingredients[13] = new Ingredient(strIngredient14, strMeasure14);
        ingredients[14] = new Ingredient(strIngredient15, strMeasure15);
        ingredients[15] = new Ingredient(strIngredient16, strMeasure16);
        ingredients[16] = new Ingredient(strIngredient17, strMeasure17);
        ingredients[17] = new Ingredient(strIngredient18, strMeasure18);
        ingredients[18] = new Ingredient(strIngredient19, strMeasure19);
        ingredients[19] = new Ingredient(strIngredient20, strMeasure20);

        mIngredientsList = IngredientManager.convertArrToIngredientList(ingredients);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getInstruction() {
        return instruction;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public List<Ingredient> getIngredientsList(){
        if (mIngredientsList == null){
            initIngredients();
        }
        return mIngredientsList;
    }

    @Contract(value = "null -> false", pure = true)
    @Override
    public boolean equals(Object object) {
        return object instanceof Meal && this.id == ((Meal) object).id;
    }

    public List<Ingredient> getChosenIngredients() {
        return mChosenIngredients;
    }

    public void addChosenIngredient(Ingredient ingredient){
        mChosenIngredients.add(ingredient);
    }
}
