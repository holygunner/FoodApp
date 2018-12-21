package com.holygunner.cocktailsapp_test;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;

import com.holygunner.cocktailsapp_test.save.Saver;

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