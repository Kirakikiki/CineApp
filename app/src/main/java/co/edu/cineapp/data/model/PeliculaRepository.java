package co.edu.cineapp.data.model;

import java.util.List;
import co.edu.cineapp.data.entities.Pelicula;
import co.edu.cineapp.data.remote.ApiCallback;
import co.edu.cineapp.data.remote.ApiService;
import co.edu.cineapp.data.remote.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PeliculaRepository {
    private final ApiService api = RetrofitClient.getApiService();

    public void getPeliculas(ApiCallback<List<Pelicula>> callback) {
        api.listarPeliculas().enqueue(new Callback<List<Pelicula>>() {
            @Override
            public void onResponse(Call<List<Pelicula>> call, Response<List<Pelicula>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Error del servidor: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<Pelicula>> call, Throwable t) {
                callback.onError("Sin conexión: " + t.getMessage());
            }
        });
    }
}