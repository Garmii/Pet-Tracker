package com.example.Pet_Tracker;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.login.R;

import java.util.ArrayList;

import Adaptador.AdaptadorAnimales;
import modelo.Animal;

public class Mascotas extends AppCompatActivity {

    ArrayList<Animal> listaAnimales;
    RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mascotas);

        recycler = findViewById(R.id.recyclerMascotas);
        recycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        listaAnimales = new ArrayList<Animal>();

        for(int i = 0;i< 50;i++){
            Animal animal = new Animal(i,i,"Animal "+i,i,i,"Raza "+i,"Sexo "+i);
            listaAnimales.add(animal);
        }

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
}