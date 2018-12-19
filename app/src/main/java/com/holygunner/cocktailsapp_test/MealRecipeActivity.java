package com.holygunner.cocktailsapp_test;

import android.support.v4.app.Fragment;

public class MealRecipeActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return MealRecipeFragment.newInstance();
    }
}
