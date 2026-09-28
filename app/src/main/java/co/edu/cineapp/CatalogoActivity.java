package co.edu.cineapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.AsyncListUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
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
public class CatalogoActivity extends AppCompatActivity implements PeliculaAdapter.Listener{
    //"todas"= lista completa de peliculas sin filtar
    private final List<Pelicula> todas = new ArrayList<>();
    // id de las peloiculas favoritas del usuario
    private Set<Integer> favoritasId = new HashSet<>();
    // Filtro seleccionado actualmente (todas osea sin filtrar)
    private String generoSeleccionado =  "Todas";

    //Texto escrito en el buscador (en minúsculas para comparar sin importar las mayusculas)
    private String query = "";

   //Pelicula mostrada en Destacada de la semana, se guarda para poder usarla en el botno de ver mas
    private Pelicula destacada;

    // elementos
    private SearchView buscar;
    private ChipGroup chipGroup;
    private ImageView imgDestacada;
    private TextView tituloDestacado;
    private TextView infoDestacado;
    private TextView mensaje;
    private RatingBar rbDestacada;
    private Button verMas;
    private RecyclerView rvPeliculas;
    private BottomNavigationView bottomNavigationView;

    private PeliculaAdapter adapter; //adpatador de la cuadrícula
    private PeliculaRepository peliculaRepository;//acceso a datos de peliculas
    private FavoritoRepository favoritoRepository; //acceso a datos de favoritos

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
        // Chips de genero: recorre el chipGroup y todos usan el mismo metod onClick
        for (int i = 0; i < chipGroup.getChildCount(); i++){
            chipGroup.getChildAt(i).setOnClickListener(this::);
        }
    }

    // Chips de genero: recorre el chipGroup y todos usan el mismo metod onClick

    /**
     * onResume: se ejecuta cada vez que la pantalla vuelve a ser visible*/
    @Override
    protected void onResume(){
        super.onResume();;
        cargarDatos();
    }

    //metodo de reposirorios
    private void initRepositorios(){
        peliculaRepository = new PeliculaRepository();
        favoritoRepository = new FavoritoRepository();
    }
    //Configura el reciclerview, distribucion de la cuadricula y el adapter
    private void setupRecycler(){
        //GridLayoutManager = cuadricula de 2 columnas
        rvPeliculas.setLayoutManager(new GridLayoutManager(this, 2));
        //this es el listener porque se esta implementando PeliculaAdapter.Listener
        adapter = new PeliculaAdapter(this);
        rvPeliculas.setAdapter(adapter); // coneccion del adapter al recyclerview

    }

    //Carga de datos
    //1. Pedir los favoritos
    private void cargarDatos(){
        favoritoRepository.getFavoritasId (new AsyncListUtil.DataCallback<Set<Integer>>() {
            @Override
            public void onSuccess(Set<Integer>id){
                onFavoritosCargados(id);
            }
            @Override
            public void onError(String message) {
                mostrarError(message);
            }
        });
    }
    //2. guargar los favoritos y pedir peliculas
    private void onFavoritosCargados(Set<Integer>id) {
        favoritasId = new HashSet<>(id); //Copia del conjunto
        peliculaRepository.getPeliculas(new DataCallback<List<Pelicula>>(){
            @Override
            public void onSuccess(List<Pelicula> data){
                onPeliculasCargadas(data);
            }
            @Override
            public void onError(String message) {
                mostrarError(message);
            }
        });
    }
    //3. resive las peliculas y actualiza la pantalla
    private void onPeliculasCargadas(List<Pelicula>data){
        todas.clear(); //se limpia por si hay errores
        todas.addAll(data);//se guarda la lista completa
        mostrarDestacada(); //se llena la tarjeta destacada de la semana
        aplicarFiltros(); //pintamos la cuandricula
    }

    //Muestra un mensaje de error
    private void mostrarError(String mensaje) {
        Toast.makeText(this, "mensaje", Toast.LENGTH_SHORT).show();
    }

    //UI: Filtros y destacada
    /**
     * empieza de la lista completa y
     * se queda solo con las peliculas que cumplan
     * el genero y el texto de busqueda a la vez*/
    private void aplicarFiltros() {
        List<Pelicula> resultados = new ArrayList<>(); // lista de resultados
        for (Pelicula pelicula : todas) {
            //Conincide el genero?
            //equalsIgnoreCase compara sin importar si es Mayuscula o minuscula
            boolean okGenero = generoSeleccionado.isEmpty() || pelicula.getGenero().equalsIgnoreCase(generoSeleccionado);
            //coincide el texto?
            boolean okTexto = query.isEmpty() || pelicula.getTitulo().toLowerCase().contains(query);
            if (okGenero && okTexto) resultados.add(pelicula);
        }
        adapter.submit(resultados, favoritasId); // se muestra el resultado
        // si no hay resultados se muestra el mensaje
        mensaje.setVisibility(resultados.isEmpty() ? View.VISIBLE : View.GONE);
    }

    //Llenar la tarjeta desttacada de la semana
    private void mostrarDestacada() {
        if (todas.isEmpty()) return;; // sin peliculas no hay nada que destacar
        // la pelicula con mayor calificacion se pone de primeras
        destacada = todas.get(0);
        // si se encuentra una mejor claificada, se remplaza
        for (Pelicula pelicula : todas) {
            if (pelicula.getClasificacion() > destacada.getClasificacion()) destacada =pelicula;
        }
        // Carga del poster con Glide
        Glide.with
    }
    private void initObjects(){
        buscar = findViewById(R.id.svSearch);
        chipGroup = findViewById(R.id.chipGroup);
        imgDestacada = findViewById(R.id.imgDestacada);
        tituloDestacado = findViewById(R.id.tvTitleFamous);
        infoDestacado = findViewById(R.id.tvInfoFamous);
        rbDestacada = findViewById(R.id.rbFamous);
        verMas = findViewById(R.id.btnVerMas);
        rvPeliculas = findViewById(R.id.rvPeliculas);
        mensaje = findViewById(R.id.tvError);
        bottomNavigationView = findViewById(R.id.btnNav);
    }
}