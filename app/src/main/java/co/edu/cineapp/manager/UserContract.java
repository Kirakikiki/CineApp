package co.edu.cineapp.manager;

public class UserContract {
    public static final String TABLE_NAME = "users";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAMES = "nombres";
    public static final String COLUMN_EMAIL = "correo";
    public static final String COLUMN_PASSWORD = "contraseña";
    public static final String COLUMN_STATUS = "status";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAMES + " TEXT NOT NULL, " +
                    COLUMN_EMAIL + " TEXT NOT NULL UNIQUE, " +
                    COLUMN_PASSWORD + " TEXT NOT NULL, " +
                    COLUMN_STATUS + " INTEGER NOT NULL DEFAULT 1 " +
                    " CHECK (" + COLUMN_STATUS + " IN (0, 1)))";

    public static final String DROP_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;
}
