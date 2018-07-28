package com.example.chiiia12.androidplayground;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class PicassoActivity extends AppCompatActivity {

    private static final String TAG = PicassoActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picasso);
    }


    @Override
    protected void onResume() {
        super.onResume();
        ImageView imageView = findViewById(R.id.picasso_IV);

        Picasso.get()
                .load("https://qiita-image-store.s3.amazonaws.com/0/50510/profile-images/1473692129")
                .resize(400, 400)
                .centerInside()
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "onSuccess: ");
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.d(TAG, "onError: " + e.getMessage());

                    }
                });
    }
}
