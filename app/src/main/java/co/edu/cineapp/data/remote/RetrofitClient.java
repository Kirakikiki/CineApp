package co.edu.cineapp.edu.data.remote;

import java.util.concurrent.TimeUnit;

import co.edu.cineapp.edu.utils.Constants;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Un unico Retrofit para toda la app (patron singleton), asi no se crea una
// conexion nueva cada vez que se necesita llamar a la API.
public class RetrofitClient {

    private static Retrofit retrofit;

    private RetrofitClient() {
    }

    public static ApiService getApiService() {
        return getRetrofit().create(ApiService.class);
    }

    private static Retrofit getRetrofit() {
        if (retrofit == null) {

            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}