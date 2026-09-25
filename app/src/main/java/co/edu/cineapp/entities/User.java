package co.edu.cineapp.entities;

public class User {
    // Declaración de atributos
    private String name;
    private String email;
    private String password;
    private byte status;

    //Metodos contructores
    public User() {
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User(String name, String email, String password, byte status) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.status = status;
    }

    // Metodos de acceso

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("name='").append(name).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", status=").append(status);
        sb.append('}');
        return sb.toString();
    }
}
