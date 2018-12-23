package com.holygunner.discover_meals.tools;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import com.holygunner.discover_meals.AboutActivity;
import com.holygunner.discover_meals.ChosenIngredientsActivity;
import com.holygunner.discover_meals.FavouriteMealsActivity;
import com.holygunner.discover_meals.HelpActivity;
import com.holygunner.discover_meals.R;
import com.holygunner.discover_meals.SearchMealActivity;
import com.holygunner.discover_meals.SelectIngredientsActivity;
import com.holygunner.discover_meals.save.Saver;

public class DrawerMenuManager {

    private static final int START_ACTIVITY_DELAY = 300;

    public void setNavigationMenu(final FragmentActivity activity, final DrawerLayout drawerLayout,
                                         @NonNull final NavigationView navigationView,
                                         final int currentItemId){
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        menuItem.setChecked(true);

                        if (menuItem.getItemId() != currentItemId) {
                            Context context = activity.getBaseContext();
                            Intent intent = null;

                            switch (menuItem.getItemId()) {
                                case R.id.select_ingredients:
                                    intent = new Intent(context,
                                            SelectIngredientsActivity.class);
                                    break;
                                case R.id.chosen_ingredients:
                                    if (isChosenIngrsAvailable(context)) {
                                        intent = new Intent(context,
                                                ChosenIngredientsActivity.class);
                                    }   else {
                                        menuItem.setChecked(false);
                                        drawerLayout.closeDrawers();
                                        return false;
                                    }
                                    break;
                                case R.id.search_meal:
                                    intent = new Intent(context,
                                            SearchMealActivity.class);
                                    break;
                                case R.id.favourite_meals:
                                    if (isFavMealsExist(context)) {
                                        intent = new Intent(context,
                                                FavouriteMealsActivity.class);
                                    }   else {
                                        menuItem.setChecked(false);
                                        drawerLayout.closeDrawers();
                                        return false;
                                    }
                                    break;
                                case R.id.help:
                                    intent = new Intent(context,
                                            HelpActivity.class);
                                    break;
                                case R.id.about:
                                    intent = new Intent(context,
                                            AboutActivity.class);
//                                    intent = new Intent(context,
//                                            TestCallbackPatternActivity.class);
                                    break;
                            }
                            if (intent != null) {
                                final Intent finalIntent = intent;
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        activity.startActivity(finalIntent);
                                    }
                                }, START_ACTIVITY_DELAY);
                            }
                            drawerLayout.closeDrawers();
                        }
                        return true;
                    }
                });
    }

    private boolean isChosenIngrsAvailable(Context context){
        boolean result = Saver.readIngredients(context, Saver.CHOSEN_INGREDIENTS_KEY).size() > 0;

        if (result){
            return true;
        }   else {
            ToastBuilder.noChosenIngrsToast(context).show();
            return false;
        }
    }

    private boolean isFavMealsExist(Context context){
        boolean result = Saver.readFavouriteMealIdSet(context).size() > 0;

        if (result){
            return true;
        }   else {
            ToastBuilder.noFavMealsToast(context).show();
            return false;
        }
    }
}
