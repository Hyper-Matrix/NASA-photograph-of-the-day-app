package com.example.npod;
//SplashScreen using VideoView
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.VideoView;

public class splashscreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        VideoView vc = findViewById(R.id.vl);
        vc.setVideoPath("android.resource://"+getPackageName()+"/"+R.raw.splash);
        vc.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(isFinishing())
                    return;
                Intent intent = new Intent(splashscreen.this,MainActivity.class);
                startActivity(intent);
                finish();

            }
        });
        vc.start();
    }
    }
