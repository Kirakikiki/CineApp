package co.edu.cineapp.local;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "mi_pelicula") //  Room crea esta tabla dentro de SQLite
public class MiPeliculaEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;  //clave primaria autogenerada

    @NonNull public String usuarioId; // correo del usuario
    @NonNull public String peliculaId; //id de la película
    public  String titulo;  // seguarda para no buscarlo cada vez
    public boolean favorita; //true= marcada con el corazón
    public String estado;   //"PENDIENTE", "VISTA" o "NINGUNO"

    public MiPeliculaEntity(@NonNull String usuarioId, @NonNull String peliculaId, String titulo, boolean favorita, String estado){
        this.usuarioId = usuarioId;
        this.peliculaId = peliculaId;
        this.titulo = titulo;
        this.favorita = favorita;
        this.estado = estado;
    }
}
