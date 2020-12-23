package com.example.npod;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;

public class fullscreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        String url = getIntent().getStringExtra("image");
        String url1 = getIntent().getStringExtra("video");
        ImageView imgview = findViewById(R.id.imgview);
        VideoView videoView = findViewById(R.id.videoView);
        videoView.setVisibility(View.GONE);
        imgview.setVisibility(View.GONE);
        if(url1.equals("")){        //If it is a image show in fullscreen
            imgview.setVisibility(View.VISIBLE);
            Picasso.get().load(url).into(imgview);  //Load image from url using picasso
        }
        else{
            videoView.setVisibility(View.VISIBLE);
            videoView.setVideoURI(Uri.parse(url1));
            videoView.requestFocus();

            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                }
            });

            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    videoView.start();
                    mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                        @Override
                        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                            MediaController mediaController = new MediaController(fullscreen.this);
                            videoView.setMediaController(mediaController);
                            mediaController.setAnchorView(videoView);
                        }
                    });
                }
            });

            videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    return false;
                }
            });
        }






    }
}