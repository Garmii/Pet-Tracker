package com.example.Pet_Tracker;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.login.R;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.sql.SQLException;

import BD.DBSalud;
import BD.SALUDSqlHelper;

public class SignIn extends AppCompatActivity {

    private Button iniciarSesion;
    private Button registrarse;

    private SALUDSqlHelper saludSqlHelper;
    private SQLiteDatabase db;

    private EditText nombre;
    private EditText correo;
    private EditText contra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        iniciarSesion = findViewById(R.id.iniciarSesion);
        registrarse = findViewById(R.id.registrarse);

        nombre = findViewById(R.id.etNombre);
        correo = findViewById(R.id.etCorreo);
        contra = findViewById(R.id.etContra);

        saludSqlHelper = SALUDSqlHelper.getInstance(this);

        try {
            db = saludSqlHelper.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        iniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] argsComprobar = new String[1];
                argsComprobar[0] = correo.getText().toString();

                String[] argsInsertar = new String[3];

                argsInsertar[0] = nombre.getText().toString();
                argsInsertar[1] = correo.getText().toString();
                argsInsertar[2] = contra.getText().toString();

               if(comprobarCuenta(argsComprobar)){ // Existe un cuenta

                   FancyToast.makeText(getApplicationContext(),"Ya hay una cuenta asociada a este correo",FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();

               }else{ // No existe la cuenta

                   if(crearCuenta(argsInsertar)){
                       FancyToast.makeText(getApplicationContext(),"Cuenta registrada!",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,false).show();
                   }else{
                       FancyToast.makeText(getApplicationContext(),"Error al crear la cuenta",FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();
                   }
               }
            }
        });

    }

    private boolean comprobarCuenta(String[] args) {
        boolean existe = false;
        Cursor cursor = db.query(DBSalud.USUARIO_TABLE_USUARIO,
                null, DBSalud.USUARIO_COL_CORREO + "=? ",args,null,null,null);

        if(cursor.moveToFirst()){
            existe = true;
        }

        return existe;
    }

    private boolean crearCuenta(String[] args){
        boolean bien=false;
        ContentValues contentValues = new ContentValues();
        contentValues.put("NOMBRE",args[0]);
        contentValues.put("CORREO",args[1]);
        contentValues.put("CONTRA",args[2]);

        long filas = db.insert(DBSalud.USUARIO_TABLE_USUARIO,null,contentValues);

        if(filas != -1){
            bien = true;
        }

        return bien;

    }

/*    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }*/
}