package com.holygunner.cocktailsapp_test;

import android.support.v4.app.Fragment;

public class SelectedMealsActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return SelectedMealsFragment.newInstance();
    }
}
