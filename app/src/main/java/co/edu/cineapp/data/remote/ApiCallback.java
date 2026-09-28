package co.edu.cineapp.data.remote;

// Interfaz simple para recibir el resultado de una llamada a la API desde
// cualquier Fragment/Activity, sin exponer las clases propias de Retrofit.
public interface ApiCallback<T> {
    void onSuccess(T resultado);
    void onError(String mensaje);
}