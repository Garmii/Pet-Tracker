package com.example.Pet_Tracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.sql.SQLException;

public class SALUDSqlHelper extends SQLiteOpenHelper {


    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_FILE_NAME = "DB_SALUD.db";

    private SQLiteDatabase db;
    private static SALUDSqlHelper SALUDSqlHelper;

    String sqlCreateUSUARIO = "CREATE TABLE " + DBSalud.USUARIO_TABLE_USUARIO + "("
            + DBSalud.USUARIO_COL_ID_USUARIO + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + DBSalud.USUARIO_COL_NOMBRE + " TEXT,"
            + DBSalud.USUARIO_COL_CORREO + " TEXT,"
            + DBSalud.USUARIO_COL_CONTRA + " TEXT);";

    String sqlCreateANIMAL = "CREATE TABLE " + DBSalud.ANIMAL_TABLE_ANIMAL
            + "(" + DBSalud.ANIMAL_COL_ID_ANIMAL + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + DBSalud.ANIMAL_COL_ID_USUARIO + " INTEGER,"
            + DBSalud.ANIMAL_COL_NOMBRE + " TEXT,"
            + DBSalud.ANIMAL_COL_ANYO + " INTEGER,"
            + DBSalud.ANIMAL_COL_MES + " INTEGER,"
            + DBSalud.ANIMAL_COL_RAZA + " TEXT,"
            + DBSalud.ANIMAL_COL_SEXO + " TEXT,"
            + " FOREIGN KEY(" + DBSalud.ANIMAL_COL_ID_USUARIO
            + ") REFERENCES " + DBSalud.USUARIO_TABLE_USUARIO
            + "(" + DBSalud.USUARIO_COL_ID_USUARIO + "));";

    String sqlCreateSEGUIMIENTO = "CREATE TABLE " + DBSalud.SEGUIMIENTO_TABLE_SEGUIMIENTO
            + "(" + DBSalud.SEGUIMIENTO_COL_ID_SEGUIMIENTO + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + DBSalud.SEGUIMIENTO_COL_ID_ANIMAL + " INTEGER,"
            + DBSalud.SEGUIMIENTO_COL_TIPO + " TEXT,"
            + DBSalud.SEGUIMIENTO_COL_PESO + " INTEGER,"
            + DBSalud.SEGUIMIENTO_COL_DESCRIPCION + " TEXT,"
            + DBSalud.SEGUIMIENTO_COL_FECHA + " TEXT,"
            + DBSalud.ANIMAL_COL_SEXO + " TEXT,"
            + " FOREIGN KEY(" + DBSalud.SEGUIMIENTO_COL_ID_ANIMAL
            + ") REFERENCES " + DBSalud.ANIMAL_TABLE_ANIMAL
            + "(" + DBSalud.ANIMAL_COL_ID_ANIMAL + "));";

    private SALUDSqlHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(sqlCreateUSUARIO);
        sqLiteDatabase.execSQL(sqlCreateANIMAL);
        sqLiteDatabase.execSQL(sqlCreateSEGUIMIENTO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DBSalud.USUARIO_TABLE_USUARIO);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DBSalud.ANIMAL_TABLE_ANIMAL);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DBSalud.SEGUIMIENTO_TABLE_SEGUIMIENTO);
        sqLiteDatabase.execSQL(sqlCreateUSUARIO);
        sqLiteDatabase.execSQL(sqlCreateANIMAL);
        sqLiteDatabase.execSQL(sqlCreateSEGUIMIENTO);
    }

    public static synchronized SALUDSqlHelper getInstance(Context context) {
        if (SALUDSqlHelper == null) {
            SALUDSqlHelper = new SALUDSqlHelper(context, DATABASE_FILE_NAME, null, DATABASE_VERSION);
        }
        return SALUDSqlHelper;
    }

    public synchronized SQLiteDatabase open() throws SQLException {
        return SALUDSqlHelper.getWritableDatabase();
    }

    public synchronized void close() {
        db.close();
    }

}