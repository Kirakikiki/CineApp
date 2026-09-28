// Indica en qué paquete está esta interfaz.
package co.edu.cineapp.data.remote;


// Esta interfaz sirve para recibir la respuesta de la API.
public interface ApiCallback<T> {

    // Se ejecuta cuando la petición salió bien.
    void onSuccess(T resultado);

    // Se ejecuta cuando ocurrió un error.
    void onError(String mensaje);
}