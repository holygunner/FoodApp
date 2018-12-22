package com.holygunner.discover_meals;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.holygunner.discover_meals.models.Meal;
import com.holygunner.discover_meals.tools.DrawerMenuManager;
import com.holygunner.discover_meals.tools.JsonParser;
import com.holygunner.discover_meals.tools.ToolbarHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SearchMealFragment extends Fragment implements SearchMealRequestProviderTask.Callback {
    private final int CURRENT_ITEM_ID = R.id.search_drink;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private RecyclerView mRecyclerView;
    private List<Meal> mMeals = new ArrayList<>();
    private ProgressBar mProgressBar;
    private SearchView mSearchView;
    private JsonParser mJsonParser;

    @NonNull
    public static SearchMealFragment newInstance(){
        return new SearchMealFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        mJsonParser = new JsonParser();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.search_meal_layout, container, false);

        android.support.v7.widget.Toolbar toolbar = v.findViewById(R.id.toolbar);
        ToolbarHelper.setToolbar(toolbar,
                (SingleFragmentActivity) Objects.requireNonNull(getActivity()),
                ToolbarHelper.MENU_BUTTON);

        mProgressBar = v.findViewById(R.id.app_progress_bar);

        mSearchView = v.findViewById(R.id.search_bar);
        setSearchView();

        mDrawerLayout = v.findViewById(R.id.drawer_layout);
        mNavigationView = v.findViewById(R.id.nav_view);
        new DrawerMenuManager().setNavigationMenu(getActivity(), mDrawerLayout, mNavigationView,
                CURRENT_ITEM_ID);

        mRecyclerView = v.findViewById(R.id.drinks_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setupAdapter();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mNavigationView.setCheckedItem(CURRENT_ITEM_ID);
        if (mRecyclerView != null) {
            Objects.requireNonNull(mRecyclerView.getAdapter()).notifyDataSetChanged();
        }
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

    @Override
    public void callbackReturn(String result) {
        if (result != null) {
            Meal[] meals = mJsonParser.parseJsonToCuisine(result).meals;
            if (meals != null) {
                mMeals = Arrays.asList(meals);
                setupAdapter();
            }
        }
    }

    private void setSearchView(){
            mSearchView.setQueryHint(getString(R.string.enter_meal_name));
            mSearchView.setClipToPadding(true);
            mSearchView.setIconifiedByDefault(false);
            mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(String query) {
                    searchMeal(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if (newText.toCharArray().length > 1) {
                        searchMeal(newText);
                    }
                    return false;
                }
            });
    }

    private void setupAdapter(){
        if (isAdded()){
            MealsAdapter mealsAdapter = new MealsAdapter(getContext(), mMeals);
            mRecyclerView.setAdapter(mealsAdapter);
        }
    }

    private void searchMeal(String drinkName){
        SearchMealRequestProviderTask task = new SearchMealRequestProviderTask(this);
        task.registerCallback(this);
        task.setProgressBar(mProgressBar);
        task.execute(drinkName);
    }
}
