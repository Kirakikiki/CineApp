package co.edu.cineapp.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MiPeliculaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void guardar(MiPeliculaEntity miPeliculaEntity);  // CREATE, si ya existe la fila la remplaza

    @Update
    void actualizar(MiPeliculaEntity miPeliculaEntity);//UPDATE: cambiar corazon o estado

    @Delete
    void eliminar(MiPeliculaEntity miPeliculaEntity); //DELETE

    @Query("SELECT * FROM mi_pelicula WHERE usuarioId = :u AND favorita = 1")
    LiveData<List<MiPeliculaEntity>> favoritas(String u); //Read, listad e favoritas

    @Query("SELECT * FROM mi_pelicula WHERE usuarioId = :u")
    LiveData<List<MiPeliculaEntity>> todas(String u); //read, todas las filas del usuario
}
