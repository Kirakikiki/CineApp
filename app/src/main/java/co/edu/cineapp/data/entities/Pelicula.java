package co.edu.cineapp.data.entities;

public class Pelicula {
    private final String titulo;
    private final String director;
    private final String sinopsis;
    private final String anio;
    private final String genero;
    private final String duracion;

    public Pelicula(String titulo, String director, String sinopsis, String anio, String genero, String duracion) {
        this.titulo = titulo;
        this.director = director;
        this.sinopsis = sinopsis;
        this.anio = anio;
        this.genero = genero;
        this.duracion = duracion;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDirector() {
        return director;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public String getAnio() {
        return anio;
    }

    public String getGenero() {
        return genero;
    }

    public String getDuracion() {
        return duracion;
    }
}
