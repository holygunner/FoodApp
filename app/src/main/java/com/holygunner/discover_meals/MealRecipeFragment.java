package com.holygunner.discover_meals;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.holygunner.discover_meals.models.Ingredient;
import com.holygunner.discover_meals.models.IngredientManager;
import com.holygunner.discover_meals.models.Meal;
import com.holygunner.discover_meals.save.Saver;
import com.holygunner.discover_meals.tools.ImageHelper;
import com.holygunner.discover_meals.tools.IngredientItemHelper;
import com.holygunner.discover_meals.tools.JsonParser;
import com.holygunner.discover_meals.tools.ToastBuilder;
import com.holygunner.discover_meals.tools.ToolbarHelper;
import com.holygunner.discover_meals.tools.URLBuilder;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;
import static com.holygunner.discover_meals.save.Saver.CHOSEN_INGREDIENTS_KEY;
import static com.holygunner.discover_meals.values.BundleKeys.MEAL_ID_KEY;
import static com.holygunner.discover_meals.values.BundleKeys.MEAL_JSON_KEY;

public class MealRecipeFragment extends Fragment implements View.OnClickListener,
        RecipeRequestProviderTask.Callback {
    private static final String SAVED_DRINK_KEY = "saved_drink_key";
    private static final String IS_FAV_KEY = "is_fav_key";
    private RecyclerView mRecyclerView;
    private ImageView mMealImageView;
    private ImageButton mLikeImageButton;
    private CardView mRecipeCardView;
    private CardView mIngredientsListCardView;
    private TextView mMealNameTextView;
    private TextView mMealRecipeTextView;
    private IngredientManager mIngredientManager;
    private Set<String> chosenIngredientNames;
    private Meal mMeal;
    private boolean mIsFav;
    private JsonParser mJsonParser;

    @NotNull
    public static Fragment newInstance(){
        return new MealRecipeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Context context = Objects.requireNonNull(getContext());
        mIngredientManager = new IngredientManager(context);
        chosenIngredientNames = Saver.readChosenIngredientsNamesInLowerCase(getContext(),
                CHOSEN_INGREDIENTS_KEY);
        mJsonParser = new JsonParser();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        if (mMeal != null) {
            savedInstanceState.putCharArray(SAVED_DRINK_KEY,
                    mJsonParser.serializeMealToJsonBar(mMeal).toCharArray());
            savedInstanceState.putBoolean(IS_FAV_KEY, mIsFav);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Objects.requireNonNull(getActivity()).onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.meal_recipe_layout, container, false);
        android.support.v7.widget.Toolbar toolbar = v.findViewById(R.id.toolbar_drink_recipe);
        ToolbarHelper.setToolbar(toolbar,
                (SingleFragmentActivity) Objects.requireNonNull(getActivity()),
                ToolbarHelper.UP_BUTTON);

        mMealImageView = v.findViewById(R.id.drink_imageView);
        mLikeImageButton = v.findViewById(R.id.like_imageButton);
        mLikeImageButton.setOnClickListener(this);
        ViewGroup likeImageButtonContainer = v.findViewById(R.id.like_button_container);
        likeImageButtonContainer.setOnClickListener(this);
        mRecipeCardView = v.findViewById(R.id.recipe_cardView);
        mIngredientsListCardView = v.findViewById(R.id.ingredients_list_cardView);
        mMealNameTextView = v.findViewById(R.id.drink_name_textView);
        mMealRecipeTextView = v.findViewById(R.id.recipe_textView);
        mMealRecipeTextView.setMovementMethod(ScrollingMovementMethod.getInstance());
        mRecyclerView = v.findViewById(R.id.drink_ingredients_recyclerGridView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), calculateSpanCount()));

        final ProgressBar progressBar = v.findViewById(R.id.app_progress_bar);

        if (savedInstanceState != null){
            if (savedInstanceState.getCharArray(SAVED_DRINK_KEY) != null) {
                mIsFav = savedInstanceState.getBoolean(IS_FAV_KEY);
                String drinkJson = new String(Objects
                        .requireNonNull(savedInstanceState.getCharArray(SAVED_DRINK_KEY)));
                setupMealRecipe(drinkJson, mIsFav);
            }
        }   else {
            loadMeal(progressBar);
        }

        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        // save isFavourite
        if (mMeal != null) {
            Saver.updFavouriteMealId(getContext(), mMeal.getId(), mIsFav,
                    mJsonParser.serializeMealToJsonBar(mMeal));
        }
    }

    private void setupMealRecipe(String drinkJson, Boolean isFav){
        mMeal = mJsonParser.parseJsonToCuisine(drinkJson).meals[0];
        if (isFav != null){
            mIsFav = isFav;
        }   else {
            mIsFav = Saver.isMealFavourite(getContext(), mMeal);
        }
        setLikeImageButton();
        setupAdapter(mMeal);
        ImageHelper.downloadImage(mMeal.getUrlImage(), mMealImageView);
        mRecipeCardView.setVisibility(View.VISIBLE);
        mIngredientsListCardView.setVisibility(View.VISIBLE);
        mLikeImageButton.setVisibility(View.VISIBLE);
        mMealNameTextView.setText(mMeal.getName());
        String recipe = " " + mMeal.getInstruction();
        mMealRecipeTextView.setText(recipe);

    }

    @Override
    public void onClick(View v) {
        boolean result = changeLikeImageButtonState();
        if (result) {
            ToastBuilder.getMealAddedToast(getContext()).show();
        }   else {
            ToastBuilder.getMealRemovedToast(getContext()).show();
        }
    }

    private int calculateSpanCount(){
        int orientation = getResources().getConfiguration().orientation;
        int spanCount = 2;
        if (orientation == ORIENTATION_PORTRAIT){
            spanCount = IngredientItemHelper
                    .calculateNumbOfColumns(Objects.requireNonNull(getContext()));
        }
        return spanCount;
    }

    private void loadMeal(@NotNull ProgressBar progressBar){
        int drinkId = Objects.requireNonNull(getActivity())
                .getIntent().getIntExtra(MEAL_ID_KEY, 0);
        String drinkJson = tryToLoadExtraJson();
        if (drinkJson != null){
            setupMealRecipe(drinkJson, null);
        }   else {
            RecipeRequestProviderTask task = new RecipeRequestProviderTask(this);
            task.registerCallback(this);
            task.setProgressBar(progressBar);
            task.execute(drinkId);
        }
    }

    private String tryToLoadExtraJson(){
        return Objects.requireNonNull(getActivity())
                .getIntent().getStringExtra(MEAL_JSON_KEY);
    }

    private void setLikeImageButton(){
        if (Saver.isMealFavourite(getContext(), mMeal)){
            mLikeImageButton.setImageResource(R.drawable.like_button_pressed);
        }   else {
            mLikeImageButton.setImageResource(R.drawable.like_button);
        }

    }

    private boolean changeLikeImageButtonState(){
        if (!mIsFav){
            mLikeImageButton.setImageResource(R.drawable.like_button_pressed);
            mIsFav = true;
        }   else {
            mLikeImageButton.setImageResource(R.drawable.like_button);
            mIsFav = false;
        }
        return mIsFav;
    }

    private void setupAdapter(Meal meal){
        if (isAdded()){
            List<Ingredient> ingredients = meal.getIngredientsList();
            mRecyclerView.setAdapter(new IngredientsAdapter(ingredients));
        }
    }

    @Override
    public void returnCallback(String drinkJson) {
        if (drinkJson != null) {
            if (isAdded()) {
                setupMealRecipe(drinkJson, null);
                }
            }   else {
                Toast toast = ToastBuilder.getFailedConnectionToast(getContext());
                toast.show();
                Objects.requireNonNull(getActivity()).onBackPressed();
            }
    }

    private class IngredientsAdapter extends RecyclerView.Adapter<IngredientsHolder>{
        List<Ingredient> mIngredients;

        IngredientsAdapter(List<Ingredient> ingredients){
            mIngredients = ingredients;
        }

        @NonNull
        @Override
        public IngredientsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.ingredient_item_v2,
                    parent, false);
            return new IngredientsHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull IngredientsHolder holder, int position) {
            holder.bindIngredient(mIngredients.get(position));
        }

        @Override
        public int getItemCount() {
            return mIngredients.size();
        }
    }

    private class IngredientsHolder extends RecyclerView.ViewHolder{
        private ViewGroup ingredientContainer;
        private ImageView ingredientImageView;
        private TextView ingredientNameTextView;

        IngredientsHolder(View itemView) {
            super(itemView);
//            ingredientContainer = itemView.findViewById(R.id.ingredient_item_container);

            ingredientImageView = itemView.findViewById(R.id.ingredientImageView);
            ingredientNameTextView = itemView.findViewById(R.id.ingredientTextView);
        }

        void bindIngredient(Ingredient ingredient){
            String category = mIngredientManager
                    .findIngredientCategory(ingredient
                            .getName());

            Drawable drawable = mIngredientManager
                    .getIngredientDrawable(category,
                            ingredient.getName());

            if (drawable != null) {
                ingredientImageView.setImageDrawable(drawable);
            }   else {
                Picasso.get()
                        .load(URLBuilder.getMissedIngredientUrl(ingredient.getName()))
                        .into(ingredientImageView);
            }

            String text;

            if (IngredientManager.ingredientMeasureVerification(ingredient.getMeasure())) {
                text = ingredient.getName() + ": " + ingredient.getMeasure();
            }   else {
                text = ingredient.getName();
            }

            ingredientNameTextView.setText(text);

            if (chosenIngredientNames.contains(ingredient.getName().toLowerCase())){
                IngredientItemHelper.setFillToNameTextView(getContext(),
                        ingredientNameTextView, true);
            }
        }
    }
}
