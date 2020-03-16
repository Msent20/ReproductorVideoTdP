package com.example.reproductorvideo;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
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
    private contenedorVideo contVid;
    private VideoItemAdapter videoItemAdapter;
    private Spinner spinner;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        video_list= findViewById(R.id.video_list);
        SharedPreferences settings = getSharedPreferences("DatosVisitas", Context.MODE_PRIVATE);
        contVid= new contenedorVideo(settings);
        spinner= findViewById(R.id.cambiarVistas);


        contVid.leerDatos(settings);


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
        videoItemAdapter = new VideoItemAdapter(contVid,MainActivity.this,MainActivity.this);
        video_list.setAdapter(videoItemAdapter);



        String[] opciones= {"Normal","Mas vistos"};
        ArrayAdapter<String> adap= new ArrayAdapter<String>(this,R.layout.spinner_text,opciones);
        spinner.setAdapter(adap);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner.getSelectedItem().toString().equals("Mas vistos")){
                    listadoPorVisitas();
                }else{
                    if (spinner.getSelectedItem().toString().equals("Normal"))
                        listadoNormal();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                listadoNormal();
            }
        });


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
        contVid.readAllFiles(hashSet);




    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void listadoPorVisitas() {
        videoItemAdapter.ordenarPorVistas();
        video_list.setAdapter(videoItemAdapter);
    }

    private void listadoNormal(){
        videoItemAdapter.ordenarNormal();
        video_list.setAdapter(videoItemAdapter);
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

    @Override
    public void onClickItem(String filePath) {
        startActivity(new Intent(MainActivity.this, ActividadDelReproductor.class).putExtra("Ruta del archivo",filePath));
        contVid.Addview(filePath);
        videoItemAdapter.notifyDataSetChanged();
    }
}