package com.example.Pet_Tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;

import com.example.login.R;

import java.util.ArrayList;

import modelo.Animal;

public class anadirMascota extends AppCompatActivity {

    ImageView imagen;
    EditText nombre;
    EditText raza;
    NumberPicker anyo;
    NumberPicker mes;
    Spinner spinner;

    Button botonAnadir;

    private ArrayList<String> especies;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir_mascota);

        anyo = findViewById(R.id.anyoMascota);
        mes = findViewById(R.id.mesMascota);

        anyo.setMinValue(0);
        anyo.setMaxValue(20);

        mes.setMinValue(0);
        mes.setMaxValue(11);

        //imagen = findViewById(R.id.imagenCrearMascota);
        spinner = findViewById(R.id.spinnerEspecie);
        botonAnadir = findViewById(R.id.botonAnadirMascota);

        Animal animal = new Animal();


        botonAnadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        animal.setEspecie(spinner.getSelectedItem().toString());
                Log.i("Spinner", animal.getEspecie());
            }
        });

    }
}