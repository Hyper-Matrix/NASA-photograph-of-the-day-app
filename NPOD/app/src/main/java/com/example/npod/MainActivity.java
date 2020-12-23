package com.example.npod;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.POST;

public class MainActivity extends AppCompatActivity {
TextView tvtitle,description;
ImageView iv;
ImageButton zoomin,zoomout;
    String data ="";
    boolean isImageFitToScreen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Attach ids to Views
        TextView tvtitle = findViewById(R.id.tvtitle);
        TextView description = findViewById(R.id.description);
        ImageView iv = findViewById(R.id.imageview);
        ImageButton zoomin = findViewById(R.id.zoomin);
        ImageButton zoomout = findViewById(R.id.zoomout);
        ProgressBar pg  = findViewById(R.id.progressBar);
        ImageButton btncalendar = findViewById(R.id.calendarbutton);
        RelativeLayout rl = findViewById(R.id.rl);

        //Set progressbar visibility ON and all other contents OFF

        pg.setVisibility(View.VISIBLE);
        tvtitle.setVisibility(View.GONE);
        description.setVisibility(View.GONE);
        btncalendar.setVisibility(View.GONE);

        //Calendar button interaction

        btncalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,calendarActivity.class);
                startActivity(intent);
                finish();
            }
        });
         data = getIntent().getStringExtra("data");
        zoomin.setVisibility(View.GONE);    //Make visibility of zoom/play button OFF
        zoomout.setVisibility(View.GONE);   //Make visibility of zoom/play button OFF
        iv.setVisibility(View.INVISIBLE);   //A temporary image view in the middle displaying fetched image its visibility is kep gone throughout
        Retrofit retrofit = new Retrofit.Builder()  //Fetch using Base URL and convert
                .baseUrl("https://api.nasa.gov/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        if(data == null)        //Check if Date is null (initial API fire case )
        {
            JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
            Call<Post> call = jsonPlaceHolderApi.getPosts("DEMO_KEY");
            call.enqueue(new Callback<Post>() {
                @Override
                public void onResponse(Call<Post> call, Response<Post> response) {
                    if(!response.isSuccessful()){
                        pg.setVisibility(View.GONE);
                        description.setVisibility(View.VISIBLE);
                        btncalendar.setVisibility(View.VISIBLE);
                        Toast.makeText(MainActivity.this, String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                        description.setText(String.valueOf(response.code()));
                        return;
                    }
                    Post posts = response.body();
                    pg.setVisibility(View.GONE);
                    tvtitle.setText(posts.getTitle().toString());
                    description.setText(posts.getExplanation().toString());
                    tvtitle.setVisibility(View.VISIBLE);
                    description.setVisibility(View.VISIBLE);
                    btncalendar.setVisibility(View.VISIBLE);


                    if(posts.getMedia_type().equals("image")){      //Check if image is received
                        zoomin.setVisibility(View.VISIBLE);
                        pg.setVisibility(View.GONE);
                        tvtitle.setVisibility(View.VISIBLE);
                        description.setVisibility(View.VISIBLE);
                        btncalendar.setVisibility(View.VISIBLE);
                        Picasso.get().load(posts.getHdurl()).into(iv);
                        Picasso.get().load(posts.getHdurl()).into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                rl.setBackground(new BitmapDrawable(bitmap));
                            }

                            @Override
                            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });
                        zoomin.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(MainActivity.this,fullscreen.class);
                                intent.putExtra("image",posts.getHdurl());
                                intent.putExtra("video","");
                                startActivity(intent);

                            }
                        });


                    }
                    else{       //Video is received
                        pg.setVisibility(View.GONE);
                        zoomout.setVisibility(View.VISIBLE);
                        tvtitle.setVisibility(View.VISIBLE);
                        description.setVisibility(View.VISIBLE);
                        btncalendar.setVisibility(View.VISIBLE);
                        Bitmap bitmap = null;
                        MediaMetadataRetriever mediaMetadataRetriever = null;
                        mediaMetadataRetriever = new MediaMetadataRetriever();
                        mediaMetadataRetriever.setDataSource(posts.getHdurl(), new HashMap<String, String>());
                        //   mediaMetadataRetriever.setDataSource(videoPath);
                        bitmap = mediaMetadataRetriever.getFrameAtTime();
                        if (mediaMetadataRetriever != null) {
                            mediaMetadataRetriever.release();
                        }

                        if (bitmap != null) {
                            BitmapDrawable ob = new BitmapDrawable(getResources(), bitmap);
                            rl.setBackground(ob);
                        }
                        zoomout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent = new Intent(MainActivity.this,fullscreen.class);
                                intent.putExtra("video",posts.getHdurl());
                                intent.putExtra("image","");
                                startActivity(intent);

                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<Post> call, Throwable t) {
                    pg.setVisibility(View.GONE);
                    tvtitle.setVisibility(View.VISIBLE);
                    Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    tvtitle.setText(t.getMessage());


                }
            });}
        else{       //Date is received after selection from calendar initial API fire case, all other below code is same as above except the fact that now placeholder API fetcher gets 2 arguments Apikey and date

            PlaceHolderApi placeHolderApi = retrofit.create(PlaceHolderApi.class);
            Call<Post> call = placeHolderApi.getPosts("DEMO_KEY",data);
            call.enqueue(new Callback<Post>() {
                @Override
                public void onResponse(Call<Post> call, Response<Post> response) {
                    if(!response.isSuccessful()){
                        pg.setVisibility(View.GONE);
                        description.setVisibility(View.VISIBLE);
                        btncalendar.setVisibility(View.VISIBLE);
                        Toast.makeText(MainActivity.this, String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                        description.setText(String.valueOf(response.code()));
                        return;
                    }
                    Post posts = response.body();
                    pg.setVisibility(View.GONE);
                    tvtitle.setText(posts.getTitle().toString());
                    description.setText(posts.getExplanation().toString());

                    tvtitle.setVisibility(View.VISIBLE);
                    description.setVisibility(View.VISIBLE);
                    btncalendar.setVisibility(View.VISIBLE);

                    if(posts.getMedia_type().equals("image")){
                        pg.setVisibility(View.GONE);
                        zoomin.setVisibility(View.VISIBLE);
                        tvtitle.setVisibility(View.VISIBLE);
                        description.setVisibility(View.VISIBLE);
                        btncalendar.setVisibility(View.VISIBLE);
                        Picasso.get().load(posts.getHdurl()).into(iv);
                        Picasso.get().load(posts.getHdurl()).into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                rl.setBackground(new BitmapDrawable(bitmap));
                            }

                            @Override
                            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });
                        zoomin.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(MainActivity.this,fullscreen.class);
                                intent.putExtra("image",posts.getHdurl());
                                intent.putExtra("video","");
                                startActivity(intent);

                            }
                        });


                    }
                    else{ pg.setVisibility(View.GONE);
                        zoomout.setVisibility(View.VISIBLE);
                        tvtitle.setVisibility(View.VISIBLE);
                        description.setVisibility(View.VISIBLE);
                        btncalendar.setVisibility(View.VISIBLE);
                        Bitmap bitmap = null;
                        MediaMetadataRetriever mediaMetadataRetriever = null;
                        mediaMetadataRetriever = new MediaMetadataRetriever();
                        mediaMetadataRetriever.setDataSource(posts.getHdurl(), new HashMap<String, String>());
                        //   mediaMetadataRetriever.setDataSource(videoPath);
                        bitmap = mediaMetadataRetriever.getFrameAtTime();
                        if (mediaMetadataRetriever != null) {
                            mediaMetadataRetriever.release();
                        }

                        if (bitmap != null) {
                            BitmapDrawable ob = new BitmapDrawable(getResources(), bitmap);
                            rl.setBackground(ob);
                        }
                        zoomout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent = new Intent(MainActivity.this,fullscreen.class);
                                intent.putExtra("video",posts.getHdurl());
                                intent.putExtra("image","");
                                startActivity(intent);

                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<Post> call, Throwable t) { pg.setVisibility(View.GONE);
                    tvtitle.setVisibility(View.VISIBLE);
                    Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    tvtitle.setText(t.getMessage());


                }
            });}
        }


    }


