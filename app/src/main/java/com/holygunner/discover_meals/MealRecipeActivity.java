package com.holygunner.discover_meals;

import android.support.v4.app.Fragment;

public class MealRecipeActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return MealRecipeFragment.newInstance();
    }
}
