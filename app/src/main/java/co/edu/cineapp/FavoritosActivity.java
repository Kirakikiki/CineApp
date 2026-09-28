package co.edu.cineapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import co.edu.cineapp.data.entities.Pelicula;
import co.edu.cineapp.data.local.MiPeliculaEntity;
import co.edu.cineapp.data.model.MiPeliculaRepository;
import co.edu.cineapp.data.model.PeliculaRepository;
import co.edu.cineapp.utils.SessionManager;

public class FavoritosActivity extends AppCompatActivity {

    private LinearLayout listaFavoritas;
    private TextView estadoFavoritas;
    private MiPeliculaRepository miPeliculaRepository;
    private PeliculaRepository peliculaRepository;
    private SessionManager sessionManager;
    private List<Pelicula> peliculas = new ArrayList<>();
    private Set<String> idsFavoritos = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_favoritos);

    estadoFavoritas = findViewById(R.id.estadoFavoritas);
    LinearLayout contenedor = (LinearLayout) ((android.widget.ScrollView) findViewById(R.id.scrollFavoritas)).getChildAt(0);
    contenedor.removeAllViews();
    listaFavoritas = contenedor;
    miPeliculaRepository = new MiPeliculaRepository(this);
    peliculaRepository = new PeliculaRepository();
    sessionManager = new SessionManager(this);

    cargarFavoritos();

        BottomNavigationView bottomNavigation =
                findViewById(R.id.bottomNavigation);

        bottomNavigation.setSelectedItemId(R.id.nav_favoritas);

        bottomNavigation.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.nav_catalogos) {
                startActivity(new Intent(this, CatalogoActivity.class));
                return true;

            } else if (id == R.id.nav_favoritas) {

                // Ya estamos en favoritos
                return true;

            } else if (id == R.id.nav_mi_cine) {
                startActivity(new Intent(this, MicineActivity.class));
                return true;
            }

            return false;
        });
    }

    private void cargarFavoritos() {
        String correo = sessionManager.getCorreo();
        if (correo == null || correo.trim().isEmpty()) {
            mostrarEstado("Inicia sesión para ver tus favoritas");
            return;
        }

        miPeliculaRepository.favoritas(correo).observe(this, favoritas -> {
            idsFavoritos = new HashSet<>();
            for (MiPeliculaEntity favorita : favoritas) {
                idsFavoritos.add(favorita.peliculaId);
            }
            renderizarFavoritos();
        });

        peliculaRepository.getPeliculas(new co.edu.cineapp.data.remote.ApiCallback<List<Pelicula>>() {
            @Override
            public void onSuccess(List<Pelicula> resultado) {
                runOnUiThread(() -> {
                    peliculas = resultado != null ? resultado : new ArrayList<>();
                    renderizarFavoritos();
                });
            }

            @Override
            public void onError(String mensaje) {
                runOnUiThread(() -> mostrarEstado(mensaje));
            }
        });
    }

    private void renderizarFavoritos() {
        if (peliculas.isEmpty()) {
            return;
        }

        listaFavoritas.removeAllViews();
        int cantidad = 0;
        for (Pelicula pelicula : peliculas) {
            if (pelicula.getId() != null && idsFavoritos.contains(String.valueOf(pelicula.getId()))) {
                listaFavoritas.addView(crearTarjeta(pelicula));
                cantidad++;
            }
        }

        if (cantidad == 0) {
            mostrarEstado("Aún no tienes películas favoritas");
        } else {
            estadoFavoritas.setVisibility(View.VISIBLE);
        }
    }

    private MaterialCardView crearTarjeta(Pelicula pelicula) {
        MaterialCardView tarjeta = new MaterialCardView(this);
        tarjeta.setRadius(22);
        tarjeta.setCardBackgroundColor(0xFFFFF7EB);
        tarjeta.setUseCompatPadding(true);

        LinearLayout fila = new LinearLayout(this);
        fila.setGravity(android.view.Gravity.CENTER_VERTICAL);
        fila.setPadding(18, 12, 8, 12);

        TextView titulo = new TextView(this);
        titulo.setText(pelicula.getTitulo() != null ? pelicula.getTitulo() : "Sin título");
        titulo.setTextColor(0xFF160B4A);
        titulo.setTextSize(18);
        titulo.setTypeface(null, android.graphics.Typeface.BOLD);
        fila.addView(titulo, new LinearLayout.LayoutParams(0, 96, 1));

        ImageButton quitar = new ImageButton(this);
        quitar.setImageResource(R.drawable.baseline_favorite_24);
        quitar.setContentDescription("Quitar de favoritos");
        quitar.setOnClickListener(v -> {
            String correo = sessionManager.getCorreo();
            miPeliculaRepository.favoritas(correo).observe(this, favoritas -> {
                for (MiPeliculaEntity favorita : favoritas) {
                    if (String.valueOf(pelicula.getId()).equals(favorita.peliculaId)) {
                        favorita.favorita = false;
                        miPeliculaRepository.actualizar(favorita);
                        break;
                    }
                }
            });
        });
        fila.addView(quitar, new LinearLayout.LayoutParams(52, 52));

        tarjeta.addView(fila);
        tarjeta.setOnClickListener(v -> abrirPelicula(pelicula));
        return tarjeta;
    }

    private void abrirPelicula(Pelicula pelicula) {
        Intent intent = new Intent(this, PeliculaActivity.class);
        intent.putExtra("id", pelicula.getId());
        intent.putExtra("titulo", pelicula.getTitulo());
        intent.putExtra("sinopsis", pelicula.getSinopsis());
        intent.putExtra("anio", pelicula.getAnio());
        intent.putExtra("genero", pelicula.getNombreGenero());
        intent.putExtra("duracion", pelicula.getDuracionTexto());
        intent.putExtra("clasificacion", pelicula.getClasificacion());
        intent.putExtra("posterUrl", pelicula.getPosterUrl());
        intent.putExtra("trailerUrl", pelicula.getTrailerUrl());
        startActivity(intent);
    }

    private void mostrarEstado(String mensaje) {
        listaFavoritas.removeAllViews();
        TextView estado = new TextView(this);
        estado.setText(mensaje);
        estado.setTextColor(0xFFFFFFFF);
        estado.setTextSize(16);
        estado.setPadding(16, 24, 16, 24);
        listaFavoritas.addView(estado);
    }
}