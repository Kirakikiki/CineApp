// Indica en qué paquete está esta interfaz.
package co.edu.cineapp.data.remote;

// Permite trabajar con listas.
import java.util.List;

// Importamos las clases que vamos a enviar o recibir.
import co.edu.cineapp.data.entities.Genero;
import co.edu.cineapp.data.entities.Pelicula;
import co.edu.cineapp.data.entities.PromedioResena;
import co.edu.cineapp.data.entities.Resena;
import co.edu.cineapp.data.entities.User;

// Clases de Retrofit para hacer las peticiones.
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;


// Aquí ponemos las rutas que tiene nuestra API.
public interface ApiService {


    // =========================
    // PELÍCULAS
    // =========================

    // GET sirve para consultar datos.
    // Aquí pedimos todas las películas.
    @GET("api/peliculas")
    Call<List<Pelicula>> listarPeliculas();


    // Busca una película usando su ID.
    @GET("api/peliculas/{id}")
    Call<Pelicula> obtenerPelicula(
            @Path("id") Long id
    );


    // POST sirve para crear una película.
    @POST("api/peliculas")
    Call<Pelicula> crearPelicula(
            @Body Pelicula pelicula
    );


    // PUT sirve para actualizar una película.
    @PUT("api/peliculas/{id}")
    Call<Pelicula> actualizarPelicula(
            @Path("id") Long id,
            @Body Pelicula pelicula
    );


    // DELETE sirve para eliminar una película.
    @DELETE("api/peliculas/{id}")
    Call<Void> eliminarPelicula(
            @Path("id") Long id
    );


    // =========================
    // GÉNEROS
    // =========================

    // Consulta todos los géneros.
    @GET("api/generos")
    Call<List<Genero>> listarGeneros();


    // =========================
    // USUARIOS
    // =========================

    // Busca un usuario usando su Firebase UID.
    @GET("api/usuarios/firebase/{firebaseUid}")
    Call<User> obtenerUsuarioPorFirebaseUid(
            @Path("firebaseUid") String firebaseUid
    );


    // Crea un nuevo usuario.
    @POST("api/usuarios")
    Call<User> crearUsuario(
            @Body User usuario
    );


    // =========================
    // RESEÑAS
    // =========================

    // Busca las reseñas de una película.
    @GET("api/resenas/pelicula/{peliculaId}")
    Call<List<Resena>> listarResenasDePelicula(
            @Path("peliculaId") Long peliculaId
    );


    // Busca el promedio de las reseñas de una película.
    @GET("api/resenas/pelicula/{peliculaId}/promedio")
    Call<PromedioResena> promedioDePelicula(
            @Path("peliculaId") Long peliculaId
    );


    // Crea una nueva reseña.
    @POST("api/resenas")
    Call<Resena> crearResena(
            @Body Resena resena
    );


    // Elimina una reseña.
    @DELETE("api/resenas/{id}")
    Call<Void> eliminarResena(
            @Path("id") Long id
    );
}