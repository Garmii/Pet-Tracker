package com.example.Pet_Tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.login.R;

import org.w3c.dom.Text;

import modelo.Animal;

public class DetalleMascota extends AppCompatActivity {

    TextView nombre;
    TextView edad;
    TextView especie;
    TextView raza;
    TextView sexo;
    ImageView imagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_mascota);

        nombre = findViewById(R.id.nombre);
        edad = findViewById(R.id.edad);
        especie = findViewById(R.id.especie);
        raza = findViewById(R.id.raza);
        sexo = findViewById(R.id.sexo);
        imagen = findViewById(R.id.imagenDetalleMascota);

        Animal animal = getAnimal();

        cargarAnimal(animal);

    }

    private void cargarAnimal(Animal animal) { // Cargo los datos del animal en la vista
        nombre.setText(animal.getNombre());
        especie.setText(animal.getEspecie());
        raza.setText(animal.getRaza());
        sexo.setText(animal.getSexo());
        imagen.setImageResource(animal.getImagen());

        if (animal.getMes() > 1) {
            if (animal.getAnyo() > 1) {
                edad.setText(animal.getAnyo() + " años y " + animal.getMes() + " meses");
            } else {
                edad.setText(animal.getAnyo() + " año y " + animal.getMes() + " meses");
            }
        }

        else if (animal.getMes() == 1) {
            if (animal.getAnyo() > 1) {
                edad.setText(animal.getAnyo() + " años y " + animal.getMes() + " mes");
            } else {
                edad.setText(animal.getAnyo() + " año y " + animal.getMes() + " mes");
            }
        }

        else if(animal.getMes() == 0){
            if (animal.getAnyo() > 1) {
                edad.setText(animal.getAnyo() + " años y " + animal.getMes() + " meses");
            } else {
                edad.setText(animal.getAnyo() + " año y " + animal.getMes() + " meses");
            }
        }
    }

    private Animal getAnimal() { // Recojo el animal seleccionado en el Recycler a traves del intent
        Animal animal = new Animal();
        animal = (Animal) getIntent().getSerializableExtra("animal");
        return animal;
    }
}