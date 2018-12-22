package com.holygunner.discover_meals;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.holygunner.discover_meals.models.IngredientManager;
import com.holygunner.discover_meals.models.IngredientsCategory;
import com.holygunner.discover_meals.save.Saver;
import com.holygunner.discover_meals.tools.DrawerMenuManager;
import com.holygunner.discover_meals.tools.FabVisibilityHelper;
import com.holygunner.discover_meals.tools.ToolbarHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.holygunner.discover_meals.save.Saver.CHOSEN_INGREDIENTS_KEY;

public class SelectIngredientsFragment extends Fragment implements View.OnClickListener{
    private final int CURRENT_ITEM_ID = R.id.select_ingredients;
    private static final String SELECT_INGREDIENTS_SAVED_STATE_KEY
            = "select_ingredients_saved_state";
    private Parcelable savedRecyclerViewState;
    private RecyclerView mRecyclerView;
    private FloatingActionButton mMixFab;
    private ViewGroup mParentContainer;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private List<IngredientsCategory> mIngredientsCategories = new ArrayList<>();
    private IngredientManager mIngredientManager;
    private SnapHelper mSnapHelper;

    @NonNull
    public static SelectIngredientsFragment newInstance(){
        return new SelectIngredientsFragment();
    }

    public void onCreate(Bundle onSavedInstanceState){
        super.onCreate(onSavedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        mIngredientManager = new IngredientManager(Objects.requireNonNull(getContext()));
        mIngredientsCategories = mIngredientManager.getAllIngredients();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null){
            savedRecyclerViewState = savedInstanceState.getParcelable(SELECT_INGREDIENTS_SAVED_STATE_KEY);
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.select_ingredients_layout, container, false);

        if (savedInstanceState != null){
            savedRecyclerViewState = savedInstanceState.getParcelable(SELECT_INGREDIENTS_SAVED_STATE_KEY);
        }

        android.support.v7.widget.Toolbar toolbar
                = v.findViewById(R.id.toolbar);
        ToolbarHelper.setToolbar(toolbar,
                (SingleFragmentActivity) Objects.requireNonNull(getActivity()),
                ToolbarHelper.MENU_BUTTON);

        mParentContainer = v.findViewById(R.id.parent_layout);
        mMixFab = v.findViewById(R.id.mix_button);
        mDrawerLayout = v.findViewById(R.id.drawer_layout);
        mNavigationView = v.findViewById(R.id.nav_view);
        DrawerMenuManager drawerMenuManager = new DrawerMenuManager();
        drawerMenuManager.setNavigationMenu(getActivity(), mDrawerLayout, mNavigationView,
                CURRENT_ITEM_ID);

        mMixFab.setOnClickListener(this);

        mSnapHelper = new LinearSnapHelper();

        mRecyclerView = v.findViewById(R.id.ingredients_recycler_view);
        mRecyclerView.setHasFixedSize(true);

//        if (mRecyclerView.getOnFlingListener() == null) {
//            mSnapHelper.attachToRecyclerView(mRecyclerView);
//        }

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        setupAdapter();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(mRecyclerView.getAdapter()).notifyDataSetChanged();
        setMixButtonVisibility();
        mNavigationView.setCheckedItem(CURRENT_ITEM_ID);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable(SELECT_INGREDIENTS_SAVED_STATE_KEY,
                Objects.requireNonNull(mRecyclerView.getLayoutManager()).onSaveInstanceState());
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(getContext(), SelectedMealsActivity.class));
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

    public void setMixButtonVisibility(){
        boolean isExist = Saver.readIngredients(getContext(),
                CHOSEN_INGREDIENTS_KEY).isEmpty();

        FabVisibilityHelper.setFabVisibility(mMixFab, !isExist);
    }

    private void setupAdapter() {
        if (isAdded()){
            IngredientCategoriesAdapter adapter = new IngredientCategoriesAdapter(this,
                    mIngredientManager, mIngredientsCategories);
            mRecyclerView.setAdapter(adapter);

            if (savedRecyclerViewState != null){
                Objects.requireNonNull(mRecyclerView.getLayoutManager())
                        .onRestoreInstanceState(savedRecyclerViewState);
            }
        }
    }
}
