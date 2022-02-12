package com.example.Pet_Tracker;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.login.R;

import modelo.Animal;

public class DetalleMascota extends AppCompatActivity {

    TextView nombre;
    TextView edad;
    TextView especie;
    TextView raza;
    TextView sexo;
    ImageView imagen;

    Button verSeguimiento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_mascota);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                    }
                }
        );

        nombre = findViewById(R.id.nombre);
        edad = findViewById(R.id.edad);
        especie = findViewById(R.id.especie);
        raza = findViewById(R.id.raza);
        sexo = findViewById(R.id.sexo);
        imagen = findViewById(R.id.imagenDetalleMascota);

        verSeguimiento = findViewById(R.id.verSeguimiento);

        Animal animal = getAnimal();

        cargarAnimal(animal);

        verSeguimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Seguimientos.class);
                intent.putExtra("animal",animal);

                activityResultLauncher.launch(intent);
            }
        });
    }

    private void cargarAnimal(Animal animal) { // Cargo los datos del animal en la vista
        nombre.setText(animal.getNombre());
        especie.setText(animal.getEspecie());
        raza.setText(animal.getRaza());
        sexo.setText(animal.getSexo());

        //Cojo la imagen como ruta y la paso a bitmap
        String ruta =  (animal.getImagen());
        Bitmap bm = BitmapFactory.decodeFile(Uri.decode(ruta));
        imagen.setImageBitmap(bm);

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