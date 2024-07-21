package com.debamalya.shopapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Splash extends AppCompatActivity {

    private long splash_time = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable(){

            @Override
            public void run() {
                    startActivity(new Intent(Splash.this,MainActivity.class));
                    finish();
                }
        },splash_time);


        ImageView imageView = findViewById(R.id.splash_image);

        // Load animation
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);

        // Apply animation to ImageView
        imageView.startAnimation(animation);

        // Optional: You can add a listener to handle animation end or other events
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // Animation started
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Animation ended
                // Proceed to the next activity or do any required task
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // Animation repeated
            }
        });

    }
}