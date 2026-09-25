package co.edu.cineapp.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import co.edu.cineapp.entities.User;
import co.edu.cineapp.manager.ManagerDataBase;
import co.edu.cineapp.manager.UserContract;

public class UserRepository {

    private static final String TAG = "UserRepository";
    private static final int STATUS_ACTIVE = 1;
    private final ManagerDataBase managerDataBase;

    public UserRepository(Context context){
        managerDataBase = new ManagerDataBase(context.getApplicationContext());
    }
    //Metodo crud para insertar usuarios en la bd
    public long insertUser(User user){
        ContentValues values = new ContentValues();
        values.put(UserContract.COLUMN_NAMES,user.getName());
        values.put(UserContract.COLUMN_EMAIL, user.getEmail());
        values.put(UserContract.COLUMN_PASSWORD, user.getPassword());
        values.put(UserContract.COLUMN_STATUS,STATUS_ACTIVE);
        try {
            SQLiteDatabase database = managerDataBase.getWritableDatabase();//abre la BD en modo escritura
            return database.insert(UserContract.TABLE_NAME, null, values);//devuelve el id insertado, o -1 si falla
        } catch (Exception e) {
            Log.e(TAG, "ERROR AL INTENAR REGISTRAR EL USUARIO", e);
        }
        return -1;
    }

    //Metodo para traer un uruario por su correo
    public User obtenerPorCorreo(String email){
        SQLiteDatabase database = managerDataBase.getReadableDatabase(); //abre la BD en modo solo lectura
        Cursor cursor = database.query(
                UserContract.TABLE_NAME, //Tabla
                null, //= todas la columnas
                UserContract.COLUMN_EMAIL + "=?", //condicion WHERE
                new String[]{email}, //valor que remplaza el ?
                null, null, null //groupBy, having, orderBy (no se usan)
        );

        User user = null;
        if (cursor.moveToFirst()){ //si encontro alguna fila
            user = new User();
            user.setName(cursor.getString(cursor.getColumnIndexOrThrow(UserContract.COLUMN_NAMES)));
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(UserContract.COLUMN_EMAIL)));
            user.setStatus((byte) cursor.getInt(cursor.getColumnIndexOrThrow(UserContract.COLUMN_STATUS)));
        }
        cursor.close(); //siempre cerrar el cursor
        return user; //retorna el usuario, sale null si no existe
    }

    //metodo update: edita el nombre del usuario existente
    public int updateUser(String emailActual, String nuevoNombre) {
        ContentValues values = new ContentValues();
        values.put(UserContract.COLUMN_NAMES, nuevoNombre);

        SQLiteDatabase database = managerDataBase.getWritableDatabase();
        return database.update(
                UserContract.TABLE_NAME,
                values,
                UserContract.COLUMN_EMAIL + "=?",
                new String[]{emailActual}
        ); //devuelve cuantas filas se modificaron
    }

    //metodo delete
    public int deleteUser(String email){
        SQLiteDatabase database = managerDataBase.getWritableDatabase();
        return database.delete(
                UserContract.TABLE_NAME,
                UserContract.COLUMN_EMAIL + "=?",
                new String[]{email}
        ); //devuelve cuántas filas se borraron
    }

}
