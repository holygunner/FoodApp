package com.holygunner.cocktailsapp_test.save;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.holygunner.cocktailsapp_test.new_models.Cuisine;
import com.holygunner.cocktailsapp_test.new_models.Meal;

import org.jetbrains.annotations.Contract;

import java.util.HashSet;
import java.util.Set;

public class Saver {
    public static final String CHOSEN_INGREDIENTS_KEY = "chosen_ingredients_key";
    public static final String CHECKED_INGREDIENTS_KEY = "checked_ingredients_key";
    private static final String SELECTED_CUISINE_KEY = "selected_cuisine_key";
    private static final String FAV_MEALS_ID_SET_KEY = "fav_meals_id_set_key";

    public static Set<String> readFavouriteMealIdSet(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getStringSet(FAV_MEALS_ID_SET_KEY, new HashSet<String>());
    }

    public static boolean isMealFavourite(Context context, @NonNull Meal meal){
        String mealId = String.valueOf(meal.getId());

        return isMealFavourite(context, mealId);
    }

    public static Set<String> readFavouriteMealsJsons(Context context){
        Set<String> favMealsJsons = new HashSet<>();

        Set<String> favMealIdSet = readFavouriteMealIdSet(context);

        for (String mealId: favMealIdSet){
            favMealsJsons.add(PreferenceManager.getDefaultSharedPreferences(context)
                    .getString(mealId, ""));
        }

        return favMealsJsons;
    }

    public static void updFavouriteMealId(Context context, int id,
                                          boolean isFav, String mealJson){
        String mealId = String.valueOf(id);
        Set<String> favMealsIdSet = readFavouriteMealIdSet(context);

        if (isFav){
            saveFavouriteMeal(context, mealId, mealJson);
            favMealsIdSet.add(mealId);
        }   else {
            removeFavouriteMeal(context, mealId);
            favMealsIdSet.remove(mealId);
        }

        updFavouriteMealsIdSet(context, favMealsIdSet);
    }

    public static Set<String> readIngredients(Context context, String key){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getStringSet(key, new HashSet<String>());
    }

    public static void writeIngredients(Context context, Set<String> ingredients, String key){
        updIngredients(context, ingredients, key);
    }

    @Contract("_ -> !null")
    public static String readSelectedCuisine(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(SELECTED_CUISINE_KEY, "");
    }

    public static void writeSelectedCuisine(Context context, Cuisine selectedCuisine){
        Gson gson = new Gson();
        String jsonCuisine = gson.toJson(selectedCuisine);

        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(SELECTED_CUISINE_KEY, jsonCuisine)
                .apply();
    }

    public static Set<String> readChosenIngredientsNamesInLowerCase(Context context, String key){
        Set<String> originalNames = readIngredients(context, key);
        Set<String> lowerCaseNames = new HashSet<>();

        for (String name: originalNames){
            lowerCaseNames.add(name.toLowerCase());
        }

        return lowerCaseNames;
    }

    public static boolean isIngredientExists(Context context, String ingredientName){
        return readIngredients(context, CHOSEN_INGREDIENTS_KEY).contains(ingredientName);
    }

    public static boolean updChosenIngredient(Context context, String ingredientName){
        Set<String> savedNames = readIngredients(context, Saver.CHOSEN_INGREDIENTS_KEY);

        boolean result;

        if (savedNames.contains(ingredientName)){
            savedNames.remove(ingredientName);
            result = false;
        }   else {
            savedNames.add(ingredientName);
            result = true;
        }
        updIngredients(context, savedNames, CHOSEN_INGREDIENTS_KEY);
        return result;
    }

    private static void updFavouriteMealsIdSet(Context context,
                                               Set<String> updFavouriteMealIdSet){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putStringSet(FAV_MEALS_ID_SET_KEY, updFavouriteMealIdSet)
                .apply();
    }

    private static boolean isMealFavourite(Context context, @NonNull String drinkId){
        return readFavouriteMealIdSet(context).contains(drinkId);
    }

    private static void saveFavouriteMeal(Context context, String mealId, String mealJson){
        if (!isMealFavourite(context, mealId)){
            PreferenceManager.getDefaultSharedPreferences(context)
                    .edit()
                    .putString(mealId, mealJson)
                    .apply();
        }
    }

    private static void removeFavouriteMeal(Context context, String mealId){
        if (isMealFavourite(context, mealId)){
            PreferenceManager.getDefaultSharedPreferences(context)
                    .edit().remove(mealId)
                    .apply();
        }
    }

    private static void updIngredients(Context context, Set<String> savedNames, String key){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putStringSet(key, savedNames)
                .apply();
    }
}
