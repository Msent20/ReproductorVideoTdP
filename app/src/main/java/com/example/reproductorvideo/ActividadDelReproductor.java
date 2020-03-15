package com.example.reproductorvideo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.io.File;

import Adapter.ViewsAdapter;

public class ActividadDelReproductor extends AppCompatActivity {

    PlayerView playerView;
    SimpleExoPlayer exoPlayer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_actividad_del_reproductor);
        playerView= findViewById(R.id.reproductorVideo);
        orientacionPantalla(getIntent().getStringExtra("Ruta del archivo"));



    }

    private void mostrarVideo(String rutaArch)  {
        exoPlayer= ExoPlayerFactory.newSimpleInstance(ActividadDelReproductor.this);
        playerView.setPlayer(exoPlayer);
        DataSource.Factory dataSoucer= new DefaultDataSourceFactory(ActividadDelReproductor.this, Util.getUserAgent(ActividadDelReproductor.this,getString(R.string.app_name)));

        MediaSource videoSource=new ExtractorMediaSource.Factory(dataSoucer).createMediaSource(Uri.parse(rutaArch));
        exoPlayer.prepare(videoSource);
        exoPlayer.setPlayWhenReady(true);

    }

    private void orientacionPantalla(String rutArh){
        MediaPlayer mp= new MediaPlayer();
        try {
            mp.setDataSource(rutArh);
            mp.prepare();
            mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                @SuppressLint("SourceLockedOrientationActivity")
                @Override
                public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                    if (width<height){
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        mostrarVideo(rutArh);
                    }else{
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                        mostrarVideo(rutArh);
                    }
                }
            });
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(exoPlayer!=null){
            exoPlayer.release();
        }
        if(exoPlayer!=null){
            if (exoPlayer.isPlayingAd()) {
                exoPlayer.release();
            }
        }
        if(exoPlayer!=null){
            if(exoPlayer.isLoading()) {
                exoPlayer.release();
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(exoPlayer!=null){
            exoPlayer.release();
        }
        if(exoPlayer!=null){
            if (exoPlayer.isPlayingAd()) {
                exoPlayer.release();
            }
        }
        if(exoPlayer!=null){
            if(exoPlayer.isLoading()) {
                exoPlayer.release();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(exoPlayer!=null){
            exoPlayer.release();
        }
        if(exoPlayer!=null){
            if (exoPlayer.isPlayingAd()) {
                exoPlayer.release();
            }
        }
        if(exoPlayer!=null){
            if(exoPlayer.isLoading()) {
                exoPlayer.release();
            }
        }
    }

}