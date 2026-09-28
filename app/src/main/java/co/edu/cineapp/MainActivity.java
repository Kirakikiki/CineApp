package co.edu.cineapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    // Componentes de la interfaz
    private EditText etCorreo;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvRegistrarse;

    // Firebase Authentication
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Configuración de pantalla completa
        EdgeToEdge.enable(this);

        // Cargar layout
        setContentView(R.layout.activity_main);

        // Configurar los márgenes de la pantalla
        configurarVentana();

        // Inicializar Firebase
        mAuth = FirebaseAuth.getInstance();

        // Inicializar componentes
        iniciarComponentes();

        // Configurar eventos
        configurarEventos();
    }


    //  Configura los márgenes para evitar que los elementos
    // queden debajo de la barra de estado o navegación.

    private void configurarVentana() {

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


    //   Inicializa los componentes del XML.

    private void iniciarComponentes() {

        etCorreo = findViewById(R.id.etCorreo);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegistrarse = findViewById(R.id.tvRegistrarse);
    }

    /**
     * Configura los eventos de los botones y textos.
     */
    private void configurarEventos() {

        // Botón iniciar sesión
        btnLogin.setOnClickListener(v -> validarLogin());

        // Texto registrarse
        tvRegistrarse.setOnClickListener(v -> abrirRegistro());
    }

    /**
     * Abre la pantalla de registro.
     */
    private void abrirRegistro() {

        Intent intent = new Intent(
                MainActivity.this,
                RegisterActivity.class
        );

        startActivity(intent);
    }


    // Valida los datos ingresados y realiza el inicio de sesión
    // mediante Firebase Authentication.
    private void validarLogin() {

        // Obtener correo
        String correo = etCorreo
                .getText()
                .toString()
                .trim();

        // Obtener contraseña
        String password = etPassword
                .getText()
                .toString();

        // Validar correo
        if (correo.isEmpty()) {

            etCorreo.setError(
                    "Ingrese su correo electrónico"
            );

            etCorreo.requestFocus();

            return;
        }

        // Validar contraseña
        if (password.isEmpty()) {

            etPassword.setError(
                    "Ingrese su contraseña"
            );

            etPassword.requestFocus();

            return;
        }

        // Desactivar botón mientras Firebase procesa
        btnLogin.setEnabled(false);

        // Iniciar sesión con Firebase
        mAuth.signInWithEmailAndPassword(correo, password)
                .addOnCompleteListener(this, task -> {

                    // Volver a activar el botón
                    btnLogin.setEnabled(true);

                    if (task.isSuccessful()) {

                        // Login correcto
                        Toast.makeText(
                                MainActivity.this,
                                "Inicio de sesión exitoso",
                                Toast.LENGTH_SHORT
                        ).show();

                        // Abrir catálogo
                        abrirCatalogo();

                    } else {

                        // Login incorrecto
                        String mensaje =
                                "Correo o contraseña incorrectos";

                        if (task.getException() != null) {

                            mensaje = task.getException()
                                    .getMessage();
                        }

                        Toast.makeText(
                                MainActivity.this,
                                mensaje,
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }


    //   Abre el catálogo después de iniciar sesión correctamente.

    private void abrirCatalogo() {

        Intent intent = new Intent(
                MainActivity.this,
                CatalogoActivity.class
        );

        startActivity(intent);

        // Evita regresar al login con el botón atrás
        finish();
    }
}
