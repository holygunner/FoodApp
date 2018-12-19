package com.holygunner.cocktailsapp_test.just_study;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class Downloader {

    interface MyCallback{
        void callbackReturn(String message);
    }

    MyCallback mMyCallback;

    public void registerCallback(MyCallback myCallback){
        mMyCallback = myCallback;
    }

    public void setImageView(ImageView imageView, String path){
        Picasso.get()
                .load(path)
                .into(imageView);

        mMyCallback.callbackReturn(path);
    }
}
