package com.holygunner.discover_meals;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.holygunner.discover_meals.models.Meal;
import com.holygunner.discover_meals.models.MealsComparator;
import com.holygunner.discover_meals.save.Saver;
import com.holygunner.discover_meals.tools.DrawerMenuManager;
import com.holygunner.discover_meals.tools.JsonParser;
import com.holygunner.discover_meals.tools.ToastBuilder;
import com.holygunner.discover_meals.tools.ToolbarHelper;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class FavouriteMealsFragment extends Fragment {
    private final int CURRENT_ITEM_ID = R.id.favourite_meals;
    private RecyclerView mRecyclerView;
    private MealsAdapter mMealsAdapter;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private List<Meal> mFavMeals = new ArrayList<>();
    private JsonParser mJsonParser;

    @NonNull
    public static FavouriteMealsFragment newInstance(){
        return new FavouriteMealsFragment();
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mJsonParser = new JsonParser();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.favourite_meals_layout, container, false);

        android.support.v7.widget.Toolbar toolbar = v.findViewById(R.id.toolbar);
        ToolbarHelper.setToolbar(toolbar,
                (SingleFragmentActivity) Objects.requireNonNull(getActivity()),
                ToolbarHelper.MENU_BUTTON);

        mDrawerLayout = v.findViewById(R.id.drawer_layout);
        mNavigationView = v.findViewById(R.id.nav_view);
        new DrawerMenuManager().setNavigationMenu(getActivity(), mDrawerLayout, mNavigationView,
                CURRENT_ITEM_ID);

        mRecyclerView = v.findViewById(R.id.meals_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        setupAdapter();
        closeIfEmpty();
        setupSwipeToRemove();
        mNavigationView.setCheckedItem(CURRENT_ITEM_ID);
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

    private List<Meal> getFavMeals(){
        Set<String> favMealsJsons = Saver.readFavouriteMealsJsons(getContext());
        List<Meal> mavMeals = new ArrayList<>();


        for (String json: favMealsJsons){
            if (!json.equals("")) {
                Meal favMeal = mJsonParser.parseJsonToCuisine(json).meals[0];
                mavMeals.add(favMeal);
            }
        }
        Collections.sort(mavMeals, new MealsComparator());
        return mavMeals;
    }

    private void setupSwipeToRemove(){
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback
                = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            Drawable xMark;
            int xMarkMargin;
            boolean initiated;

            private void init() {
                xMark = ContextCompat.getDrawable(Objects.requireNonNull(getContext()),
                        R.drawable.ic_clear);
                xMarkMargin = (int) getContext().getResources().getDimension(R.dimen.ic_clear_margin);
                initiated = true;
            }

            @Contract(pure = true)
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.LEFT || direction == ItemTouchHelper.RIGHT){
                    int position = viewHolder.getAdapterPosition();
                    Meal drink = mFavMeals.get(position);
                    mFavMeals.remove(position);
                    Saver.updFavouriteMealId(getContext(), drink.getId(), false, "");
                    mMealsAdapter.notifyItemRemoved(position);
                    mMealsAdapter.notifyItemRangeChanged(position, mFavMeals.size());
                    closeIfEmpty();
                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder,
                                    float dX, float dY, int actionState,
                                    boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;

                if (viewHolder.getAdapterPosition() == -1) {
                    return;
                }

                if (!initiated) {
                    init();
                }

                // draw x mark
                int itemHeight = itemView.getBottom() - itemView.getTop();
                int intrinsicWidth = xMark.getIntrinsicWidth();
                int intrinsicHeight = xMark.getIntrinsicWidth();

                int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
                int xMarkRight = itemView.getRight() - xMarkMargin;
                int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight)/2;
                int xMarkBottom = xMarkTop + intrinsicHeight;
                xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

                xMark.draw(c);

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);
    }

    private void setupAdapter(){
        mFavMeals = getFavMeals();
        mMealsAdapter = new MealsAdapter(getContext(), mFavMeals);
        mRecyclerView.setAdapter(mMealsAdapter);
    }

    private void closeIfEmpty(){

        if (mFavMeals.size() == 0){
            startActivity(new Intent(getContext(), SelectIngredientsActivity.class));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ToastBuilder.favMealsListEmptyToast(getContext()).show();
                }
            }, ToastBuilder.SHOW_TOAST_DELAY);
        }
    }
}
