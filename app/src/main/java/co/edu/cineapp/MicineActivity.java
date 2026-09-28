package co.edu.cineapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import co.edu.cineapp.utils.SessionManager;

public class MicineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_mi_cine);

        SessionManager sessionManager = new SessionManager(this);
        String correo = sessionManager.getCorreo();
        TextView tvCorreo = findViewById(R.id.tvCorreoUsuario);
        TextView tvNombre = findViewById(R.id.tvNombreUsuario);
        TextView tvIniciales = findViewById(R.id.tvIniciales);
        TextView tvCerrarSesion = findViewById(R.id.tvCerrarSesion);
        if (correo != null && !correo.isEmpty()) {
            tvCorreo.setText(correo);
            tvNombre.setText(correo.substring(0, correo.indexOf('@')));
            tvIniciales.setText(correo.substring(0, 1).toUpperCase());
        }
        tvCerrarSesion.setOnClickListener(v -> {
            sessionManager.cerrarSesion();
            com.google.firebase.auth.FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, MainActivity.class));
            finishAffinity();
        });

        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigationMiCine);
        bottomNavigation.setSelectedItemId(R.id.nav_mi_cine);
        bottomNavigation.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_catalogos) {
                startActivity(new Intent(this, CatalogoActivity.class));
                return true;
            }
            if (item.getItemId() == R.id.nav_favoritas) {
                startActivity(new Intent(this, FavoritosActivity.class));
                return true;
            }
            return true;
        });

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (v, insets) -> {

                    Insets systemBars = insets.getInsets(
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
}