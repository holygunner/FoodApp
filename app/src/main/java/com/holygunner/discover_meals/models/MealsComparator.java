package com.holygunner.discover_meals.models;

import java.util.Comparator;

public class MealsComparator implements Comparator<Meal> {
    @Override
    public int compare(Meal meal1, Meal meal2) {
        int sizeCompare = Integer.compare(meal2.getChosenIngredients().size(),
                meal1.getChosenIngredients().size());

        if (sizeCompare == 0){
            return (meal1.getName().compareTo(meal2.getName()));
        }   else
            return sizeCompare;
    }
}
