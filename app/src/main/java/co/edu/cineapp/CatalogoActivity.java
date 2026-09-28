package co.edu.cineapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.TextView;
import co.edu.cineapp.data.remote.ApiCallback;
import android.widget.Toast;
import co.edu.cineapp.data.entities.Pelicula;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.AsyncListUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import co.edu.cineapp.data.model.FavoritoRepository;
import co.edu.cineapp.ui.adapter.PeliculaAdapter;
import co.edu.cineapp.data.entities.Pelicula;
import co.edu.cineapp.data.model.PeliculaRepository;

//PeliculaAdapter. Listener es el activity se que tiene los metodos
public class CatalogoActivity extends AppCompatActivity {
    private PeliculaRepository peliculaRepository;
    private RecyclerView rvPeliculas; //Cuadricula de peliculas
    private ChipGroup chipGroup; //Chips de género
    private Chip chip;
    private SearchView svSearch; //Buscador
    private TextView tvError; //Mensaje "sin resultados"
    private Button btnVerMas; //Boton de la tarjeta destacada
    private BottomNavigationView btnNav; //Menu de navegacion inferior

    private final List<Pelicula> todasLasPeliculas = new ArrayList<>(); //Lista completa de pelicuals sin filtrar
    private PeliculaAdapter adaptador; //Adsptador del recyclerview

    private String generoActual = "Todas"; // Genero seleccionado en los chips
    private String textoBusqueda = ""; //Texto escrito en el buscador
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_catalogo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initObjects();
        configurarLista();
        cargarPeliculas();
        chipGroup.setOnCheckedStateChangeListener(this::seleccionarGenero);
        btnVerMas.setOnClickListener(this::abrirDestacada);
        btnNav.setOnItemSelectedListener(this::seleccionarMenu);
    }

    private void configurarLista(){ //Prepara el ReclyclerView
        adaptador = new PeliculaAdapter(new ArrayList<>(todasLasPeliculas), this::abrirInformacion); //Crear el adaptador; al tocar una tarjeta llama a abrirInformacion
        rvPeliculas.setLayoutManager(new GridLayoutManager(this, 2)); //Cuadrícula de 2 columnas
        rvPeliculas.setAdapter(adaptador); //Conecta el adaptador

    }
    private void cargarPeliculas() {
        peliculaRepository.getPeliculas(new ApiCallback<List<Pelicula>>() {
            @Override
            public void onSuccess(List<Pelicula> data) {
                todasLasPeliculas.clear();
                if (data != null) {
                    todasLasPeliculas.addAll(data);
                }
                aplicarFiltros(); // Actualiza el RecyclerView y chips
            }
            @Override
            public void onError(String error) {
                Toast.makeText(CatalogoActivity.this, error, Toast.LENGTH_LONG).show();
                tvError.setVisibility(View.VISIBLE);
                tvError.setText(error);
            }
        });
    }

    private void seleccionarGenero(ChipGroup grupo, List<Integer> idSelececciondados) { //Se llama cuando cambia el chip seleccionado
        if (idSelececciondados.isEmpty()){ //Por seguridad: si no hay ningun chip seleccionado
            return;
        }
        int idChip = idSelececciondados.get(0); //toma el id del chip seleccionado
        Chip chip = grupo.findViewById(idChip); //Busca el chip dentro del grupo
        generoActual = chip.getText().toString(); // Guarda el genero
        aplicarFiltros(); //Actualiza la lista con el nuevo genero
    }

    private void abrirDestacada(View view){//metodo para el boton vermas
        if (!todasLasPeliculas.isEmpty()) {//Evita un error si la lista esta vacia
            abrirInformacion(todasLasPeliculas.get(0));
        }
    }

    private void buscar(String texto) {//Guarda el texto buscado
        textoBusqueda = texto.trim().toLowerCase(); //Sin espacios y en minusculas
        aplicarFiltros(); //Actualiza la lista
    }
    private void aplicarFiltros(){ //Combina genero y busqueda
        List<Pelicula> filtradas = new ArrayList<>(); //Lista vacia para resultados
        boolean todas = generoActual.equalsIgnoreCase("Todas");//revisa si "Todas" esta sleeccionado

        for (Pelicula pelicula : todasLasPeliculas) { //Revisa cada pelicula
            boolean coincideGenero = todas || pelicula.getGenero().getNombre().equalsIgnoreCase(generoActual);//revisa si el genro si coincide
            boolean coincideTitulo = pelicula.getTitulo().toLowerCase().contains(textoBusqueda); // revisa si el titulo tiene lo que se esta buscando
            if (coincideGenero && coincideTitulo) {
                filtradas.add(pelicula); //agrega la pelicula
            }
        }
        adaptador.actualizarLista(filtradas);//Muestra los resultados
        tvError.setVisibility(filtradas.isEmpty() ? View.VISIBLE : View.GONE); //Muestra el error solo si no hay nada
    }

    private void abrirInformacion(Pelicula pelicula){ //Abre la informacion (lo usa el adaptador y el boton)
        Intent intent = new Intent(this, PeliculaActivity.class) ; //Crea el intent Conexion a la pantalla
        intent.putExtra("titulo", pelicula.getTitulo()); //Envia el titulo
        intent.putExtra("sinopsis", pelicula.getSinopsis());
        intent.putExtra("anio", pelicula.getAnio());
        intent.putExtra("genero", pelicula.getGenero().getNombre());
        intent.putExtra("duracion", pelicula.getDuracionMinutos());
        startActivity(intent);//abre la pantalla
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

    private void initObjects(){
        rvPeliculas = findViewById(R.id.rvPeliculas);
        chipGroup = findViewById(R.id.chipGroup);

        svSearch = findViewById(R.id.svSearch);
        tvError = findViewById(R.id.tvError);
        btnVerMas = findViewById(R.id.btnVerMas);
        btnNav = findViewById(R.id.btnNav);

        peliculaRepository = new PeliculaRepository();
    }
}