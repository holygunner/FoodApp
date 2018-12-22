package com.holygunner.discover_meals.tools;

import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.holygunner.discover_meals.R;

import org.jetbrains.annotations.NotNull;

public abstract class ToolbarHelper {
    public static final String UP_BUTTON = "up_button";
    public static final String MENU_BUTTON = "menu_button";

    public static void setToolbar(@NotNull android.support.v7.widget.Toolbar toolbar,
                                  @NotNull final AppCompatActivity activity, @NonNull String key){
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        assert actionBar != null;

        switch (key) {
            case UP_BUTTON:
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);
                actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
                break;
            case MENU_BUTTON:
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
                break;
            default:
                throw new IllegalArgumentException("Wrong key");
        }
    }
}
