package com.holygunner.cocktailsapp_test.just_study;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.holygunner.cocktailsapp_test.R;
import com.holygunner.cocktailsapp_test.new_models.Cuisine;
import com.holygunner.cocktailsapp_test.new_models.Ingredient;
import com.holygunner.cocktailsapp_test.new_models.Meal;
import com.holygunner.cocktailsapp_test.tools.RequestProvider;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class TestCallbackPatternActivity extends AppCompatActivity
        implements View.OnClickListener, Downloader.MyCallback {
    Button mLoadButton;
    ImageView mImageView;
    TextView mProgressTextView;
    Downloader mDownloader;
    private static final String PATH
            = "https://static.boredpanda.com/blog/wp-content/uploads/2018/04/5acb63d83493f__700-png.jpg";
    private static final String MESSAGE = "Image load";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_cb_layout);
        mLoadButton = findViewById(R.id.load_button);
        mLoadButton.setOnClickListener(this);
        mImageView = findViewById(R.id.imageView);
        mProgressTextView = findViewById(R.id.load_progress_textView);
        mDownloader = new Downloader();
        mDownloader.registerCallback(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.load_button){
//            mDownloader.setImageView(mImageView, PATH);
            Gson gson = new Gson();

            String filtIngrUrl = "https://www.themealdb.com/api/json/v1/1/filter.php?i=chicken%20breast";
            String mealByIdUrl = "https://www.themealdb.com/api/json/v1/1/lookup.php?i=52772";
            String searchMealByName = "https://www.themealdb.com/api/json/v1/1/search.php?s=Arrabiata";
//            String url = "https://www.thecocktaildb.com/api/json/v1/1/list.php?i=list";
            new MyAsyncTask().execute(searchMealByName);
        }
    }

    @Override
    public void callbackReturn(String message) {
        mProgressTextView.setText(message);
    }

    private class MyAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            return new RequestProvider().downloadJsonByRequest(strings[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Gson gson = new Gson();
            Cuisine cuisine = gson.fromJson(result, Cuisine.class);

            Log.i("TAG", result);



//            for (Ingredient ingredient: cuisine.meals){
//                String url = "https://www.themealdb.com/images/ingredients/" + ingredient.name + ".png";
//                Picasso.get()
//                        .load(url)
//                        .into(Target(url, ingredient));
//            }
        }

        private Target Target(final String url, final Ingredient ingredient){
            return new Target(){

                @Override
                public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            File file = new File(Environment.getExternalStorageDirectory()
                                    .getPath() + "/" + ingredient.getName() + ".png");
                            try {
                                file.createNewFile();
                                FileOutputStream ostream = new FileOutputStream(file);
                                bitmap.compress(Bitmap.CompressFormat.PNG, 80, ostream);
                                ostream.flush();
                                ostream.close();
                            } catch (IOException e) {
                                Log.e("IOException", e.getLocalizedMessage());
                            }
                        }
                    }).start();

                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
        }
    }
}
