package co.edu.cineapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText etCorreo;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvRegistrarse;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_main);

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

        // Inicializar Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        iniciarComponentes();
        configurarEventos();
    }

    private void iniciarComponentes() {

        etCorreo = findViewById(R.id.etCorreo);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegistrarse = findViewById(R.id.tvRegistrarse);
    }

    private void configurarEventos() {

        btnLogin.setOnClickListener(v -> validarLogin());

        tvRegistrarse.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    RegisterActivity.class
            );

            startActivity(intent);
        });
    }

    private void validarLogin() {

        String correo =
                etCorreo.getText().toString().trim();

        String password =
                etPassword.getText().toString();

        if (correo.isEmpty()) {

            etCorreo.setError("Ingrese su correo electrónico");
            etCorreo.requestFocus();

            return;
        }

        if (password.isEmpty()) {

            etPassword.setError("Ingrese su contraseña");
            etPassword.requestFocus();

            return;
        }

        // Desactivar temporalmente el botón
        // para evitar varios intentos al mismo tiempo.
        btnLogin.setEnabled(false);

        mAuth.signInWithEmailAndPassword(correo, password)
                .addOnCompleteListener(this, task -> {

                    btnLogin.setEnabled(true);

                    if (task.isSuccessful()) {

                        Toast.makeText(
                                MainActivity.this,
                                "Inicio de sesión exitoso",
                                Toast.LENGTH_SHORT
                        ).show();

                    } else {

                        String mensaje = "Correo o contraseña incorrectos";

                        if (task.getException() != null) {
                            mensaje = task.getException().getMessage();
                        }

                        Toast.makeText(
                                MainActivity.this,
                                mensaje,
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }
}
