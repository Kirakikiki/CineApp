package co.edu.cineapp.data.entities;

public class Resena {
    private Long id;
    private Long peliculaId;
    private Long usuarioId;
    private String comentario;
    private int calificacion;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPeliculaId() { return peliculaId; }
    public void setPeliculaId(Long peliculaId) { this.peliculaId = peliculaId; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }

    public int getCalificacion() { return calificacion; }
    public void setCalificacion(int calificacion) { this.calificacion = calificacion; }
}