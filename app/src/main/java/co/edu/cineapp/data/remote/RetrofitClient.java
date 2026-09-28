package co.edu.cineapp.data.remote; // Indica en qué carpeta está esta clase.

import java.util.concurrent.TimeUnit; // Sirve para poner tiempos de espera en la conexión.

import co.edu.cineapp.utils.Constants; // Trae la dirección de nuestra API.

import okhttp3.OkHttpClient; // Sirve para crear y configurar la conexión.

import okhttp3.logging.HttpLoggingInterceptor; // Sirve para ver en consola las llamadas que hace la app.

import retrofit2.Retrofit; // Biblioteca que usamos para conectarnos con la API.

import retrofit2.converter.gson.GsonConverterFactory; // Sirve para convertir los datos JSON en objetos de Java.

public class RetrofitClient { // Esta clase se encarga de crear la conexión con nuestra API

    private static Retrofit retrofit; // Aquí guardamos la conexión para poder reutilizarla.

    private RetrofitClient() { // Constructor privado para evitar crear objetos de esta clase.
    }

    public static co.edu.cineapp.data.remote.ApiService getApiService() { // Este metodo nos entrega el servicio de la API.

        // Crea el ApiService usando nuestra conexión.
        return getRetrofit().create(
                co.edu.cineapp.data.remote.ApiService.class
        );
    }

    private static Retrofit getRetrofit() { // Este metodo crea la conexión con Retrofit.

        if (retrofit == null) { // Si todavía no existe la conexión, la creamos.

            HttpLoggingInterceptor loggingInterceptor = // Crea una herramienta para ver las llamadas a la API.
                    new HttpLoggingInterceptor();

            loggingInterceptor.setLevel( // Muestra en consola la información completa de las llamadas.
                    HttpLoggingInterceptor.Level.BODY
            );

            OkHttpClient client = new OkHttpClient.Builder() // Configuramos el cliente que hará las conexiones.

                    .addInterceptor(loggingInterceptor) // Agregamos el registro de las llamadas.

                    .connectTimeout(15, TimeUnit.SECONDS) // Espera máximo 15 segundos para conectarse.

                    .readTimeout(15, TimeUnit.SECONDS) // Espera máximo 15 segundos para recibir datos.

                    .build(); // Termina de crear el cliente.

            retrofit = new Retrofit.Builder() // Aquí configuramos Retrofit.

                    // Le damos la dirección de nuestra API.
                    .baseUrl(Constants.BASE_URL)

                    // Le decimos qué cliente debe usar.
                    .client(client)

                    // Permite convertir JSON a objetos Java.
                    .addConverterFactory(
                            GsonConverterFactory.create()
                    )

                    // Termina de crear Retrofit.
                    .build();
        }

        // Devuelve la conexión que ya tenemos creada.
        return retrofit;
    }
}