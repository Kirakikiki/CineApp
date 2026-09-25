package co.edu.cineapp.local;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

/**
 * Actua como intermediario entre la capa de presentacion (Activities/ViewModels)
 * y la fuente de datos local (Room Database). Centraliza el acceso a los datos
 * y oculta la complejidad del manejo de hilos y consultas
 */
public class MiPeliculaRepository {
    //DAO(Data Acces Object): tiene los metodos directos para interactuar con la base de datos Room
    private final MiPeliculaDao dao;
    //Manejador de hilos: permite ejecutar tareas en segundo plano
    private final AppExecutors executors;

    /**
     * Constructor del repositorio, inicializa la conexion
     * a la base de datos y al administrador de hilos*/
    public MiPeliculaRepository(Context context){
        this.dao = AppDatabase.get(context).miPeliculaDao(); //obtiene la instancia
        this.executors = AppExecutors.getInstance();
    }

    public void guardar(MiPeliculaEntity miPeliculaEntity){
        executors.diskIO.execute(() -> dao.guardar(miPeliculaEntity));
    }
    public void actualizar(MiPeliculaEntity miPeliculaEntity){
        executors.diskIO.execute(() -> dao.actualizar(miPeliculaEntity));
    }
    public void eliminar(MiPeliculaEntity miPeliculaEntity){
        executors.diskIO.execute(() -> dao.eliminar(miPeliculaEntity));
    }
    public LiveData<List<MiPeliculaEntity>> favoritas(String u){
        return dao.favoritas(u);
    }
    public LiveData<List<MiPeliculaEntity>> todas(String u){
        return dao.todas(u);
    }

}
