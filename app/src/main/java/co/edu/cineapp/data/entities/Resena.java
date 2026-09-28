package co.edu.cineapp.data.entities;

public class Resena {
    private Long id;
    private Pelicula pelicula;
    private User user;
    private Integer calificacion;
    private String comentario;
    private String fecha;
// + getters y setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Pelicula getPelicula() { return pelicula; }
    public void setPelicula(Pelicula pelicula) { this.pelicula = pelicula; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }

    public int getCalificacion() { return calificacion; }
    public void setCalificacion(int calificacion) { this.calificacion = calificacion; }
}