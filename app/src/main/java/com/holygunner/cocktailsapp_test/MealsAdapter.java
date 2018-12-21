package com.holygunner.cocktailsapp_test;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.holygunner.cocktailsapp_test.new_models.Ingredient;
import com.holygunner.cocktailsapp_test.new_models.Meal;
import com.holygunner.cocktailsapp_test.save.Saver;
import com.holygunner.cocktailsapp_test.tools.ImageHelper;
import com.holygunner.cocktailsapp_test.tools.JsonParser;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.holygunner.cocktailsapp_test.values.BundleKeys.MEAL_ID_KEY;
import static com.holygunner.cocktailsapp_test.values.BundleKeys.MEAL_JSON_KEY;

public class MealsAdapter extends RecyclerView.Adapter<MealsAdapter.MealsHolder> {
    private static final int VISIBLE_INGREDIENTS_LIMIT = 10;
    private Context mContext;
    private List<Meal> mMeals;
    private JsonParser mJsonParser;

    MealsAdapter(Context context, List<Meal> drinks){
        mContext = context;
        mMeals = drinks;
        mJsonParser = new JsonParser();
    }

    @NonNull
    @Override
    public MealsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.drink_card_item, parent, false);
        return new MealsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealsHolder holder, int position) {
        String drinkPosition = position + 1 + "";
        holder.drinkPositionTextView.setText(drinkPosition);
        holder.bindMeal(mMeals.get(position));
    }

    @Override
    public int getItemCount() {
        return mMeals.size();
    }

    protected class MealsHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private Meal mMeal;
        private TextView drinkNameTextView;
        private TextView mealIngredientsTextView;
        private TextView drinkPositionTextView;
        private ImageView drinkImageView;
        private View mHeartImageViewContainer;

        MealsHolder(View itemView) {
            super(itemView);
            drinkNameTextView = itemView.findViewById(R.id.drink_name_TextView);
            mealIngredientsTextView
                    = itemView.findViewById(R.id.drink_ingredients_textView);
            drinkPositionTextView = itemView.findViewById(R.id.drink_position);
            drinkImageView = itemView.findViewById(R.id.drink_imageView);
            mHeartImageViewContainer = itemView.findViewById(R.id.is_drink_liked_container);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, MealRecipeActivity.class);
            intent.putExtra(MEAL_ID_KEY, mMeal.getId());
            if (mMeal.getIngredientsList().size() > 0){
                intent.putExtra(MEAL_JSON_KEY,
                        mJsonParser.serializeMealToJsonBar(mMeal));
            }
            mContext.startActivity(intent);
        }

        void bindMeal(Meal meal){
            mMeal = meal;
            setIsFavourite(Saver.isMealFavourite(mContext, mMeal));
            drinkImageView.setTag(ImageHelper.downloadImage(meal.getUrlImage(), drinkImageView));
            drinkNameTextView.setText(meal.getName());
            setMealChosenIngredientsTextView(meal);
        }

        private void setIsFavourite(boolean isFav){
            if (isFav){
                mHeartImageViewContainer.setVisibility(View.VISIBLE);
            }   else {
                mHeartImageViewContainer.setVisibility(View.GONE);
            }
        }

        private void setMealChosenIngredientsTextView(@NotNull Meal meal){
            StringBuilder text = new StringBuilder();

            List<Ingredient> ingredients;

            if (meal.getChosenIngredients().size() > 0){
                ingredients = meal.getChosenIngredients();
            }   else {
                ingredients = meal.getIngredientsList();
            }

            boolean isLimit = false;

            if (ingredients.size() > VISIBLE_INGREDIENTS_LIMIT){
                ingredients = ingredients.subList(0, VISIBLE_INGREDIENTS_LIMIT - 1);
                isLimit = true;
            }
            for (Ingredient ingredient: ingredients){
                text.append(ingredient.getName()).append(", ");
            }
            text.delete(text.length()-2, text.length()-1);
            if (isLimit){
                text.deleteCharAt(text.length()-1);
                text.append("...");
            }

            mealIngredientsTextView.setText(text);
        }
    }
}
