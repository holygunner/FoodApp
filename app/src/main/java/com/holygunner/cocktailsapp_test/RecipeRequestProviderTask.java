package com.holygunner.cocktailsapp_test;

import android.support.v4.app.Fragment;

import com.holygunner.cocktailsapp_test.tools.RequestProvider;
import com.holygunner.cocktailsapp_test.tools.RequestProviderAsyncTask;

public class RecipeRequestProviderTask extends RequestProviderAsyncTask<Integer, Integer, String> {
    private Callback mCallback;

    RecipeRequestProviderTask(Fragment instance) {
        super(instance);
    }

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
