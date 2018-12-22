package com.holygunner.discover_meals.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.holygunner.discover_meals.R;

public abstract class ToastBuilder {
    public static final int SHOW_TOAST_DELAY = 300;

    public static Toast getFailedConnectionToast(Context context){
        return buildToast(context, R.string.failed_connection);
    }

    public static Toast getMealAddedToast(Context context){
        return buildToast(context, R.string.like_button_pressed_ok);
    }

    public static Toast getMealRemovedToast(Context context){
        return buildToast(context, R.string.like_button_pressed_cancel);
    }

    public static Toast chosenIngrsListEmptyToast(Context context){
        return buildToast(context, R.string.chosen_ingredients_are_cleared);
    }

    public static Toast favMealsListEmptyToast(Context context){
        return buildToast(context, R.string.fav_meals_are_cleared);
    }

    static Toast noChosenIngrsToast(Context context){
        return buildToast(context, R.string.no_chosen_ingredients);
    }

    static Toast noFavMealsToast(Context context){
        return buildToast(context, R.string.no_fav_meals);
    }

    private static Toast buildToast(Context context, int resId){
        @SuppressLint("ShowToast") Toast toast = Toast.makeText(context,
                resId, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL,
                0, 0);
        return toast;
    }
}
