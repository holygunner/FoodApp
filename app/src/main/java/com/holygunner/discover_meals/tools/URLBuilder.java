package com.holygunner.discover_meals.tools;

import android.support.annotation.NonNull;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public abstract class URLBuilder {
    private static final String SERVICE_URL_BASE = "https://www.themealdb.com/api/json/v1/";
    private static final String PERSONAL_API_KEY = "23543";
    private static final String GET_BY_INGR
            = "/filter.php?i=";
    private static final String GET_BY_DRINK_NAME
            = "/search.php?s=";
    private static final String GET_BY_DRINK_ID
            = "/lookup.php?i=";

    @NonNull
    @Contract(pure = true)
    public static String getMissedIngredientUrl(String name){
        return "https://www.themealdb.com/images/ingredients/" + name + ".png";
    }

    @NotNull
    static String getCuisineByIngredientUrl(String ingredientName){
        return buildUrl(GET_BY_INGR, underscoresToSpacesIfRequired(ingredientName));
    }

    @Contract(pure = true)
    @NonNull
    static String getBarByDrinkNameUrl(String drinkName){
        return buildUrl(GET_BY_DRINK_NAME, drinkName);
    }

    @NonNull
    @Contract(pure = true)
    static String getMealDetailsUrl(int id){
        return buildUrl(GET_BY_DRINK_ID, String.valueOf(id));
    }

    @NonNull
    @Contract(pure = true)
    private static String buildUrl(String requestType, String request){
        return SERVICE_URL_BASE + PERSONAL_API_KEY + requestType
                + request;
    }

    @NonNull
    private static String underscoresToSpacesIfRequired(@NotNull String name){
        return name.replace(" ", "_");
    }
}
