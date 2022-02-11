package com.example.Pet_Tracker;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;

import com.andanhm.quantitypicker.QuantityPicker;
import com.example.login.R;
import com.google.android.material.navigation.NavigationBarView;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;

import modelo.Animal;
public class anadirMascota extends AppCompatActivity {

    ImageView imagen;
    EditText nombre;
    EditText raza;
    QuantityPicker anyo;
    QuantityPicker mes;
    Spinner spinner;

    Button botonImagen;
    Button botonAnadir;

    private ArrayList<String> especies;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir_mascota);

        anyo = findViewById(R.id.anyoMascota);
        mes = findViewById(R.id.mesMascota);

        botonImagen = findViewById(R.id.botonCambiarImagenMascota);

        imagen = findViewById(R.id.imagenCrearMascota);
        spinner = findViewById(R.id.spinnerEspecie);
        botonAnadir = findViewById(R.id.botonAnadirMascota);

        Animal animal = new Animal();


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                cambiarImagen(spinner.getSelectedItem().toString()); // Cambio la imagen según el valor seleccionado
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });


        botonAnadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animal.setEspecie(spinner.getSelectedItem().toString());
                Log.i("Spinner", animal.getEspecie());
                Log.i("Number Picker",anyo.getQuantity()+"");
            }
        });

        botonImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cargarImagen();
            }
        });

    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if(result.getResultCode() == RESULT_OK){
                        Intent data = result.getData();
                        Uri imageUri = data.getData();

                        imagen.setImageURI(imageUri);
                    }else{
                        FancyToast.makeText(getApplicationContext(),"Error al cargar la imagen",FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();
                    }
                }
            }
    );

    private void cargarImagen() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        activityResultLauncher.launch(intent);
    }


    private void cambiarImagen(String seleccionado) {

        switch (seleccionado){
            case "Perro":
                imagen.setImageResource(R.drawable.default_dog);
                break;
            case "Gato":
                imagen.setImageResource(R.drawable.default_cat);
                break;
            case "Pajaro":
                imagen.setImageResource(R.drawable.default_bird);
                break;
            case "Pinguino":
                imagen.setImageResource(R.drawable.default_penguin);
                break;
            case "Rana":
                imagen.setImageResource(R.drawable.default_frog);
                break;
            case "Conejo":
                imagen.setImageResource(R.drawable.default_rabbit);
                break;
            case "Cerdo":
                imagen.setImageResource(R.drawable.default_pig);
                break;
            default:
                imagen.setImageResource(R.drawable.add_pet_icon);
                break;
        }
    }
}