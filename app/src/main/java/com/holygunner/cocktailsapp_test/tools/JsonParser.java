package com.holygunner.cocktailsapp_test.tools;

import com.google.gson.Gson;
import com.holygunner.cocktailsapp_test.new_models.Cuisine;
import com.holygunner.cocktailsapp_test.new_models.Meal;

public class JsonParser {
    private Gson mGson;

    public JsonParser(){
        mGson = new Gson();
    }

    public Cuisine parseJsonToCuisine(String json){
        Cuisine cuisine = mGson.fromJson(json, Cuisine.class);
        return cuisine;
    }

    public String serializeMealToJsonBar(Meal meal){
        Cuisine cuisine = new Cuisine();
        cuisine.meals = new Meal[1];
        cuisine.meals[0] = meal;
        return mGson.toJson(cuisine);
    }
}
