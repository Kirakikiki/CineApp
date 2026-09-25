package co.edu.cineapp.datastore;

import android.content.Context;

import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava3.RxDataStore;

import androidx.datastore.preferences.core.Preferences;

import co.edu.cineapp.utils.Constants;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class AjustesRepository {
    private final RxDataStore<androidx.datastore.preferences.core.Preferences> dataStore; // almacen de preferencias

    //llave bajo la que se guarda el valor booleano del modo oscuro
    private static final Preferences.Key<Boolean> MODO_OSCURO = PreferencesKeys.booleanKey(Constants.KEY_MODO_OSCURO);

    public AjustesRepository(Context context){
        dataStore = new RxPreferenceDataStoreBuilder(
                context.getApplicationContext(), Constants.DATASTORE_AJUSTES).build();
    }

    public Flowable<Boolean> observarModoOscuro(){
        return dataStore.data() //flujo que emite cada vez que cambian las preferencias
                .map(preferences -> preferences.get(MODO_OSCURO) != null && preferences.get(MODO_OSCURO)); //si no hay valor, asume false
    }

    public Single<Preferences> setModoOscuro(boolean activo){
        return dataStore.updateDataAsync(preferences -> { //actualiza de forma asincrona
            MutablePreferences m = preferences.toMutablePreferences();
            m.set(MODO_OSCURO, activo);
            return Single.just(m);
        });
    }
}
