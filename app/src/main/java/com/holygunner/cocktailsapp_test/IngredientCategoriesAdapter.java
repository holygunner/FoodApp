package com.holygunner.cocktailsapp_test;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.holygunner.cocktailsapp_test.new_models.Ingredient;
import com.holygunner.cocktailsapp_test.new_models.IngredientManager;
import com.holygunner.cocktailsapp_test.new_models.IngredientsCategory;
import com.holygunner.cocktailsapp_test.tools.IngredientItemHelper;
import com.holygunner.cocktailsapp_test.values.IngredientsCategoriesNames;

import java.util.List;
import java.util.Objects;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

public class IngredientCategoriesAdapter extends RecyclerView.Adapter<IngredientCategoriesAdapter
        .CategoryHolder> {
    private List<IngredientsCategory> mIngredientsCategories;
    private IngredientManager mIngredientManager;
    private RecyclerView.RecycledViewPool mRecycledViewPool;
    private SnapHelper mSnapHelper;
    private SelectIngredientsFragment mFragment;

    IngredientCategoriesAdapter(SelectIngredientsFragment fragment,
                                IngredientManager ingredientManager,
                                List<IngredientsCategory> ingredientsCategories){
        mFragment = fragment;
        mIngredientManager = ingredientManager;
        mIngredientsCategories = ingredientsCategories;
        mRecycledViewPool = new RecyclerView.RecycledViewPool();
        mRecycledViewPool.setMaxRecycledViews(R.id.ingredients_recyclerView,
                IngredientsCategoriesNames.CATEGORIES_NAMES.length);
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mFragment.getContext())
                .inflate(R.layout.ingredients_section_card_item, parent, false);
        mSnapHelper = new LinearSnapHelper();
        CategoryHolder holder = new CategoryHolder(view);
        holder.mRecyclerView.setRecycledViewPool(mRecycledViewPool);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
        final String categoryName = mIngredientsCategories.get(position).getCategoryName();
        List<Ingredient> singleCategoryIngredients
                = mIngredientsCategories.get(position).getIngredients();
        holder.categoryNameTextView.setText(categoryName);
        SelectIngredientsAdapter selectIngredientsAdapter
                = new SelectIngredientsAdapter(mFragment, singleCategoryIngredients,
                mIngredientManager);
        holder.mRecyclerView.setHasFixedSize(true);

        int orientation = mFragment.getResources().getConfiguration().orientation;

        RecyclerView.LayoutManager manager;

        if (orientation == ORIENTATION_PORTRAIT) {
            manager = new GridLayoutManager(mFragment.getActivity(),
                    IngredientItemHelper
                            .calculateNumbOfColumns(Objects.requireNonNull(mFragment.getContext())),
                    GridLayoutManager.HORIZONTAL, false);
            if (holder.mRecyclerView.getOnFlingListener() == null) {
                mSnapHelper.attachToRecyclerView(holder.mRecyclerView);
            }
        }   else {
            manager = new LinearLayoutManager(mFragment.getActivity(),
                    LinearLayoutManager.HORIZONTAL, false);
        }

        holder.mRecyclerView.setLayoutManager(manager);
        holder.mRecyclerView.setAdapter(selectIngredientsAdapter);
    }

    @Override
    public int getItemCount() {
        return mIngredientsCategories.size();
    }

    class CategoryHolder extends RecyclerView.ViewHolder{
        private TextView categoryNameTextView;
        private RecyclerView mRecyclerView;

        CategoryHolder(View itemView) {
            super(itemView);
            categoryNameTextView = itemView.findViewById(R.id.category_textView);
            mRecyclerView = itemView.findViewById(R.id.ingredients_recyclerView);
        }
    }
}
