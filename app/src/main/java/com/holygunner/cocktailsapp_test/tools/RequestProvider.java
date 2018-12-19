package com.holygunner.cocktailsapp_test.tools;

import android.support.annotation.NonNull;

import com.holygunner.cocktailsapp_test.new_models.Cuisine;
import com.holygunner.cocktailsapp_test.new_models.Ingredient;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RequestProvider {
    private final static String WRONG_RESPONSE = "{\"drinks\":null}";
    private JsonParser mJsonParser;
    private OkHttpClient mHttpClient;

    public RequestProvider(){
        mJsonParser = new JsonParser();
        mHttpClient = new OkHttpClient();
    }

    public List<Cuisine> downloadCuisines(String... ingredients){
        List<Cuisine> downloadCuisines = new ArrayList<>();

        for (String checkedIngr : ingredients) {
            String url = URLBuilder.getCuisineByIngredientUrl(checkedIngr);
            String json = downloadJsonByRequest(url);
            Cuisine cuisine = mJsonParser.parseJsonToCuisine(json);

            if (cuisine != null && cuisine.meals != null) {
                if (cuisine.meals.length > 0) {
                    downloadCuisines.add(cuisine);

                    for (int j = 0; j < cuisine.meals.length; j++) {
                        cuisine.meals[j].addChosenIngredient(new Ingredient(checkedIngr));
                    }
                }
            }
        }
        return downloadCuisines;
    }

    public String downloadBarByDrinkName(String drinkName){
        if (isDrinkNameCorrect(drinkName)){
            String url = URLBuilder.getBarByDrinkNameUrl(drinkName);
            String response = downloadJsonByRequest(url);
            if (isResponseCorrect(response)){
                return response;
            }
        }   return null;
    }

    @Contract("null -> false")
    private boolean isDrinkNameCorrect(String drinkName){
        if (drinkName != null){
            return !drinkName.startsWith(" ") && !drinkName.equals("");
        }   else {
            return false;
        }
    }

    @Nullable
    public String downloadBarJsonById(Integer drinkId){
        if (drinkId != null){
            String url = URLBuilder.getMealDetailsUrl(drinkId);
            String jsonDrink = downloadJsonByRequest(url);

            if (!isResponseCorrect(jsonDrink)){
                return null;
            }   else {
                return jsonDrink;
            }
        }   else
            return null;
    }

    @NonNull
    public String downloadJsonByRequest(String url){
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        try {
            return mHttpClient.newCall(request).execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private boolean isResponseCorrect(@NonNull String response){
        return !response.equals("") && !response.equals(WRONG_RESPONSE);
    }
}