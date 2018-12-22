package com.holygunner.discover_meals.models;

import java.text.Collator;
import java.util.Comparator;

public class IngredientsComparator implements Comparator<Ingredient> {
    @Override
    public int compare(Ingredient ingr1, Ingredient ingr2) {
        return Collator.getInstance().compare(ingr1.getName(), ingr2.getName());
    }
}