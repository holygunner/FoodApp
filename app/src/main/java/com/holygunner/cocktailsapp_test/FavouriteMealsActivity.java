package com.holygunner.cocktailsapp_test;

import android.content.Intent;
import android.support.v4.app.Fragment;

public class FavouriteMealsActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return FavouriteMealsFragment.newInstance();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, SelectIngredientsActivity.class));
    }
}
