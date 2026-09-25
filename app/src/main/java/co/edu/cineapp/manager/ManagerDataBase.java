package co.edu.cineapp.manager;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ManagerDataBase  extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "cineapp.db";

        private static final int DATABASE_VERSION = 1;

        // Constructor: se ejecuta cuando haces new ManagerDataBase(context)

        public ManagerDataBase(@Nullable Context context) {

            // Le pasa a la clase padre: el contexto, el nombre de la BD,

            // null (fábrica de cursores, casi nunca se usa) y la versión

            super(context, DATABASE_NAME, null, DATABASE_VERSION);

        }

        // Se ejecuta UNA sola vez: cuando la base de datos se crea por primera vez

        @Override

        public void onCreate(SQLiteDatabase database) {

            // Ejecuta la sentencia SQL que crea la tabla.

            // CREATE_TABLE viene de la clase UserContract (ahí está el "CREATE TABLE ...")

            database.execSQL(UserContract.CREATE_TABLE);

        }


        // Sirve para modificar tablas sin perder datos

        @Override

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Está vacío: por ahora no hace nada al actualizar
        }

}


