package com.holygunner.discover_meals.tools;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ProgressBar;

import java.lang.ref.WeakReference;

public abstract class RequestProviderAsyncTask<Params, Integer, Result>
        extends AsyncTask<Params, Integer, Result> {
    private WeakReference<Fragment> mFragmentReference;
    private WeakReference<ProgressBar> mProgressBarReference;

    public RequestProviderAsyncTask(Fragment instance){
        mFragmentReference = new WeakReference<>(instance);
    }

    @SafeVarargs
    @Override
    protected final void onProgressUpdate(Integer... param) {
        super.onProgressUpdate(param);
        ProgressBar progressBar = mProgressBarReference.get();
        if (progressBar != null) {
            progressBar.setProgress((java.lang.Integer) param[0]);
        }
    }

    public void setProgressBar(ProgressBar progressBar) {
        mProgressBarReference = new WeakReference<>(progressBar);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (mProgressBarReference.get() != null){
            mProgressBarReference.get().setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPostExecute(Result result) {
        ProgressBar progressBar = mProgressBarReference.get();
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }

    protected WeakReference<Fragment> getFragmentReference() {
        return mFragmentReference;
    }
}
