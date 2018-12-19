package com.holygunner.cocktailsapp_test.new_models;

import com.holygunner.cocktailsapp_test.tools.JsonParser;
import com.holygunner.cocktailsapp_test.save.Saver;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static com.holygunner.cocktailsapp_test.save.Saver.CHECKED_INGREDIENTS_KEY;
import static com.holygunner.cocktailsapp_test.save.Saver.CHOSEN_INGREDIENTS_KEY;

public class CuisineManager {
    private List<Meal> mSelectedMeals;
    private JsonParser mJsonParser;
    private Context mContext;

    public CuisineManager(Context context){
        mContext = context;
        mJsonParser = new JsonParser();
    }

    public Cuisine getSelectedCuisine(List<Cuisine> downloadCuisines){
        mSelectedMeals = new ArrayList<>();
        Cuisine deserializedCuisine = mJsonParser.parseJsonToCuisine(Saver
                .readSelectedCuisine(mContext));

        if (deserializedCuisine != null) {
            deserializedCuisine.meals = checkAndRemovePreviousMeals(deserializedCuisine).toArray(new Meal[0]);
            mSelectedMeals.addAll(Arrays.asList(deserializedCuisine.meals));
        }

        for (Cuisine cuisine: downloadCuisines){
            selectBars(cuisine);
        }

        Collections.sort(mSelectedMeals, new MealsComparator());
        Cuisine selectedCuisine = new Cuisine();
        Saver.writeIngredients(mContext,
                Saver.readIngredients(mContext, CHOSEN_INGREDIENTS_KEY),
                CHECKED_INGREDIENTS_KEY);
        selectedCuisine.meals = mSelectedMeals.toArray(new Meal[0]);
        Saver.writeSelectedCuisine(mContext, selectedCuisine);

        return selectedCuisine;
    }

    private void selectBars(Cuisine addedCuisine){
        if (addedCuisine != null) {
            Ingredient addedCuisineIngr = addedCuisine.meals[0].getChosenIngredients().get(0);
            List<Meal> addedBarList = new LinkedList<>(Arrays.asList(addedCuisine.meals));

            for (Meal existedDrink : mSelectedMeals){
                Iterator<Meal> iterator = addedBarList.listIterator();

                while (iterator.hasNext()) {
                    Meal matchedMeal = iterator.next();

                    if (existedDrink.equals(matchedMeal)){
                        if (!existedDrink.getChosenIngredients().contains(addedCuisineIngr)){
                            existedDrink.addChosenIngredient(addedCuisineIngr);
                        }
                        iterator.remove();
                    }

                }
            }
            mSelectedMeals.addAll(addedBarList);
        }
    }

    private List<Meal> checkAndRemovePreviousMeals(Cuisine checkedCuisine){
        Set<String> removedIngrs = IngredientManager.countRemovedIngredients(
                Saver.readIngredients(mContext, CHOSEN_INGREDIENTS_KEY),
                Saver.readIngredients(mContext, CHECKED_INGREDIENTS_KEY));

        List<Meal> updDrinks = new LinkedList<>();

        if (checkedCuisine != null) {
            updDrinks = new LinkedList<>(Arrays.asList(checkedCuisine.meals));

            for (String removedIngrName : removedIngrs) {
                Iterator<Meal> drinkIterator = updDrinks.listIterator();

                while (drinkIterator.hasNext()){
                    Meal meal = drinkIterator.next();
                    Iterator<Ingredient> ingredientIterator = meal.getChosenIngredients().listIterator();

                    while (ingredientIterator.hasNext()) {
                        Ingredient comparedIngr = ingredientIterator.next();
                        if (comparedIngr.getName().equals(removedIngrName)) {
                            ingredientIterator.remove();
                        }
                        if (meal.getChosenIngredients().size() == 0) {
                            drinkIterator.remove();
                        }
                    }
                }
            }
            checkedCuisine = new Cuisine();
            checkedCuisine.meals = updDrinks.toArray(new Meal[0]);
        }
        return updDrinks;
    }
}
