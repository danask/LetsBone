package com.mobile.letsbone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Thread.sleep;

public class SplashActivity extends AppCompatActivity {

    private TextView textViewTitle;
    private TextView textViewVersion;
    private TextView textViewLogo;
    private TextView textViewSplashDesc;
    private ImageView imageView;

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        databaseHelper = new DatabaseHelper(this);

        textViewSplashDesc = (TextView)findViewById(R.id.textViewSplashDesc);
        imageView = (ImageView)findViewById(R.id.letsbone_logo);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.animation);
        textViewSplashDesc.startAnimation(animation);
        imageView.startAnimation(animation);

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    finish();
                    startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                }
            }
        };

        Timer op = new Timer();
        op.schedule(timerTask, 1500);
    }
}
