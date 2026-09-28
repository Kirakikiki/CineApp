package co.edu.cineapp.data.entities;

public class PromedioResena {
    private Long peliculaId;
    private double promedio;
    private int totalResenas;

    public Long getPeliculaId() { return peliculaId; }
    public void setPeliculaId(Long peliculaId) { this.peliculaId = peliculaId; }

    public double getPromedio() { return promedio; }
    public void setPromedio(double promedio) { this.promedio = promedio; }

    public int getTotalResenas() { return totalResenas; }
    public void setTotalResenas(int totalResenas) { this.totalResenas = totalResenas; }
}