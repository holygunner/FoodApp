package com.holygunner.cocktailsapp_test;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.holygunner.cocktailsapp_test.new_models.Ingredient;
import com.holygunner.cocktailsapp_test.new_models.IngredientManager;
import com.holygunner.cocktailsapp_test.new_models.IngredientsComparator;
import com.holygunner.cocktailsapp_test.save.Saver;
import com.holygunner.cocktailsapp_test.tools.DrawerMenuManager;
import com.holygunner.cocktailsapp_test.tools.IngredientItemHelper;
import com.holygunner.cocktailsapp_test.tools.ToastBuilder;
import com.holygunner.cocktailsapp_test.tools.ToolbarHelper;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ChosenIngredientsFragment extends Fragment implements View.OnClickListener {
    private static final String CHOSEN_INGREDIENTS_SAVED_STATE_KEY = "chosen_ingrs_saved_state_key";
    private static final String FILLED_POSITIONS_SAVED_KEY = "filled_position_saved_key";
    private final int CURRENT_ITEM_ID = R.id.chosen_ingredients;
    private RecyclerView mRecyclerView;
    private IngredientsAdapter mIngredientsAdapter;
    private IngredientManager mIngredientManager;
    private FloatingActionButton mRemoveButton;
    private DrawerLayout mDrawerLayout;
    private ViewGroup parentLayout;
    private NavigationView mNavigationView;
    private List<Ingredient> mChosenIngredients = new LinkedList<>();
    private Set<String> mFilledIngredients = new HashSet<>();
    private Parcelable savedRecyclerViewState;

    @NotNull
    public static Fragment newInstance(){
        return new ChosenIngredientsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        mIngredientManager = new IngredientManager(Objects.requireNonNull(getContext()));
        mChosenIngredients = mIngredientManager
                .chosenNameToIngredientsList(Saver.readIngredients(getContext(),
                        Saver.CHOSEN_INGREDIENTS_KEY));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null){
            savedRecyclerViewState = savedInstanceState
                    .getParcelable(CHOSEN_INGREDIENTS_SAVED_STATE_KEY);
            mFilledIngredients = new HashSet<>(Arrays.asList(Objects
                    .requireNonNull(savedInstanceState
                            .getStringArray(FILLED_POSITIONS_SAVED_KEY))));
            setButtonVisibility((mFilledIngredients.size() > 0));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.chosen_ingredients_layout, container, false);

        if (savedInstanceState != null){
            savedRecyclerViewState = savedInstanceState
                    .getParcelable(CHOSEN_INGREDIENTS_SAVED_STATE_KEY);
        }

        android.support.v7.widget.Toolbar toolbar
                = v.findViewById(R.id.toolbar);
        ToolbarHelper.setToolbar(toolbar,
                (SingleFragmentActivity) Objects.requireNonNull(getActivity()),
                ToolbarHelper.MENU_BUTTON);

        parentLayout = v.findViewById(R.id.parent_layout);
        mRemoveButton = v.findViewById(R.id.remove_button);
        mRemoveButton.setOnClickListener(this);
        mDrawerLayout = v.findViewById(R.id.drawer_layout);
        mNavigationView = v.findViewById(R.id.nav_view);
        DrawerMenuManager drawerMenuManager = new DrawerMenuManager();
        drawerMenuManager.setNavigationMenu(getActivity(), mDrawerLayout, mNavigationView,
                CURRENT_ITEM_ID);

        mRecyclerView = v.findViewById(R.id.chosen_ingredients_list);
        mRecyclerView.setHasFixedSize(false);
        LinearLayoutManager manager = new GridLayoutManager(getContext(),
                IngredientItemHelper.calculateNumbOfColumns(Objects.requireNonNull(getContext())));
        mRecyclerView.setLayoutManager(manager);
        setupAdapter();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mNavigationView.setCheckedItem(CURRENT_ITEM_ID);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putParcelable(CHOSEN_INGREDIENTS_SAVED_STATE_KEY,
                Objects.requireNonNull(mRecyclerView.getLayoutManager())
                        .onSaveInstanceState());

        savedInstanceState.putStringArray(FILLED_POSITIONS_SAVED_KEY,
                mFilledIngredients.toArray(new String[0]));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(getActivity()).invalidateOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupAdapter() {
        if (isAdded()){
            Collections.sort(mChosenIngredients, new IngredientsComparator());
            mIngredientsAdapter = new IngredientsAdapter(mChosenIngredients);
            mRecyclerView.setAdapter(mIngredientsAdapter);
            if (savedRecyclerViewState != null){
                Objects.requireNonNull(mRecyclerView.getLayoutManager())
                        .onRestoreInstanceState(savedRecyclerViewState);
            }
        }
    }

    @Override
    public void onClick(View v) {
        Iterator<Ingredient> iterator = mIngredientsAdapter.mIngredients.listIterator();

        while (iterator.hasNext()){
            Ingredient ingredient = iterator.next();

            if (ingredient.isFill()){
                int position = mIngredientsAdapter.mIngredients.indexOf(ingredient);
                mIngredientsAdapter.notifyItemRemoved(position);
                mIngredientsAdapter.notifyItemRangeChanged(position, mChosenIngredients.size());
                iterator.remove();
                Saver.updChosenIngredient(getContext(), ingredient.getName());
            }
        }
        mFilledIngredients.clear();
        setButtonVisibility(false);

        if (mChosenIngredients.size() == 0){
            startActivity(new Intent(getContext(), SelectIngredientsActivity.class));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ToastBuilder.chosenIngrsListEmptyToast(getContext()).show();
                }
            }, ToastBuilder.SHOW_TOAST_DELAY);
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
            View view = inflater.inflate(R.layout.ingredient_item, parent, false);
            return new IngredientsHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull IngredientsHolder holder, int position) {
            Ingredient ingredient = mIngredients.get(position);
            if (mFilledIngredients.contains(ingredient.getName())){
                holder.setHolderFill(true);
                ingredient.setFill(true);
            }
            holder.bindIngredient(mIngredients.get(position));
        }

        @Override
        public int getItemCount() {
            return mIngredients.size();
        }
    }

    private class IngredientsHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private ImageView ingredientImageView;
        private TextView ingredientNameTextView;
        private Ingredient mIngredient;

        IngredientsHolder(View itemView) {
            super(itemView);
            ingredientImageView = itemView.findViewById(R.id.ingredientImageView);
            ingredientNameTextView = itemView.findViewById(R.id.ingredientTextView);
            ingredientImageView.setOnClickListener(this);
            ingredientNameTextView.setOnClickListener(this);
        }

        void bindIngredient(Ingredient ingredient) {
            mIngredient = ingredient;
            mIngredientManager.bindIngredientWithImageView(ingredientImageView, ingredient.getName());
            ingredientNameTextView.setText(ingredient.getName());

            if (mIngredient.isFill()){
                setHolderFill(true);
            }   else {
                setHolderFill(false);
            }
        }

        @Override
        public void onClick(View v) {
            if (mIngredient.isFill()){
                mFilledIngredients.remove(mIngredient.getName());
                mIngredient.setFill(false);
                setHolderFill(false);
            }   else {
                mFilledIngredients.add(mIngredient.getName());
                mIngredient.setFill(true);
                setHolderFill(true);
            }

            setButtonVisibility((mFilledIngredients.size() > 0));
        }

        private void setHolderFill(boolean isFill){
            IngredientItemHelper.setColorFilterToImageView(getContext(),
                    ingredientImageView, isFill);
            IngredientItemHelper.setFillToNameTextView(getContext(),
                    ingredientNameTextView, isFill);
        }
    }

    public void setButtonVisibility(boolean isFillExists){
        boolean visibility;

        if ((mRemoveButton.getVisibility() == View.INVISIBLE) && isFillExists){
            visibility = true;
        }   else if
                ((mRemoveButton.getVisibility() == View.VISIBLE) && !isFillExists){
            visibility = false;
            TransitionSet set = new TransitionSet()
                    .addTransition(new Fade())
                    .setInterpolator(new FastOutLinearInInterpolator());
            TransitionManager.beginDelayedTransition(parentLayout, set);
        }   else {
            return;
        }
        mRemoveButton.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
    }
}
