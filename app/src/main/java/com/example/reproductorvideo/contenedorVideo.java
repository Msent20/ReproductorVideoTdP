package com.example.reproductorvideo;

import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import Adapter.ViewsAdapter;
import Model.VideoModel;

/*
    Clase que contiene los videos (la ruta de cada uno) y los ordenamientos.
 */
public class contenedorVideo {
    private ArrayList<String> contenedor;
    private ViewsAdapter viewsAdapter;

    public contenedorVideo(){
        viewsAdapter= new ViewsAdapter();
        contenedor= new ArrayList<String>();
    }


    //Lee todos los videos de la memoria del celular y retorna una lista con dichos videos.
    public List<VideoModel> readAllFiles(HashSet<String> hashSet) {
        List<String> file_path=new ArrayList<String>(hashSet);
        List<VideoModel> vml=new ArrayList<VideoModel>();

        for (String data: file_path){
            File file= new File(data);
            viewsAdapter.addView(data);
            contenedor.add(data);
            vml.add(new VideoModel(file.getName(),file.getAbsolutePath()));
        }
        return vml;
    }

    //Ordena los videos de forma predeterminada, y retorna una lista con videomodels con dichos videos
    public List<VideoModel> ordenarNormal(){
        List<VideoModel> videoModel=new ArrayList<VideoModel>();
        for(String rutaArch: contenedor){
            File file= new File(rutaArch);
            videoModel.add(new VideoModel(file.getName(),file.getAbsolutePath()));
        }

        return videoModel;
    }

    //Ordena los videos segun fueron mas vistos y los retorna en una lista
    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<VideoModel> ordenarVisitas(){
        ArrayList<String> masVisitados= viewsAdapter.masVistos();


        List<VideoModel> videoModel= new ArrayList<VideoModel>();
        for(String ruta: masVisitados){
            File file= new File(ruta);
            videoModel.add(new VideoModel(file.getName(),file.getAbsolutePath()));
        }

        return videoModel;
    }

    public void Addview(String rutaArch) {
        viewsAdapter.addView(rutaArch);
    }
    public void guardarDatos(SharedPreferences.Editor datos){
        viewsAdapter.guardarDatos(datos);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void leerDatos(SharedPreferences datos){
        viewsAdapter.leerDatos(datos);
    }


}
