package co.edu.cineapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

import co.edu.cineapp.data.local.MiPeliculaEntity;
import co.edu.cineapp.data.model.MiPeliculaRepository;
import co.edu.cineapp.utils.SessionManager;

public class PeliculaActivity extends AppCompatActivity {

    // =========================================================
    // VISTAS
    // =========================================================

    private ImageButton btnFavorite;

    private TextView tvTituloPeli;
    private TextView cpYear;
    private TextView cpGenre;
    private TextView cpDuration;
    private TextView tvDirector;
    private TextView tvSinopsis;
    private TextView tvPromedio;

    private AppCompatRatingBar rbCalificacion;

    private MaterialButton btnPendiente;
    private MaterialButton btnVista;

    private Button btnResena;


    // =========================================================
    // DATOS DE LA PELÍCULA
    // =========================================================

    private Long peliculaId;
    private String titulo;
    private String sinopsis;
    private String anio;
    private String genero;
    private String duracion;
    private String clasificacion;
    private String posterUrl;
    private String trailerUrl;


    // =========================================================
    // ESTADO DE LA PELÍCULA
    // =========================================================

    /*
     * false = no es favorita → ♡
     * true  = es favorita     → ♥
     */
    private boolean esFavorita = false;

    private boolean estaPendiente = false;
    private boolean estaVista = false;
    private MiPeliculaRepository miPeliculaRepository;
    private SessionManager sessionManager;
    private MiPeliculaEntity peliculaGuardada;


    // =========================================================
    // ON CREATE
    // =========================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_pelicula);

        configurarVentana();
        iniciarComponentes();
        miPeliculaRepository = new MiPeliculaRepository(this);
        sessionManager = new SessionManager(this);
        recibirDatos();
        mostrarDatos();
        cargarEstadoFavorito();
        configurarEventos();
    }


    // =========================================================
    // CONFIGURACIÓN DE LA VENTANA
    // =========================================================

    private void configurarVentana() {

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (v, insets) -> {

                    Insets systemBars =
                            insets.getInsets(
                                    WindowInsetsCompat.Type.systemBars()
                            );

                    v.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );

                    return insets;
                }
        );
    }


    // =========================================================
    // INICIALIZAR COMPONENTES
    // =========================================================

    private void iniciarComponentes() {

        btnFavorite = findViewById(R.id.btnFavorite);

        tvTituloPeli = findViewById(R.id.tvTituloPeli);
        cpYear = findViewById(R.id.cpYear);
        cpGenre = findViewById(R.id.cpGenre);
        cpDuration = findViewById(R.id.cpDuration);

        tvDirector = findViewById(R.id.tvDirector);
        tvSinopsis = findViewById(R.id.tvSinopsis);
        tvPromedio = findViewById(R.id.tvPromedio);

        rbCalificacion = findViewById(R.id.rbCalificacion);

        btnPendiente = findViewById(R.id.btnPendiente);
        btnVista = findViewById(R.id.btnVista);

        btnResena = findViewById(R.id.btnReseña);
    }


    // =========================================================
    // RECIBIR DATOS DESDE CATALOGO
    // =========================================================

    private void recibirDatos() {

        Intent intent = getIntent();

        // ID
        if (intent.hasExtra("id")) {

            peliculaId = intent.getLongExtra(
                    "id",
                    -1L
            );
        }

        // Datos de la película
        titulo = intent.getStringExtra("titulo");

        sinopsis = intent.getStringExtra("sinopsis");

        anio = intent.getStringExtra("anio");

        genero = intent.getStringExtra("genero");

        duracion = intent.getStringExtra("duracion");

        clasificacion =
                intent.getStringExtra("clasificacion");

        posterUrl =
                intent.getStringExtra("posterUrl");

        trailerUrl =
                intent.getStringExtra("trailerUrl");
    }


    // =========================================================
    // MOSTRAR INFORMACIÓN
    // =========================================================

    private void mostrarDatos() {

        // -----------------------------------------------------
        // TÍTULO
        // -----------------------------------------------------

        if (titulo != null && !titulo.isEmpty()) {

            tvTituloPeli.setText(titulo);

        } else {

            tvTituloPeli.setText("Película");
        }


        // -----------------------------------------------------
        // AÑO
        // -----------------------------------------------------

        if (anio != null && !anio.isEmpty()) {

            cpYear.setText(anio);

        } else {

            cpYear.setText("Sin año");
        }


        // -----------------------------------------------------
        // GÉNERO
        // -----------------------------------------------------

        if (genero != null && !genero.isEmpty()) {

            cpGenre.setText(genero);

        } else {

            cpGenre.setText("Sin género");
        }


        // -----------------------------------------------------
        // DURACIÓN
        // -----------------------------------------------------

        if (duracion != null && !duracion.isEmpty()) {

            cpDuration.setText(duracion);

        } else {

            cpDuration.setText("Sin duración");
        }


        // -----------------------------------------------------
        // SINOPSIS
        // -----------------------------------------------------

        if (sinopsis != null && !sinopsis.isEmpty()) {

            tvSinopsis.setText(sinopsis);

        } else {

            tvSinopsis.setText(
                    "No hay sinopsis disponible."
            );
        }


        // -----------------------------------------------------
        // DIRECTOR
        // -----------------------------------------------------

        /*
         * Actualmente Pelicula no tiene
         * un campo director.
         */

        tvDirector.setText(
                "Director: Información no disponible"
        );


        // -----------------------------------------------------
        // PROMEDIO
        // -----------------------------------------------------

        tvPromedio.setText("0.0");


        // -----------------------------------------------------
        // ESTADOS INICIALES
        // -----------------------------------------------------

        actualizarBotonFavorito();

        actualizarBotonPendiente();

        actualizarBotonVista();
    }


    // =========================================================
    // EVENTOS
    // =========================================================

    private void configurarEventos() {

        // -----------------------------------------------------
        // FAVORITO
        // -----------------------------------------------------

        btnFavorite.setOnClickListener(
                v -> cambiarFavorito()
        );


        // -----------------------------------------------------
        // PENDIENTE
        // -----------------------------------------------------

        btnPendiente.setOnClickListener(
                v -> cambiarPendiente()
        );


        // -----------------------------------------------------
        // VISTA
        // -----------------------------------------------------

        btnVista.setOnClickListener(
                v -> cambiarVista()
        );


        // -----------------------------------------------------
        // CALIFICACIÓN
        // -----------------------------------------------------

        rbCalificacion.setOnRatingBarChangeListener(
                (ratingBar, rating, fromUser) -> {

                    if (fromUser) {

                        guardarCalificacion(
                                rating
                        );
                    }
                }
        );


        // -----------------------------------------------------
        // RESEÑA
        // -----------------------------------------------------

        btnResena.setOnClickListener(
                v -> abrirResena()
        );
    }


    // =========================================================
    // FAVORITO
    // =========================================================

    private void cambiarFavorito() {

        /*
         * Cambiamos el estado:
         *
         * false → true
         * true  → false
         */

        esFavorita = !esFavorita;

        String correo = sessionManager.getCorreo();
        if (correo != null && peliculaId != null) {
            if (peliculaGuardada == null) {
                peliculaGuardada = new MiPeliculaEntity(
                        correo,
                        String.valueOf(peliculaId),
                        titulo,
                        esFavorita,
                        "NINGUNO"
                );
                miPeliculaRepository.guardar(peliculaGuardada);
            } else {
                peliculaGuardada.favorita = esFavorita;
                miPeliculaRepository.actualizar(peliculaGuardada);
            }
        }


        // Actualizar el icono
        actualizarBotonFavorito();


        // Mostrar mensaje al usuario

        if (esFavorita) {

            Toast.makeText(
                    this,
                    "Agregada a favoritos",
                    Toast.LENGTH_SHORT
            ).show();

        } else {

            Toast.makeText(
                    this,
                    "Eliminada de favoritos",
                    Toast.LENGTH_SHORT
            ).show();
        }


    }

    private void cargarEstadoFavorito() {
        String correo = sessionManager.getCorreo();
        if (correo == null || peliculaId == null) {
            return;
        }

        miPeliculaRepository.todas(correo).observe(this, peliculas -> {
            for (MiPeliculaEntity pelicula : peliculas) {
                if (String.valueOf(peliculaId).equals(pelicula.peliculaId)) {
                    peliculaGuardada = pelicula;
                    esFavorita = pelicula.favorita;
                    actualizarBotonFavorito();
                    return;
                }
            }
        });
    }


    // =========================================================
    // ACTUALIZAR BOTÓN FAVORITO
    // =========================================================

    private void actualizarBotonFavorito() {

        if (esFavorita) {

            /*
             * =============================================
             * FAVORITA
             * =============================================
             *
             * Mostramos el corazón LLENO.
             */

            btnFavorite.setImageResource(
                    R.drawable.baseline_favorite_24
            );

            btnFavorite.setContentDescription(
                    "Quitar de favoritos"
            );

        } else {

            /*
             * =============================================
             * NO FAVORITA
             * =============================================
             *
             * Mostramos el corazón VACÍO.
             */

            btnFavorite.setImageResource(
                    R.drawable.outline_favorite_24
            );

            btnFavorite.setContentDescription(
                    "Agregar a favoritos"
            );
        }
    }


    // =========================================================
    // PENDIENTE
    // =========================================================

    private void cambiarPendiente() {

        estaPendiente = !estaPendiente;

        actualizarBotonPendiente();

        if (estaPendiente) {

            Toast.makeText(
                    this,
                    "Película agregada a pendientes",
                    Toast.LENGTH_SHORT
            ).show();

        } else {

            Toast.makeText(
                    this,
                    "Película eliminada de pendientes",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }


    private void actualizarBotonPendiente() {

        if (estaPendiente) {

            btnPendiente.setText(
                    "Pendiente ✓"
            );

        } else {

            btnPendiente.setText(
                    "Pendiente"
            );
        }
    }


    // =========================================================
    // VISTA
    // =========================================================

    private void cambiarVista() {

        estaVista = !estaVista;

        actualizarBotonVista();

        if (estaVista) {

            Toast.makeText(
                    this,
                    "Marcada como vista",
                    Toast.LENGTH_SHORT
            ).show();

        } else {

            Toast.makeText(
                    this,
                    "Marcada como no vista",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }


    private void actualizarBotonVista() {

        if (estaVista) {

            btnVista.setText(
                    "Vista ✓"
            );

        } else {

            btnVista.setText(
                    "Vista"
            );
        }
    }


    // =========================================================
    // CALIFICACIÓN
    // =========================================================

    private void guardarCalificacion(float rating) {

        tvPromedio.setText(
                String.valueOf(rating)
        );

        Toast.makeText(
                this,
                "Calificación guardada: " + rating,
                Toast.LENGTH_SHORT
        ).show();


        /*
         * Posteriormente podemos guardar
         * la calificación del usuario en Firebase
         * o en tu API.
         */
    }


    // =========================================================
    // RESEÑA
    // =========================================================

    private void abrirResena() {

        Intent intent = new Intent(
                PeliculaActivity.this,
                ResenaActivity.class
        );


        // -----------------------------------------------------
        // ENVIAR ID
        // -----------------------------------------------------

        if (peliculaId != null) {

            intent.putExtra(
                    "id",
                    peliculaId
            );
        }


        // -----------------------------------------------------
        // ENVIAR TÍTULO
        // -----------------------------------------------------

        intent.putExtra(
                "titulo",
                titulo
        );


        // Abrir pantalla de reseña
        startActivity(intent);
    }
}
