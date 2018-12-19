package com.holygunner.cocktailsapp_test;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.holygunner.cocktailsapp_test.save.Saver;

public class ChosenIngredientsActivity extends SingleFragmentActivity {
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