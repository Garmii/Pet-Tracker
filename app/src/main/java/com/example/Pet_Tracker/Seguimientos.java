package com.example.Pet_Tracker;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import com.example.login.R;

import java.sql.SQLException;
import java.util.ArrayList;

import Adaptador.AdaptadorSeguimiento;
import BD.DBSalud;
import BD.SALUDSqlHelper;
import modelo.Animal;
import modelo.Seguimiento;

public class Seguimientos extends AppCompatActivity {

    ArrayList<Seguimiento> listaSeguimientos;
    RecyclerView recycler;

    private SALUDSqlHelper saludSqlHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seguimiento);

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

        recycler = findViewById(R.id.recyclerSeguimiento);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(staggeredGridLayoutManager);

        Animal animal = (Animal) getIntent().getSerializableExtra("animal");

        cargarArrayListSeguimiento(animal);

        AdaptadorSeguimiento adaptadorSeguimiento = new AdaptadorSeguimiento(listaSeguimientos);

        recycler.setAdapter(adaptadorSeguimiento);
    }

    private void cargarArrayListSeguimiento(Animal animal) {
        String[] args = new String[1];
        args[0] = String.valueOf(animal.getId());
        listaSeguimientos = extraerSeguimientos(args);
    }

    private ArrayList<Seguimiento> extraerSeguimientos(String[] args) { //Añade las mascotas del usuario registrado al array del recycler
        ArrayList<Seguimiento> seguimientos = new ArrayList<Seguimiento>();

        Cursor cursor = db.query(DBSalud.SEGUIMIENTO_TABLE_SEGUIMIENTO,
                null, DBSalud.SEGUIMIENTO_COL_ID_ANIMAL + "=?",args,null,null,null);

        while(cursor.moveToNext()){
            Seguimiento seguimiento = new Seguimiento();
            seguimiento.setId(cursor.getInt(0));
            seguimiento.setIdAnimal(cursor.getInt(1));
            seguimiento.setTipo(cursor.getString(2));
            seguimiento.setPeso(cursor.getInt(3));
            seguimiento.setDescripcion(cursor.getString(4));
            seguimiento.setFecha(cursor.getString(5));
            seguimiento.setSexo(cursor.getString(6));

            seguimientos.add(seguimiento);
        }
        cursor.close();
        return seguimientos;
    }
}