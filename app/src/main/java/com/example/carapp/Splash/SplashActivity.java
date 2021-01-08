package com.example.carapp.Splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.carapp.Home.MainActivity;
import com.example.carapp.R;

public class SplashActivity extends AppCompatActivity {

    private ImageView mGifImage ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mGifImage = findViewById(R.id.gif_image);
        Glide.with(this).load(R.raw.gifcar).into(mGifImage);
        splashTimer();
    }

    private void splashTimer() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this , MainActivity.class));
                finish();
            }
        }, 3000);
    }
}