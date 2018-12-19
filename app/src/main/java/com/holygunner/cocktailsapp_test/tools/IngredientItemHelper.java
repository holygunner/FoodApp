package com.holygunner.cocktailsapp_test.tools;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.TextView;

import com.holygunner.cocktailsapp_test.R;

public abstract class IngredientItemHelper {
    private static final int MATERIAL_COEFFICIENT = 128;

    public static void setFillToNameTextView(Context context,
                                             TextView ingredientNameTextView, boolean isFill){
        if (isFill){
            ingredientNameTextView.setTextColor(ContextCompat.getColor(context,
                    R.color.material_grey50));
            ingredientNameTextView
                    .setBackground(ContextCompat
                            .getDrawable(context, R.drawable.ingredient_name_fill));
        }   else {
            ingredientNameTextView.setTextColor(ContextCompat.getColor(context,
                    R.color.text_color));
            ingredientNameTextView.setBackgroundResource(0);
        }
    }

    public static void setColorFilterToImageView(Context context,
                                                 ImageView ingredientImageView, boolean isFill){
        if (isFill){
            ingredientImageView.setColorFilter(ContextCompat.getColor(context,
                    R.color.ingredient_color_fill));
        }   else {
            ingredientImageView.setColorFilter(null);
        }
    }

    public static int calculateNumbOfColumns(@NonNull Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return  (int) (dpWidth / MATERIAL_COEFFICIENT);
    }
}
