package co.edu.cineapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FavoritosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_favoritos);

        BottomNavigationView bottomNavigation =
                findViewById(R.id.bottomNavigation);

        bottomNavigation.setSelectedItemId(R.id.nav_favoritas);

        bottomNavigation.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.nav_catalogos) {

                // Ir al catálogo
                return true;

            } else if (id == R.id.nav_favoritas) {

                // Ya estamos en favoritos
                return true;

            } else if (id == R.id.nav_mi_cine) {

                // Ir a Mi cine
                return true;
            }

            return false;
        });
    }
}