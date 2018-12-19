package com.holygunner.cocktailsapp_test;

import android.support.v4.app.Fragment;

import com.holygunner.cocktailsapp_test.tools.JsonParser;
import com.holygunner.cocktailsapp_test.tools.RequestProvider;
import com.holygunner.cocktailsapp_test.tools.RequestProviderAsyncTask;

import java.util.Arrays;

public class SearchMealRequestProviderTask extends RequestProviderAsyncTask<String,Integer, String> {
    public SearchMealRequestProviderTask(Fragment instance) {
        super(instance);
    }

    interface Callback{
        void callbackReturn(String result);
    }

    private Callback mCallback;

    void registerCallback(Callback callback){
        mCallback = callback;
    }

    @Override
    protected String doInBackground(String... strings) {
        return new RequestProvider().downloadBarByDrinkName(strings[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        if (result != null) {
            mCallback.callbackReturn(result);

//                JsonParser jsonParser = new JsonParser();
//                fragment.mDrinks = Arrays.asList(jsonParser.parseJsonToCuisine(result).drinks);
//                fragment.setupAdapter();
        }

    }
}
