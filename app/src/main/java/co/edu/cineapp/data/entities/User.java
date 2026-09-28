package co.edu.cineapp.data.entities;

// Usuario tal como lo maneja la API (PostgreSQL).
// No lleva contraseña: la autenticación la hace Firebase.
public class User {
    private Long id;
    private String nombre;
    private String email;
    private String password;
    private byte status;
    private String firebaseUid;

    public User() {
    }

    public User(String nombre, String email, String password, byte status, String firebaseUid) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.status = status;
        this.firebaseUid = firebaseUid;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword(){return password; }
    public void setPassword(String password){this.password = password; }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public String getFirebaseUid() { return firebaseUid; }
    public void setFirebaseUid(String firebaseUid) { this.firebaseUid = firebaseUid; }
}