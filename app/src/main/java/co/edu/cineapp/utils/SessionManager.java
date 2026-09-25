package co.edu.cineapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private final SharedPreferences prefs; //objeto que lee o escribe el archivo de preferencias

    public SessionManager(Context context){
        // sesion_prefs = nombre del archivo; MODE_PRIVATE = solo esta app puede leerlo
        prefs = context.getApplicationContext().getSharedPreferences(Constants.PREFS_SESION, Context.MODE_PRIVATE);
    }

    public void guardarSesion(String correo){
        prefs.edit().putString(Constants.KEY_CORREO, correo).apply(); //guarda la forma asíncrona
    }

    public String getCorreo() {
        return prefs.getString(Constants.KEY_CORREO, null);
    }
    public boolean haySesionActiva() {
        return getCorreo() != null;
    }
    public void cerrarSesion(){
        prefs.edit().clear().apply();//borratodo lo guardado
    }
}
