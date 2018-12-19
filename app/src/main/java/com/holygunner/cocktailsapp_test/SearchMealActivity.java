package com.holygunner.cocktailsapp_test;

import android.content.Intent;
import android.support.v4.app.Fragment;

public class SearchMealActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return SearchMealFragment.newInstance();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, SelectIngredientsActivity.class));
    }
}
