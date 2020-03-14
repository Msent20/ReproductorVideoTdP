package Adapter;


import java.util.HashMap;
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


}
