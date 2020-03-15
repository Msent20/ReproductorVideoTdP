package Adapter;



import android.content.SharedPreferences;
import android.os.Build;
import android.renderscript.Sampler;
import android.util.Pair;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ViewsAdapter {
  private static Map<String, Integer> views = new HashMap<String, Integer>();
  private static final int CONST_ZERO = 0;




  public static void addView(String rutaArch){
      if(views.containsKey(rutaArch)) {
          Integer aux= views.get(rutaArch)+1;
          views.put(rutaArch, aux);
      }
      else
          views.put(rutaArch, CONST_ZERO);
  }
  public static int seeViews(String rutaArch) throws Exception{
      if(!views.containsKey(rutaArch))
          throw new Exception("No video found with name: "+rutaArch.toString());
      return views.get(rutaArch);
  }

  //Metodo que crea una lista ordenada con las rutas de los videos mas vistos
  @RequiresApi(api = Build.VERSION_CODES.N)
  public static ArrayList<String> masVistos(){
      List<Map.Entry<String , Integer>> list = new ArrayList<Map.Entry<String, Integer>>(views.entrySet());
      list.sort(Collections.reverseOrder(Map.Entry.comparingByValue()));

      ArrayList<String> videosOrd= new ArrayList<String>();

      for(Map.Entry<String,Integer> ent: list){
        videosOrd.add(ent.getKey());
      }

      return videosOrd;
  }
  // Guarda los datos de las visitas cuando se cierra la app
  public void guardarDatos(SharedPreferences.Editor datos){
      List<Map.Entry<String , Integer>> list = new ArrayList<Map.Entry<String, Integer>>(views.entrySet());
      for(Map.Entry<String,Integer> ent:list) {
          datos.putInt(ent.getKey(),ent.getValue());
      }
  }

  //cuando se abre la aplicacion carga los datos de las visitas
  @RequiresApi(api = Build.VERSION_CODES.N)
  public void leerDatos(SharedPreferences datos){

      Map<String,?> aux=datos.getAll();
      for(Map.Entry<String,?> ent: aux.entrySet()){
          views.replace(ent.getKey(),(Integer)ent.getValue());
      }
  }


}
