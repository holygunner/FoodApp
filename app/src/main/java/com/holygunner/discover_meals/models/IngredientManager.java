package com.holygunner.discover_meals.models;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.holygunner.discover_meals.values.IngredientsCategoriesNames.CATEGORIES_NAMES;

public class IngredientManager {
    private AssetManager mAssetManager;

    public IngredientManager(@NotNull Context context){
        mAssetManager = context.getAssets();
    }

    public List<Ingredient> chosenNameToIngredientsList(Set<String> chosenNames){
        List<Ingredient> chosenIngredientsList = new LinkedList<>();

        for (String name : chosenNames){
            Ingredient ingredient = new Ingredient(name);
            chosenIngredientsList.add(ingredient);
        }
        return chosenIngredientsList;
    }

    public List<IngredientsCategory> getAllIngredients(){
        List<IngredientsCategory> allIngredients = new ArrayList<>();

        for (String category: CATEGORIES_NAMES){
            allIngredients.add(new IngredientsCategory(category, getIngredientsOfCategory(category)));
        }
        return allIngredients;
    }

    public static Set<String> countAddedIngredients(Set<String> userChosenIngrs,
                                               @NotNull Set<String> checkedIngrs) {
        return countChangedIngredients(userChosenIngrs, checkedIngrs, false);
    }

    static Set<String> countRemovedIngredients(Set<String> userChosenIngrs,
                                               @NotNull Set<String> checkedIngrs) {
        return countChangedIngredients(userChosenIngrs, checkedIngrs, true);
    }

    public Drawable getIngredientDrawable(String folderName, String fileName){
        try {
            InputStream inputStream = mAssetManager.open(getRightFileName(folderName, fileName));
            return Drawable.createFromStream(inputStream, null);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void bindIngredientWithImageView(ImageView imageView, String fileName){
        String folderName = findIngredientCategory(fileName);
        String path = getPath(fileName, folderName);
        Picasso.get()
                .load(path)
                .into(imageView);
    }

    public void bindIngredientWithImageView(ImageView imageView, String fileName, String category){
        String path = getPath(fileName, category);
        Picasso.get()
                .load(path)
                .priority(Picasso.Priority.HIGH)
//                .resize(175, 175)
                .into(imageView);
    }

    @NonNull
    @Contract(pure = true)
    private String getPath(String fileName, String folderName){
        return "file:///android_asset/" + folderName + "/" + fileName + ".png";
    }

    public String findIngredientCategory(String fileName){
        String ingredientCategory = "";
        try {
            for (String category: CATEGORIES_NAMES){
                if (Arrays.asList(Objects.requireNonNull(mAssetManager.list(category)))
                        .contains(fileName + ".png")){
                    ingredientCategory = category;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ingredientCategory;
    }

    public static boolean ingredientMeasureVerification(@NotNull String measure){
        return !measure.equals("\n")
                && !measure.equals(" ") && !measure.equals("");
    }

    public static List<Ingredient> convertArrToIngredientList(@NotNull Ingredient[] ingredients){
        List<Ingredient> ingredientList = new ArrayList<>();
        for (Ingredient ingredient: ingredients){
            if (ingredient.getName() != null){
                if (!ingredient.getName().equals("")) {
                    ingredientList.add(ingredient);
                }
            }   else {
                break;
            }
        }
        return ingredientList;
    }

    private static Set<String> countChangedIngredients(Set<String> ingrs1, Set<String> ingrs2,
                                                       boolean isInvertCompare){
        Set<String> returnIngrs = new HashSet<>();

        if (isInvertCompare){
            Set<String> temp = ingrs1;
            ingrs1 = ingrs2;
            ingrs2 = temp;
        }

        for (String ingr: ingrs1){
            if (!ingrs2.contains(ingr)) {
                returnIngrs.add(ingr);
            }
        }
        return returnIngrs;
    }

    @Nullable
    private List<Ingredient> getIngredientsOfCategory(String category){
        try {
            String[] names = mAssetManager.list(category);
            assert names != null;
            return namesToIngredients(names, category);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @NonNull
    @Contract(pure = true)
    private String getRightFileName(String folderName, String fileName){
        return folderName + "/" + fileName + ".png";
    }

    private List<Ingredient> setTitleElemFirstIfExists(@NotNull List<Ingredient> ingredients, String category){
        int indx = 0;
        for (int i = 0; i < ingredients.size(); i++){
            if (ingredients.get(i).getName().equals(category)){
                indx = i;
                break;
            }
        }
        if (indx != 0){
            Ingredient titleIngredient = ingredients.get(indx);
            ingredients.remove(indx);
            ingredients.add(0, titleIngredient);
        }

        return ingredients;
    }

    private List<Ingredient> namesToIngredients(@NotNull String[] fileNames, String category){
        List<Ingredient> ingredients = new LinkedList<>();
        for (String name: fileNames){
            name = name.replace(".png", "");
            Ingredient ingredient = new Ingredient(name);
            ingredient.setCategory(category);
            ingredients.add(ingredient);
        }
        ingredients = setTitleElemFirstIfExists(ingredients, category);
        return ingredients;
    }
}
