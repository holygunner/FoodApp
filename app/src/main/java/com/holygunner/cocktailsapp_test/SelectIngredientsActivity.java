package com.holygunner.cocktailsapp_test;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class SelectIngredientsActivity extends SingleFragmentActivity {
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    @Override
    protected Fragment createFragment() {
        return SelectIngredientsFragment.newInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
