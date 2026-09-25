package co.edu.cineapp.local;

import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import android.os.Handler;

//Clase para no bloquear la UI
public class AppExecutors {
    private static AppExecutors instancia;
    public final Executor diskIO = Executors.newSingleThreadExecutor(); //hilo solo para la base de datos
    public final Handler mainThread = new Handler(Looper.getMainLooper()); //para volver al hilo principal

    public static synchronized AppExecutors getInstance(){
        if (instancia == null) instancia = new AppExecutors();
        return instancia;
    }
}
