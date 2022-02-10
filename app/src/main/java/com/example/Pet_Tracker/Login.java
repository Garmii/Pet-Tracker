package com.example.Pet_Tracker;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.login.R;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.sql.SQLException;

import BD.DBSalud;
import BD.SALUDSqlHelper;
import modelo.Animal;
import modelo.Usuario;

public class Login extends AppCompatActivity {

    private SALUDSqlHelper saludSqlHelper;
    private SQLiteDatabase db;

    EditText correo;
    EditText contra;

    private Button iniciarSesion;
    private Button registrarse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        correo = findViewById(R.id.etCorreo);
        contra = findViewById(R.id.etContra);

        iniciarSesion = findViewById(R.id.iniciarSesion);
        registrarse = findViewById(R.id.registrarse);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                    }
                }
        );

        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,SignIn.class);
               activityResultLauncher.launch(intent);
            }
        });

        iniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] argsDatos = new String[2];

                argsDatos[0] = correo.getText().toString();
                argsDatos[1] = contra.getText().toString();

                String[] argsComprobarCuenta = new String[1];

                argsComprobarCuenta[0] = correo.getText().toString();

                if(comprobarDatos(argsDatos)){ // Están bien los datos
                    Intent intent = new Intent(Login.this, Mascotas.class);
                    Usuario usuario = extraerDatos(argsDatos);
                    intent.putExtra("usuario",usuario);
                    activityResultLauncher.launch(intent);
                }else{

                    if(comprobarCuenta(argsComprobarCuenta)){// Existe el correo en la BD?
                        FancyToast.makeText(getApplicationContext(),"Datos incorrectos",FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();
                    }else{
                        FancyToast.makeText(getApplicationContext(),"No hay una cuenta con ese correo, porfavor registrate",FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();
                    }
                }


            }
        });
    }

    private boolean comprobarDatos(String[] args) { //Comprueba que los datos estén bien
        boolean existe = false;
        Cursor cursor = db.query(DBSalud.USUARIO_TABLE_USUARIO,
                null, DBSalud.USUARIO_COL_CORREO + "=? AND "
                + DBSalud.USUARIO_COL_CONTRA + "=?",args,null,null,null);

        if(cursor.moveToFirst()){
            existe = true;
        }

        return existe;
    }

    private boolean comprobarCuenta(String[] args) { // Comprueba que exsita el correo en la BD
        boolean existe = false;
        Cursor cursor = db.query(DBSalud.USUARIO_TABLE_USUARIO,
                null, DBSalud.USUARIO_COL_CORREO + "=?",args,null,null,null);

        if(cursor.moveToFirst()){
            existe = true;
        }

        return existe;
    }

    private Usuario extraerDatos(String[] args) { //Extrae los datos del usuario registrado
        Usuario usuario = new Usuario();
        Cursor cursor = db.query(DBSalud.USUARIO_TABLE_USUARIO,
                null, DBSalud.USUARIO_COL_CORREO + "=? AND "
                        + DBSalud.USUARIO_COL_CONTRA + "=?",args,null,null,null);

        while(cursor.moveToNext()){
            usuario.setId(cursor.getInt(0));
            usuario.setNombre(cursor.getString(1));
            usuario.setCorreo(cursor.getString(2));
            usuario.setContra(cursor.getString(3));
        }
        cursor.close();

        return usuario;
    }

/*    protected void onDestroy() { // Destruyo la conexion cuando destruyo la actividad
        super.onDestroy();
        db.close();
    }*/

    @Override
    protected void onResume() { // Abro la conexion a la base de datos cuando inicio la actividad
        super.onResume();

            saludSqlHelper = SALUDSqlHelper.getInstance(this);
            db = null;
            try {
                    db = saludSqlHelper.open();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }
}
