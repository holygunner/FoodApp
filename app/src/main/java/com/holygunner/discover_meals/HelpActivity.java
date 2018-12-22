package com.holygunner.discover_meals;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.holygunner.discover_meals.tools.ToolbarHelper;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_activity_layout);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        ToolbarHelper.setToolbar(toolbar, this,
                ToolbarHelper.UP_BUTTON);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}