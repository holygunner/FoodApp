package com.holygunner.discover_meals;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.holygunner.discover_meals.save.Saver;

public class ChosenIngredientsActivity extends SingleFragmentActivity {
    private static final int CLOSE_ACTIVITY_DELAY = 1000;

    @Override
    protected Fragment createFragment() {
        return ChosenIngredientsFragment.newInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkIsListNotEmpty();
    }

    private void checkIsListNotEmpty(){
        if (Saver.readIngredients(this, Saver.CHOSEN_INGREDIENTS_KEY).size() == 0){
            onBackPressed();
        }
    }
}