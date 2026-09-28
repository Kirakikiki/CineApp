package co.edu.cineapp.edu.data.remote;

import java.util.List;

import co.edu.cineapp.edu.data.model.Genero;
import co.edu.cineapp.edu.data.model.Pelicula;
import co.edu.cineapp.edu.data.model.PromedioResena;
import co.edu.cineapp.edu.data.model.Resena;
import co.edu.cineapp.edu.data.model.Usuario;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.DELETE;

// Un metodo por cada endpoint de cineapp-api. Los nombres y rutas deben
// coincidir EXACTAMENTE con los @RequestMapping del backend.
public interface ApiService {

    // ---- Peliculas ----
    @GET("api/peliculas")
    Call<List<Pelicula>> listarPeliculas();

    @GET("api/peliculas/{id}")
    Call<Pelicula> obtenerPelicula(@Path("id") Long id);

    @POST("api/peliculas")
    Call<Pelicula> crearPelicula(@Body Pelicula pelicula);

    @PUT("api/peliculas/{id}")
    Call<Pelicula> actualizarPelicula(@Path("id") Long id, @Body Pelicula pelicula);

    @DELETE("api/peliculas/{id}")
    Call<Void> eliminarPelicula(@Path("id") Long id);

    // ---- Generos ----
    @GET("api/generos")
    Call<List<Genero>> listarGeneros();

    // ---- Usuarios ----
    @GET("api/usuarios/firebase/{firebaseUid}")
    Call<Usuario> obtenerUsuarioPorFirebaseUid(@Path("firebaseUid") String firebaseUid);

    @POST("api/usuarios")
    Call<Usuario> crearUsuario(@Body Usuario usuario);

    // ---- Resenas ----
    @GET("api/resenas/pelicula/{peliculaId}")
    Call<List<Resena>> listarResenasDePelicula(@Path("peliculaId") Long peliculaId);

    @GET("api/resenas/pelicula/{peliculaId}/promedio")
    Call<PromedioResena> promedioDePelicula(@Path("peliculaId") Long peliculaId);

    @POST("api/resenas")
    Call<Resena> crearResena(@Body Resena resena);

    @DELETE("api/resenas/{id}")
    Call<Void> eliminarResena(@Path("id") Long id);
}