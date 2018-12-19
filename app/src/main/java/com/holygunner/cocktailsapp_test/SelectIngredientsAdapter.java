package com.holygunner.cocktailsapp_test;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.holygunner.cocktailsapp_test.new_models.Ingredient;
import com.holygunner.cocktailsapp_test.new_models.IngredientManager;
import com.holygunner.cocktailsapp_test.save.Saver;
import com.holygunner.cocktailsapp_test.tools.IngredientItemHelper;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SelectIngredientsAdapter
        extends RecyclerView.Adapter<SelectIngredientsAdapter.IngredientHolder> {
    private Context mContext;
    private SelectIngredientsFragment mFragment;
    private List<Ingredient> mIngredients;
    private IngredientManager mIngredientManager;

    SelectIngredientsAdapter(@NotNull SelectIngredientsFragment fragment,
                             List<Ingredient> ingredients,
                             IngredientManager ingredientManager){
        mFragment = fragment;
        mContext = fragment.getContext();
        mIngredients = ingredients;
        mIngredientManager = ingredientManager;
    }

    @NonNull
    @Override
    public IngredientHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_item,
                parent, false);
        return new IngredientHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientHolder holder, int position) {
        Ingredient item = mIngredients.get(position);
        holder.bind(item);

    }

    @Override
    public int getItemCount() {
        return mIngredients.size();
    }

    protected class IngredientHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{
        private TextView ingredientNameTextView;
        private ImageView ingredientImageView;
        private Ingredient mIngredient;

        IngredientHolder(View itemView) {
            super(itemView);
            ingredientNameTextView = itemView.findViewById(R.id.ingredientTextView);
            ingredientImageView = itemView.findViewById(R.id.ingredientImageView);
            ingredientNameTextView.setOnClickListener(this);
            ingredientImageView.setOnClickListener(this);
        }

        void bind(Ingredient ingredient){
            mIngredient = ingredient;
            ingredientNameTextView.setText(ingredient.getName());
//            ingredientImageView.setImageDrawable(mIngredientManager
//                            .getIngredientDrawable(ingredient.getCategory(),
//                    ingredient.getName()));

            mIngredientManager.bindIngredientWithImageView(ingredientImageView, mIngredient.getName(),
                    mIngredient.getCategory());

            boolean isFill = Saver.isIngredientExists(mContext, ingredient.getName());
            IngredientItemHelper.setColorFilterToImageView(mContext, ingredientImageView, isFill);
            IngredientItemHelper.setFillToNameTextView(mContext, ingredientNameTextView, isFill);
        }

        @Override
        public void onClick(View v) {
            boolean isFill = Saver.updChosenIngredient(mContext, mIngredient.getName());
            IngredientItemHelper.setColorFilterToImageView(mContext, ingredientImageView, isFill);
            IngredientItemHelper.setFillToNameTextView(mContext, ingredientNameTextView, isFill);
            mFragment.setMixButtonVisibility();
        }
    }
}
