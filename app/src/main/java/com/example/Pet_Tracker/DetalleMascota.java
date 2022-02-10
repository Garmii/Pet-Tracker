package com.example.Pet_Tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.login.R;

import org.w3c.dom.Text;

import modelo.Animal;

public class DetalleMascota extends AppCompatActivity {

    TextView nombre;
    TextView anyo;
    TextView mes;
    TextView especie;
    TextView raza;
    TextView sexo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_mascota);

        nombre = findViewById(R.id.nombre);
        anyo = findViewById(R.id.anyo);
        mes = findViewById(R.id.mes);
        especie = findViewById(R.id.especie);
        raza = findViewById(R.id.raza);
        sexo = findViewById(R.id.sexo);

        Animal animal = new Animal();
        animal = (Animal) getIntent().getSerializableExtra("animal");

        nombre.setText(animal.getNombre());
        anyo.setText(String.valueOf(animal.getAnyo()));
        mes.setText(String.valueOf(animal.getMes()));
        especie.setText(animal.getEspecie());
        raza.setText(animal.getRaza());
        sexo.setText(animal.getSexo());

    }
}