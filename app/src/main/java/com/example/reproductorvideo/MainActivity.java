package com.example.reproductorvideo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

import Adapter.VideoItemAdapter;
import Adapter.ViewsAdapter;
import Interfaz.ClickListenner;
import Model.VideoModel;

public class MainActivity extends AppCompatActivity implements ClickListenner {

    private static final int PERMISSION_CODE = 101;
    RecyclerView video_list;
    private static List<String> archivos=new ArrayList<>();
    private contenedorVideo contVid;

    private static Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        video_list= findViewById(R.id.video_list);
        //Si la version de andriod es mayor a la 6 pregunta por los permisos
        if(Build.VERSION.SDK_INT>=23){
            if(checkPermission()) {
                readVideos();
            }else{
                requestPermissions();
            }
        }else{
            readVideos();
        }
        /*spinner= findViewById(R.id.cambiarVistas);
        String[] opciones= {"Normal","Mas vistos"};
        ArrayAdapter<String> adap= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,opciones);
        spinner.setAdapter(adap);
        if (spinner.getSelectedItem().toString().equals("Normal")){
            listadoNormal();
        }else{
            listadoPorVisitas();
        }
        */
    }

    private void readVideos(){
        String[] projection={MediaStore.Video.VideoColumns.DATA, MediaStore.Video.Media.DISPLAY_NAME};
        Cursor cursor= getApplicationContext().getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,projection, null,null, null);
        HashSet<String> hashSet=new HashSet<>();
        try{
            if (cursor!=null){
                cursor.moveToFirst();
                do{
                    hashSet.add(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)));
                }while(cursor.moveToNext());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        video_list.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        List<VideoModel> videoModellist= contVid.readAllFiles(hashSet);

        VideoItemAdapter videoItemAdapter = new VideoItemAdapter(contVid,videoModellist,MainActivity.this,MainActivity.this);
        video_list.setAdapter(videoItemAdapter);
    }

    private void listadoPorVisitas() {
        VideoItemAdapter via= new VideoItemAdapter(contVid,contVid.ordenarVisitas(),MainActivity.this,MainActivity.this);
        video_list.setAdapter(via);
    }

    private void listadoNormal(){
        VideoItemAdapter via= new VideoItemAdapter(contVid,contVid.ordenarNormal(),MainActivity.this,MainActivity.this);
        video_list.setAdapter(via);
    }




    private boolean checkPermission() {
        int result= ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result== PackageManager.PERMISSION_GRANTED){
            return true;
        }else
            return false;
    }

    private void requestPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            Toast.makeText(this,"Permitir acceso",Toast.LENGTH_SHORT).show();
        }else{
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_CODE:
                if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    readVideos();
                }
        }
    }


    //Lee todos los videos de la memoria del celular

    /*private void readAllVideos() {

        HashSet<String> hashSet=new HashSet<>();
        String[] projection={MediaStore.Video.VideoColumns.DATA, MediaStore.Video.Media.DISPLAY_NAME};
        Cursor cursor= getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,projection, null,null, null);
        try{

            if (cursor!=null){
                cursor.moveToFirst();
                do{
                    hashSet.add(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)));
                }while(cursor.moveToNext());

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        List<String> file_path=new ArrayList<>(hashSet);
        List<VideoModel> vml=new ArrayList<>();

        for (String data: file_path){
            File file= new File(data);
            ViewsAdapter.addView(data);
            archivos.add(data);
            vml.add(new VideoModel(file.getName(),file.getAbsolutePath()));
        }
        video_list.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        VideoItemAdapter videoItemAdapter= new VideoItemAdapter(vml,MainActivity.this,MainActivity.this);
        video_list.setAdapter(videoItemAdapter);

    }*/


    @Override
    public void onClickItem(String filePath) {
        startActivity(new Intent(MainActivity.this, ActividadDelReproductor.class).putExtra("Ruta del archivo",filePath));
        ViewsAdapter.addView(filePath);
    }
}
