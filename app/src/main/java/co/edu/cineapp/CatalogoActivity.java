package co.edu.cineapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

import co.edu.cineapp.data.entities.Pelicula;
import co.edu.cineapp.data.model.PeliculaRepository;
import co.edu.cineapp.data.remote.ApiCallback;
import co.edu.cineapp.ui.adapter.PeliculaAdapter;

public class CatalogoActivity extends AppCompatActivity {

    private RecyclerView rvPeliculas;
    private ChipGroup chipGroup;
    private SearchView svSearch;
    private TextView tvError;
    private Button btnVerMas;
    private BottomNavigationView btnNav;

    // Lista con todas las películas que vienen de la API
    private final List<Pelicula> todasLasPeliculas = new ArrayList<>();

    private PeliculaAdapter adaptador;

    // Filtros
    private String generoActual = "Todas";
    private String textoBusqueda = "";

    // Repositorio que se comunica con la API
    private PeliculaRepository peliculaRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_catalogo);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (v, insets) -> {

                    Insets systemBars =
                            insets.getInsets(WindowInsetsCompat.Type.systemBars());

                    v.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );

                    return insets;
                }
        );

        // Inicializar componentes
        initObjects();

        // Crear repositorio
        peliculaRepository = new PeliculaRepository();

        // Configurar RecyclerView
        configurarLista();

        // Configurar chips
        configurarChips();

        // Configurar buscador
        configurarBuscador();

        // Cargar películas desde la API
        cargarPeliculas();
    }


    // =========================================================
    // INICIALIZAR VISTAS
    // =========================================================

    private void initObjects() {

        rvPeliculas = findViewById(R.id.rvPeliculas);

        chipGroup = findViewById(R.id.chipGroup);

        svSearch = findViewById(R.id.svSearch);

        tvError = findViewById(R.id.tvError);

        btnVerMas = findViewById(R.id.btnVerMas);

        btnNav = findViewById(R.id.btnNav);
    }


    // =========================================================
    // CONFIGURAR RECYCLERVIEW
    // =========================================================

    private void configurarLista() {

        /*
         * El Adapter recibirá la lista de películas.
         *
         * Cuando el usuario toque una película,
         * se ejecutará abrirInformacion().
         */

        adaptador = new PeliculaAdapter(
                new ArrayList<>(),
                this::abrirInformacion
        );

        // Dos columnas
        rvPeliculas.setLayoutManager(
                new GridLayoutManager(this, 2)
        );

        rvPeliculas.setAdapter(adaptador);
    }


    // =========================================================
    // CARGAR PELÍCULAS DESDE LA API
    // =========================================================

    private void cargarPeliculas() {

        // Mostrar mensaje mientras carga
        tvError.setVisibility(View.GONE);

        peliculaRepository.getPeliculas(
                new ApiCallback<List<Pelicula>>() {

                    @Override
                    public void onSuccess(List<Pelicula> resultado) {

                        // Limpiar lista anterior
                        todasLasPeliculas.clear();

                        // Agregar películas recibidas de la API
                        if (resultado != null) {
                            todasLasPeliculas.addAll(resultado);
                        }

                        // Mostrar películas
                        aplicarFiltros();
                    }

                    @Override
                    public void onError(String mensaje) {

                        Toast.makeText(
                                CatalogoActivity.this,
                                "No se pudieron cargar las películas: "
                                        + mensaje,
                                Toast.LENGTH_LONG
                        ).show();

                        tvError.setText(
                                "No se pudieron cargar las películas."
                        );

                        tvError.setVisibility(View.VISIBLE);
                    }
                }
        );
    }


    // =========================================================
    // CONFIGURAR CHIPS DE GÉNERO
    // =========================================================

    private void configurarChips() {

        /*
         * IMPORTANTE:
         *
         * No usamos setOnClickListener() en ChipGroup.
         *
         * ChipGroup tiene su propio listener para saber
         * cuál Chip fue seleccionado.
         */

        chipGroup.setOnCheckedStateChangeListener(
                (group, checkedIds) -> {

                    if (checkedIds == null || checkedIds.isEmpty()) {

                        generoActual = "Todas";

                        aplicarFiltros();

                        return;
                    }

                    // Obtener el ID del chip seleccionado
                    int idChip = checkedIds.get(0);

                    Chip chipSeleccionado =
                            group.findViewById(idChip);

                    if (chipSeleccionado != null) {

                        generoActual =
                                chipSeleccionado
                                        .getText()
                                        .toString();

                        aplicarFiltros();
                    }
                }
        );
    }


    // =========================================================
    // CONFIGURAR BUSCADOR
    // =========================================================

    private void configurarBuscador() {

        svSearch.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {

                    @Override
                    public boolean onQueryTextSubmit(String query) {

                        buscar(query);

                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {

                        buscar(newText);

                        return true;
                    }
                }
        );
    }


    // =========================================================
    // BUSCAR PELÍCULA
    // =========================================================

    private void buscar(String texto) {

        if (texto == null) {
            textoBusqueda = "";
        } else {
            textoBusqueda =
                    texto.trim().toLowerCase();
        }

        aplicarFiltros();
    }


    // =========================================================
    // APLICAR FILTROS
    // =========================================================

    private void aplicarFiltros() {

        List<Pelicula> filtradas =
                new ArrayList<>();

        boolean todas =
                generoActual.equalsIgnoreCase("Todas");


        for (Pelicula pelicula : todasLasPeliculas) {

            if (pelicula == null) {
                continue;
            }

            // -----------------------------------------
            // FILTRO POR GÉNERO
            // -----------------------------------------

            boolean coincideGenero;

            if (todas) {

                coincideGenero = true;

            } else {

                /*
                 * IMPORTANTE:
                 *
                 * Ahora genero es un objeto:
                 *
                 * genero {
                 *     id
                 *     nombre
                 * }
                 *
                 * Por eso usamos getNombreGenero().
                 */

                String nombreGenero =
                        pelicula.getNombreGenero();

                coincideGenero =
                        nombreGenero != null
                                && nombreGenero.equalsIgnoreCase(
                                generoActual
                        );
            }


            // -----------------------------------------
            // FILTRO POR TÍTULO
            // -----------------------------------------

            String titulo =
                    pelicula.getTitulo();

            boolean coincideTitulo;

            if (titulo == null) {

                coincideTitulo =
                        textoBusqueda.isEmpty();

            } else {

                coincideTitulo =
                        titulo
                                .toLowerCase()
                                .contains(textoBusqueda);
            }


            // -----------------------------------------
            // AGREGAR SI COINCIDE
            // -----------------------------------------

            if (coincideGenero && coincideTitulo) {

                filtradas.add(pelicula);
            }
        }


        // Actualizar RecyclerView
        if (adaptador != null) {

            adaptador.actualizarLista(
                    filtradas
            );
        }


        // Mostrar/ocultar mensaje de error
        if (filtradas.isEmpty()) {

            tvError.setText(
                    "No encontramos películas con esos criterios."
            );

            tvError.setVisibility(
                    View.VISIBLE
            );

        } else {

            tvError.setVisibility(
                    View.GONE
            );
        }
    }


    // =========================================================
    // ABRIR INFORMACIÓN DE PELÍCULA
    // =========================================================

    private void abrirInformacion(Pelicula pelicula) {

        if (pelicula == null) {
            return;
        }

        /*
         * Por ahora mostramos la información básica.
         *
         * Después podemos conectar este método con la pantalla
         * de detalle de la película.
         */

        String titulo =
                pelicula.getTitulo();

        String genero =
                pelicula.getNombreGenero();

        String duracion =
                pelicula.getDuracionTexto();

        String mensaje =
                titulo
                        + "\n"
                        + genero
                        + "\n"
                        + duracion;

        Toast.makeText(
                this,
                mensaje,
                Toast.LENGTH_SHORT
        ).show();
    }

    private  boolean seleccionarMenu(MenuItem item){ //decide a cual pantalla abrir segun el item seleccionado
        int id = item.getItemId(); //obtiene el id del item
        if (id == R.id.nav_catalogos) { //si selecciona catalogo
            return true; //pasa a la pantalla
        } else if (id == R.id.nav_favoritas) {
            startActivity(new Intent(this, FavoritosActivity.class));// abre favoritas
            return true;
        } else if (id == R.id.nav_mi_cine) {
            startActivity(new Intent(this, MicineActivity.class));
            return true;
        }
        return false;
    }
}