package com.example.Pet_Tracker;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import com.example.login.R;

import java.sql.SQLException;
import java.util.ArrayList;

import Adaptador.AdaptadorAnimales;
import BD.DBSalud;
import BD.SALUDSqlHelper;
import modelo.Animal;
import modelo.Usuario;

public class Mascotas extends AppCompatActivity {

    ArrayList<Animal> listaAnimales;
    RecyclerView recycler;

    private SALUDSqlHelper saludSqlHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mascotas);

        db=null;
        saludSqlHelper = SALUDSqlHelper.getInstance(this);

        try {
            db = saludSqlHelper.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        recycler = findViewById(R.id.recyclerMascotas);
        recycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        //Recojo el usuario registrado
        Usuario usuario = (Usuario) getIntent().getSerializableExtra("usuario");


        cargarArrayListAnimales(usuario);

        AdaptadorAnimales adaptadorAnimales = new AdaptadorAnimales(listaAnimales);
        recycler.setAdapter(adaptadorAnimales);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                    }
                }
        );

    }

    private void cargarArrayListAnimales(Usuario usuario) {
        String[] args = new String[1];
        args[0] = String.valueOf(usuario.getId());
        listaAnimales = extraerMascotas(args);
    }

    private ArrayList<Animal> extraerMascotas(String[] args) { //Comprueba que los datos estén bien
        ArrayList<Animal> animales = new ArrayList<Animal>();
        Animal animal = new Animal();
        Cursor cursor = db.query(DBSalud.ANIMAL_TABLE_ANIMAL,
                null, DBSalud.ANIMAL_COL_ID_USUARIO + "=?",args,null,null,null);

       while(cursor.moveToNext()){
           animal.setId(cursor.getInt(0));
           animal.setIdUsuario(cursor.getInt(1));
           animal.setNombre(cursor.getString(2));
           animal.setAnyo(cursor.getInt(3));
           animal.setMes(cursor.getInt(4));
           animal.setRaza(cursor.getString(5));
           animal.setSexo(cursor.getString(6));
           Log.i("ANIMALES", animal.toString());
           animales.add(animal);
       }
       cursor.close();

        return animales;
    }

}