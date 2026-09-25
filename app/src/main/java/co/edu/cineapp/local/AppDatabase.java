package co.edu.cineapp.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {MiPeliculaEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract MiPeliculaDao miPeliculaDao();

    private static volatile AppDatabase INSTANCE; //una sola instancia para toda la app

    public static AppDatabase get(Context context){
        if (INSTANCE == null){
            synchronized (AppDatabase.class){ //evita que dos hilos la creen a la vez
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "cineapp_room.db" //nombre de archivo distinto al "cineapp.db" de ManagerDataBase
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}
