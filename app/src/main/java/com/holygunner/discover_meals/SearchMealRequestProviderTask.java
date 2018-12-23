package com.holygunner.discover_meals;

import android.support.v4.app.Fragment;

import com.holygunner.discover_meals.tools.RequestProvider;
import com.holygunner.discover_meals.tools.RequestProviderAsyncTask;

public class SearchMealRequestProviderTask extends RequestProviderAsyncTask<String,Integer, String> {

    interface Callback {
        void callbackReturn(String result);
    }

    private Callback mCallback;

    void registerCallback(Callback callback){
        mCallback = callback;
    }

    @Override
    protected String doInBackground(String... strings) {
        return new RequestProvider().downloadJsonByMealName(strings[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        if (result != null) {
            mCallback.callbackReturn(result);
        }

    }
}
