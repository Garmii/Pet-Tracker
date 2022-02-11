package com.example.Pet_Tracker;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.login.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shashank.sony.fancytoastlib.FancyToast;

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

    private FloatingActionButton anadirAnimal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mascotas);

        anadirAnimal = findViewById(R.id.botonAnadir);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                    }
                }
        );

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

        adaptadorAnimales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(),DetalleMascota.class);
                intent.putExtra("animal",listaAnimales.get(recycler.getChildAdapterPosition(view)));

                activityResultLauncher.launch(intent);


                //Log.i("Animal seleccionado",""+listaAnimales.get(recycler.getChildAdapterPosition(view)));

            }
        });

        recycler.setAdapter(adaptadorAnimales);

        anadirAnimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),anadirMascota.class);
                activityResultLauncher.launch(intent);
            }
        });




    }

    private void cargarArrayListAnimales(Usuario usuario) {
        String[] args = new String[1];
        args[0] = String.valueOf(usuario.getId());
        listaAnimales = extraerMascotas(args);
    }

    private ArrayList<Animal> extraerMascotas(String[] args) { //Añade las mascotas del usuario registrado al array del recycler
        ArrayList<Animal> animales = new ArrayList<Animal>();

        Cursor cursor = db.query(DBSalud.ANIMAL_TABLE_ANIMAL,
                null, DBSalud.ANIMAL_COL_ID_USUARIO + "=?",args,null,null,null);

       while(cursor.moveToNext()){
           Animal animal = new Animal();
           animal.setId(cursor.getInt(0));
           animal.setIdUsuario(cursor.getInt(1));
           animal.setNombre(cursor.getString(2));
           animal.setAnyo(cursor.getInt(3));
           animal.setMes(cursor.getInt(4));
           animal.setEspecie(cursor.getString(5));
           animal.setRaza(cursor.getString(6));
           animal.setSexo(cursor.getString(7));
           animal.setImagen(cursor.getInt(8));
           animales.add(animal);
           Log.i("ANIMALES",animal.toString());
       }
       cursor.close();
        Log.i("ANIMALES",animales.toString());
        return animales;
    }

}