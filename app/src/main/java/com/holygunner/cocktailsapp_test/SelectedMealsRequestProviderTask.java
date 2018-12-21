package com.holygunner.cocktailsapp_test;

import android.support.v4.app.Fragment;

import com.holygunner.cocktailsapp_test.new_models.Cuisine;
import com.holygunner.cocktailsapp_test.tools.RequestProvider;
import com.holygunner.cocktailsapp_test.tools.RequestProviderAsyncTask;

import java.util.List;

public class SelectedMealsRequestProviderTask
        extends RequestProviderAsyncTask<String, Void, List<Cuisine>> {
    private Callback mCallback;

    public interface Callback{
        void returnCallback(List<Cuisine> downloadCuisines);
    }

    void registerCallback(Callback callback){
        mCallback = callback;
    }

    SelectedMealsRequestProviderTask(Fragment instance) {
        super(instance);
    }

    @Override
    protected List<Cuisine> doInBackground(String... ingredients) {
        return new RequestProvider().downloadCuisines(ingredients);
    }

    @Override
    protected void onPostExecute(List<Cuisine> downloadCuisines) {
        super.onPostExecute(downloadCuisines);

        mCallback.returnCallback(downloadCuisines);
    }
}
