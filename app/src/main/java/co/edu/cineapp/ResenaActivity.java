package co.edu.cineapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

public class ResenaActivity extends AppCompatActivity {

    private ImageButton btnAtras;
    private TextView tvTituloP;
    private TextView tvInfoP;
    private TextView tvTitleReseña;
    private RatingBar rbResena;
    private EditText etResena;
    private MaterialButton btnTomarfoto, btnUbicacion;
    private Button btnPublicarResena;
    private Bitmap fotoCapturada = null;
    private String ubicacionTexto = "";

    // Launchers para cámara y permisos
    private final ActivityResultLauncher<Void> camaraLauncher =
            registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), bitmap -> {
                if (bitmap != null) {
                    fotoCapturada = bitmap;
                    btnTomarfoto.setText("Foto tomada ✓");
                    Toast.makeText(this, "Foto adjuntada", Toast.LENGTH_SHORT).show();
                }
            });
    // Launcher para pedir permiso de cámara
    private final ActivityResultLauncher<String> permisoCamaraLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    camaraLauncher.launch(null);
                } else {
                    Toast.makeText(this, "Se requiere permiso de cámara para tomar fotos", Toast.LENGTH_SHORT).show();
                }
            });
        //hola
        // Launcher para pedir permiso de ubicación
        private final ActivityResultLauncher<String> permisoUbicacionLauncher =
        registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                obtenerUbicacion();
            } else {
                Toast.makeText(this, "Se requiere permiso de ubicación", Toast.LENGTH_SHORT).show();
            }
        });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_resena);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();
        cargarDatosPelicula();
        // LLAMADA A LOS BOTONES DENTRO DE ONCREATE
        btnAtras.setOnClickListener(this::volverAtras);
        btnTomarfoto.setOnClickListener(this::tomarFoto);
        btnUbicacion.setOnClickListener(this::solicitarUbicacion);
        btnPublicarResena.setOnClickListener(this::publicarResena);
    }

    /*1.Metodo del botón Atrás*/
    private void volverAtras(View view) {
        finish();
    }

    // 2. Metodo del botón Tomar Foto
    private void tomarFoto(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            camaraLauncher.launch(null);
        } else {
            permisoCamaraLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    // 3. Metodo del botón Ubicación
    private void solicitarUbicacion(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            obtenerUbicacion();
        } else {
            permisoUbicacionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }
    // 4. Metodo del botón Publicar Reseña
    private void publicarResena(View view) {
        String texto = etResena.getText().toString().trim();
        float calificacion = rbResena.getRating();
        if (calificacion == 0) {
            Toast.makeText(this, "Por favor selecciona una calificación con estrellas", Toast.LENGTH_SHORT).show();
            return;
        }
        if (texto.isEmpty()) {
            etResena.setError("Escribe tu opinión sobre la película");
            etResena.requestFocus();
            return;
        }
        // Aquí conectarás con tu API de Retrofit cuando me pases ApiService
        Toast.makeText(this, "¡Reseña publicada con éxito!", Toast.LENGTH_LONG).show();
        finish();
    }

    private void cargarDatosPelicula() {
        // Obtenemos los datos que nos envía la pantalla anterior
        String titulo = getIntent().getStringExtra("titulo");
        String anio = getIntent().getStringExtra("anio");
        String genero = getIntent().getStringExtra("genero");
        if (titulo != null) {
            tvTituloP.setText(titulo);
            tvTitleReseña.setText("Tu opinión sobre " + titulo);
        } else {
            tvTituloP.setText("Película");
            tvTitleReseña.setText("Tu reseña");
        }
        String info = (anio != null ? anio : "") + (genero != null ? " • " + genero : "");
        tvInfoP.setText(info.isEmpty() ? "Información no disponible" : info);
    }

    private void obtenerUbicacion() {
        try {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (locationManager != null && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location == null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
                if (location != null) {
                    ubicacionTexto = "Lat: " + String.format("%.2f", location.getLatitude()) + ", Lon: " + String.format("%.2f", location.getLongitude());
                    btnUbicacion.setText("Ubicación ✓");
                    Toast.makeText(this, "Ubicación obtenida: " + ubicacionTexto, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "No se pudo obtener la ubicación actual. Activa el GPS.", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error obteniendo ubicación: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void publicarResena() {
        String texto = etResena.getText().toString().trim();
        float calificacion = rbResena.getRating();
        if (calificacion == 0) {
            Toast.makeText(this, "Por favor selecciona una calificación con estrellas", Toast.LENGTH_SHORT).show();
            return;
        }
        if (texto.isEmpty()) {
            etResena.setError("Escribe tu opinión sobre la película");
            etResena.requestFocus();
            return;
        }
        // Aquí se procesa el guardado (API o base de datos)
        Toast.makeText(this, "¡Reseña publicada con éxito!", Toast.LENGTH_LONG).show();
        finish(); // Cierra la pantalla y regresa a la película
    }

    private void initViews() {
        btnAtras = findViewById(R.id.btnAtras);
        tvTituloP = findViewById(R.id.tvTituloP);
        tvInfoP = findViewById(R.id.tvInfoP);
        tvTitleReseña = findViewById(R.id.tvTitleReseña);
        rbResena = findViewById(R.id.rbResena);
        etResena = findViewById(R.id.etResena);
        btnTomarfoto = findViewById(R.id.btnTomarfoto);
        btnUbicacion = findViewById(R.id.btnUbicacion);
        btnPublicarResena = findViewById(R.id.btnPublicarResena);
    }
}