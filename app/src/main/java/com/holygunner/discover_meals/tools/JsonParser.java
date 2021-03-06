package com.holygunner.discover_meals.tools;

import com.google.gson.Gson;
import com.holygunner.discover_meals.models.Cuisine;
import com.holygunner.discover_meals.models.Meal;

public class JsonParser {
    private Gson mGson;

    public JsonParser(){
        mGson = new Gson();
    }

    public Cuisine parseJsonToCuisine(String json){
        return mGson.fromJson(json, Cuisine.class);
    }

    public String serializeMealToJsonBar(Meal meal){
        Cuisine cuisine = new Cuisine();
        cuisine.meals = new Meal[1];
        cuisine.meals[0] = meal;
        return mGson.toJson(cuisine);
    }
}
