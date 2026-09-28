package co.edu.cineapp;

import android.os.Bundle;
import android.util.Log;
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

import co.edu.cineapp.data.entities.User;
import co.edu.cineapp.data.entities.User;
import co.edu.cineapp.data.remote.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private EditText etNombre;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private Button btnRegistrarse;
    private TextView tvVolverLogin;
    private String firebaseUid;

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
        etEmail = findViewById(R.id.etEmail);
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

        String email =
                etEmail.getText().toString().trim();

        String password =
                etPassword.getText().toString();

        String confirmPassword =
                etConfirmPassword.getText().toString();

        if (nombre.isEmpty()) {

            etNombre.setError("Ingrese su nombre");
            etNombre.requestFocus();

            return;
        }

        if (email.isEmpty()) {

            etEmail.setError("Ingrese su correo electrónico");
            etEmail.requestFocus();

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
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {

                    if (task.isSuccessful()) {

                        FirebaseUser user = mAuth.getCurrentUser();

                        // Guardar el nombre en el perfil de Firebase
                        if (user != null) {

                            // Guardamos el uid ahora, porque más adelante
                            // se cierra la sesión de Firebase.
                            String firebaseUid = user.getUid();
                            byte status = 1;

                            UserProfileChangeRequest profileUpdates =
                                    new UserProfileChangeRequest.Builder()
                                            .setDisplayName(nombre)
                                            .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(profileTask -> {

                                        // Firebase ya creó la cuenta. Ahora
                                        // también guardamos al usuario en la API.
                                        registrarEnApi(nombre, email, password, status, firebaseUid);
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

    // Envía el usuario a cineapp-api (POST /api/usuarios) para que quede
    // guardado en PostgreSQL, vinculado con Firebase por medio del uid.
    private void registrarEnApi(String nombre, String email, String password, byte status, String firebaseUid) {

        User nuevo = new User(nombre, email, password, status, firebaseUid);

        RetrofitClient.getApiService().crearUsuario(nuevo)
                .enqueue(new Callback<User>() {

                    @Override
                    public void onResponse(Call<User> call,
                                           Response<User> response) {

                        if (response.isSuccessful() && response.body() != null) {

                            Toast.makeText(
                                    RegisterActivity.this,
                                    "Cuenta creada correctamente",
                                    Toast.LENGTH_SHORT
                            ).show();

                        } else {

                            Log.e(TAG, "La API respondió con código "
                                    + response.code());

                            Toast.makeText(
                                    RegisterActivity.this,
                                    "Cuenta creada, pero el servidor rechazó el perfil ("
                                            + response.code() + ")",
                                    Toast.LENGTH_LONG
                            ).show();
                        }

                        terminarRegistro();
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                        // Sin conexión con la API (servidor apagado, URL incorrecta...)
                        Log.e(TAG, "No se pudo contactar la API", t);

                        Toast.makeText(
                                RegisterActivity.this,
                                "Cuenta creada, pero no se pudo conectar con el servidor",
                                Toast.LENGTH_LONG
                        ).show();

                        terminarRegistro();
                    }
                });
    }

    // Cerramos sesión porque queremos que el usuario entre desde el Login.
    private void terminarRegistro() {
        mAuth.signOut();
        finish();
    }
}