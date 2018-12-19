package com.holygunner.cocktailsapp_test.new_models;

import java.text.Collator;
import java.util.Comparator;

public class IngredientsComparator implements Comparator<Ingredient> {
    @Override
    public int compare(Ingredient ingr1, Ingredient ingr2) {
        return Collator.getInstance().compare(ingr1.getName(), ingr2.getName());
    }
}