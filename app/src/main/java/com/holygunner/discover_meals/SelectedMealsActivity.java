package com.holygunner.discover_meals;

import android.support.v4.app.Fragment;

public class SelectedMealsActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return SelectedMealsFragment.newInstance();
    }
}
