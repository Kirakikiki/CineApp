package co.edu.cineapp.edu.ui.auth;

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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import co.edu.cineapp.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText etNombre;
    private EditText etCorreo;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private Button btnRegistrarse;
    private TextView tvVolverLogin;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_register);

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

        etNombre = findViewById(R.id.etNombre);
        etCorreo = findViewById(R.id.etCorreo);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegistrarse = findViewById(R.id.btnRegistrarse);
        tvVolverLogin = findViewById(R.id.tvVolverLogin);
    }

    private void configurarEventos() {

        btnRegistrarse.setOnClickListener(v -> registrarUsuario());

        tvVolverLogin.setOnClickListener(v -> {
            finish();
        });
    }

    private void registrarUsuario() {

        String nombre =
                etNombre.getText().toString().trim();

        String correo =
                etCorreo.getText().toString().trim();

        String password =
                etPassword.getText().toString(); //Almacena la contraseña dentro de firebase

        String confirmPassword =
                etConfirmPassword.getText().toString();

        if (nombre.isEmpty()) {

            etNombre.setError("Ingrese su nombre");
            etNombre.requestFocus();

            return;
        }

        if (correo.isEmpty()) {

            etCorreo.setError("Ingrese su correo electrónico");
            etCorreo.requestFocus();

            return;
        }

        if (password.isEmpty()) {

            etPassword.setError("Ingrese una contraseña");
            etPassword.requestFocus();

            return;
        }

        if (password.length() < 6) {

            etPassword.setError(
                    "La contraseña debe tener al menos 6 caracteres"
            );
            etPassword.requestFocus();

            return;
        }

        if (confirmPassword.isEmpty()) {

            etConfirmPassword.setError(
                    "Confirme su contraseña"
            );
            etConfirmPassword.requestFocus();

            return;
        }

        if (!password.equals(confirmPassword)) {

            etConfirmPassword.setError(
                    "Las contraseñas no coinciden"
            );
            etConfirmPassword.requestFocus();

            return;
        }

        // Desactivar botón mientras Firebase procesa el registro
        btnRegistrarse.setEnabled(false);

        // Crear usuario en Firebase Authentication
        // Firebase no guarda datos en una tabla solo genera la autenticacion
        mAuth.createUserWithEmailAndPassword(correo, password)
                .addOnCompleteListener(this, task -> {

                    if (task.isSuccessful()) {

                        FirebaseUser user = mAuth.getCurrentUser();

                        // Guardar el nombre en el perfil de Firebase
                        if (user != null) {

                            UserProfileChangeRequest profileUpdates =
                                    new UserProfileChangeRequest.Builder()
                                            .setDisplayName(nombre)
                                            .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(profileTask -> {

                                        Toast.makeText(
                                                RegisterActivity.this,
                                                "Cuenta creada correctamente",
                                                Toast.LENGTH_SHORT
                                        ).show();

                                        // Cerramos sesión porque queremos
                                        // que el usuario entre desde el Login.
                                        mAuth.signOut();

                                        finish();
                                    });

                        } else {

                            btnRegistrarse.setEnabled(true);

                            Toast.makeText(
                                    RegisterActivity.this,
                                    "Cuenta creada correctamente",
                                    Toast.LENGTH_SHORT
                            ).show();

                            finish();
                        }

                    } else {

                        btnRegistrarse.setEnabled(true);

                        String mensaje =
                                "No se pudo crear la cuenta";

                        if (task.getException() != null) {
                            mensaje =
                                    task.getException().getMessage();
                        }

                        Toast.makeText(
                                RegisterActivity.this,
                                mensaje,
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }
}
