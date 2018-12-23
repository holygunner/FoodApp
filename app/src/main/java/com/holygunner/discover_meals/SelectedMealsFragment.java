package com.holygunner.discover_meals;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.holygunner.discover_meals.models.Cuisine;
import com.holygunner.discover_meals.models.CuisineManager;
import com.holygunner.discover_meals.models.IngredientManager;
import com.holygunner.discover_meals.models.Meal;
import com.holygunner.discover_meals.save.Saver;
import com.holygunner.discover_meals.tools.ToastBuilder;
import com.holygunner.discover_meals.tools.ToolbarHelper;

import org.jetbrains.annotations.NotNull;

import static com.holygunner.discover_meals.save.Saver.CHECKED_INGREDIENTS_KEY;
import static com.holygunner.discover_meals.save.Saver.CHOSEN_INGREDIENTS_KEY;

public class SelectedMealsFragment extends Fragment
        implements SelectedMealsRequestProviderTask.Callback {
    private static final String SELECTED_DRINKS_SAVED_STATE_KEY = "selected_drinks_saved_state_key";
    private Parcelable savedRecyclerViewState;
    private RecyclerView mRecyclerView;
    private List<Meal> mMeals = new ArrayList<>();
    private CuisineManager mCuisineManager;
    private int howMuchChecked;

    @NonNull
    public static SelectedMealsFragment newInstance(){
        return new SelectedMealsFragment();
    }

    public void onCreate(Bundle onSavedInstanceState){
        super.onCreate(onSavedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null){
            savedRecyclerViewState = savedInstanceState
                    .getParcelable(SELECTED_DRINKS_SAVED_STATE_KEY);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.selected_meals_layout, container, false);

        if (savedInstanceState != null){
            savedRecyclerViewState = savedInstanceState
                    .getParcelable(SELECTED_DRINKS_SAVED_STATE_KEY);
        }

        android.support.v7.widget.Toolbar toolbar = v.findViewById(R.id.toolbar);
        ToolbarHelper.setToolbar(toolbar,
                (SingleFragmentActivity) Objects.requireNonNull(getActivity()),
                ToolbarHelper.UP_BUTTON);
        Objects.requireNonNull(((SingleFragmentActivity) getActivity()).getSupportActionBar())
                .setTitle(R.string.choose_the_meal);

        mRecyclerView = v.findViewById(R.id.meals_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        final ProgressBar progressCuisine = v.findViewById(R.id.app_progress_bar);

        loadMeals(progressCuisine);
        setupAdapter();
        return v;
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

    @Override
    public void onResume() {
        super.onResume();
        if (mRecyclerView != null) {
            Objects.requireNonNull(mRecyclerView.getAdapter()).notifyDataSetChanged();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putParcelable(SELECTED_DRINKS_SAVED_STATE_KEY,
                Objects.requireNonNull(mRecyclerView.getLayoutManager()).onSaveInstanceState());
    }

    @Override
    public void returnCallback(List<Cuisine> downloadCuisines) {
        if (howMuchChecked == downloadCuisines.size()) {
            Cuisine selectedCuisine = mCuisineManager.getSelectedCuisine(downloadCuisines);
            mMeals = Arrays.asList(selectedCuisine.meals);
            setupAdapter();
        }   else {
            Toast toast = ToastBuilder.getFailedConnectionToast(getContext());
            toast.show();
            Objects.requireNonNull(getActivity()).onBackPressed();
        }
        howMuchChecked = 0;
    }

    private void loadMeals(@NotNull ProgressBar progressCuisine){
        mCuisineManager = new CuisineManager(getContext());

        String[] added = IngredientManager.countAddedIngredients(
                Saver.readIngredients(getContext(), CHOSEN_INGREDIENTS_KEY),
                Saver.readIngredients(getContext(), CHECKED_INGREDIENTS_KEY)).toArray(new String[0]);

        howMuchChecked = added.length;

        SelectedMealsRequestProviderTask task = new SelectedMealsRequestProviderTask();
        task.registerCallback(this);
        task.setProgressBar(progressCuisine);
        task.execute(added);
    }

    private void setupAdapter(){
        if (isAdded()){
            MealsAdapter mealsAdapter = new MealsAdapter(getContext(), mMeals);
            mRecyclerView.setAdapter(mealsAdapter);
            if (savedRecyclerViewState != null) {
                Objects.requireNonNull(mRecyclerView.getLayoutManager())
                        .onRestoreInstanceState(savedRecyclerViewState);
            }
        }
    }
}
