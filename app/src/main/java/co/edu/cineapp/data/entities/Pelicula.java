package co.edu.cineapp.data.entities;

public class Pelicula {
    private Long id;
    private String titulo;
    private String sinopsis;
    private Integer duracionMinutos;
    private String clasificacion;
    private String posterUrl;
    private String trailerUrl;
    private String fechaEstreno;
    private Genero genero;

    // Constructor vacío necesario para Gson/Retrofit
    public Pelicula() {
    }

    public Pelicula(
            Long id,
            String titulo,
            String sinopsis,
            Integer duracionMinutos,
            String clasificacion,
            String posterUrl,
            String trailerUrl,
            String fechaEstreno,
            Genero genero
    ) {
        this.id = id;
        this.titulo = titulo;
        this.sinopsis = sinopsis;
        this.duracionMinutos = duracionMinutos;
        this.clasificacion = clasificacion;
        this.posterUrl = posterUrl;
        this.trailerUrl = trailerUrl;
        this.fechaEstreno = fechaEstreno;
        this.genero = genero;
    }

    // =========================
    // GETTERS
    // =========================

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public Integer getDuracionMinutos() {
        return duracionMinutos;
    }

    public String getClasificacion() {
        return clasificacion;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public String getTrailerUrl() {
        return trailerUrl;
    }

    public String getFechaEstreno() {
        return fechaEstreno;
    }

    public Genero getGenero() {
        return genero;
    }

    // SETTERS
    public void setId(Long id) {
        this.id = id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public void setDuracionMinutos(Integer duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public void setClasificacion(String clasificacion) {
        this.clasificacion = clasificacion;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public void setTrailerUrl(String trailerUrl) {
        this.trailerUrl = trailerUrl;
    }

    public void setFechaEstreno(String fechaEstreno) {
        this.fechaEstreno = fechaEstreno;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    // =========================
    // CLASE GENERO
    // =========================

    public static class Genero {

        private Long id;
        private String nombre;

        public Genero() {
        }

        public Genero(Long id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }

        public Long getId() {
            return id;
        }

        public String getNombre() {
            return nombre;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        @Override
        public String toString() {
            return nombre;
        }
    }

    // =========================
    // MÉTODOS DE APOYO
    // =========================

    /**
     * Devuelve el nombre del género.
     * Sirve para mostrarlo directamente en la interfaz.
     */
    public String getNombreGenero() {
        if (genero != null && genero.getNombre() != null) {
            return genero.getNombre();
        }

        return "";
    }

    /**
     * Devuelve la duración como texto.
     * Ejemplo: "120 min"
     */
    public String getDuracionTexto() {
        if (duracionMinutos != null) {
            return duracionMinutos + " min";
        }

        return "";
    }

    // Devuelve el año de estreno.  Ejemplo: fechaEstreno = "2014-11-07"  resultado = "2014"
    public String getAnio() {
        if (fechaEstreno != null && fechaEstreno.length() >= 4) {
            return fechaEstreno.substring(0, 4);
        }

        return "";
    }
}
