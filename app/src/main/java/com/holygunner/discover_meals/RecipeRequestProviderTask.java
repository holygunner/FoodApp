package com.holygunner.discover_meals;

import android.support.v4.app.Fragment;

import com.holygunner.discover_meals.tools.RequestProvider;
import com.holygunner.discover_meals.tools.RequestProviderAsyncTask;

public class RecipeRequestProviderTask extends RequestProviderAsyncTask<Integer, Integer, String> {
    private Callback mCallback;

    public interface Callback{
        void returnCallback(String drinkJson);
    }

    void registerCallback(Callback callback){
        mCallback = callback;
    }

    @Override
    protected String doInBackground(Integer... drinksId) {
        return new RequestProvider().downloadBarJsonById(drinksId[0]);
    }

    @Override
    protected void onPostExecute(String drinkJson){
        super.onPostExecute(drinkJson);
        mCallback.returnCallback(drinkJson);
    }
}
