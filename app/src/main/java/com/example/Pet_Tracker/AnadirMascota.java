package com.example.Pet_Tracker;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
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
import android.widget.RadioButton;
import android.widget.Spinner;

import com.example.login.R;
import com.google.android.material.navigation.NavigationBarView;
import com.mcdev.quantitizerlibrary.HorizontalQuantitizer;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;

import BD.DBSalud;
import BD.SALUDSqlHelper;
import modelo.Animal;
import modelo.Usuario;

public class AnadirMascota extends AppCompatActivity {

    ImageView imagen;
    EditText nombre;
    EditText raza;
    HorizontalQuantitizer anyo;
    HorizontalQuantitizer mes;
    Spinner especie;
    RadioButton rbMacho;
    RadioButton rbHembra;

    Button botonImagen;
    Button botonAnadir;

    private SALUDSqlHelper saludSqlHelper;
    private SQLiteDatabase db;

    private ArrayList<String> especies;

    private String ruta="";
    private String nombreFotoDefecto="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir_mascota);

        nombre = findViewById(R.id.etNombreMascota);
        raza = findViewById(R.id.etRazaMascota);
        imagen = findViewById(R.id.imagenCrearMascota);
        especie = findViewById(R.id.spinnerEspecie);
        anyo = findViewById(R.id.anyoMascota);
        mes = findViewById(R.id.mesMascota);
        botonImagen = findViewById(R.id.botonCambiarImagenMascota);
        botonAnadir = findViewById(R.id.botonAnadirMascota);
        rbHembra = findViewById(R.id.rbHembra);
        rbMacho = findViewById(R.id.rbMacho);

        anyo.setMaxValue(30);
        anyo.setMinValue(0);

        mes.setMaxValue(11);
        mes.setMinValue(0);


        //Recojo el usuario registrado
        Usuario usuario = getUsuario();

        db=null;
        saludSqlHelper = SALUDSqlHelper.getInstance(this);

        try {
            db = saludSqlHelper.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Animal animal = new Animal();


        especie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//Cambio la imagen al cambiar el elemnto del spinner
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                cambiarImagen(especie.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });

        View.OnClickListener rb = new View.OnClickListener() { // Valores de los radio button
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.rbMacho:
                        animal.setSexo("Macho");
                        break;
                    case R.id.rbHembra:
                        animal.setSexo("Hembra");
                        break;
                }
            }
        };
        rbMacho.setOnClickListener(rb);
        rbHembra.setOnClickListener(rb);


        botonAnadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animal.setIdUsuario(usuario.getId());
                animal.setNombre(nombre.getText().toString());
                animal.setEspecie(especie.getSelectedItem().toString());
                animal.setAnyo(anyo.getValue());
                animal.setMes(mes.getValue());
                animal.setRaza(raza.getText().toString());
                if(!ruta.isEmpty()){
                animal.setImagen(ruta);
                }else{
                animal.setImagen(nombreFotoDefecto);
                }
                anadirMascota(animal);
                animal.setId(getIdMascotaCreada(animal));
                Intent intent = new Intent();
                setResult(101,intent);
                intent.putExtra("animal",animal);
                finish();
            }
        });

        botonImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cargarImagen();
            }
        });

    }

    private int getIdMascotaCreada(Animal animal) {
        String args[] = new String[2];
        args[0] = String.valueOf(animal.getIdUsuario());
        args[1] = animal.getNombre();

        String cols[] = new String[1];
        cols[0] = DBSalud.ANIMAL_COL_ID_ANIMAL;

        Cursor c = db.query(DBSalud.ANIMAL_TABLE_ANIMAL,cols,DBSalud.ANIMAL_COL_ID_USUARIO + " =? AND " + DBSalud.ANIMAL_COL_NOMBRE + " =?",args,null,null,null);

        while(c.moveToNext()){
            animal.setId(c.getInt(0));
        }
        c.close();
        return animal.getId();
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if(result.getResultCode() == RESULT_OK){
                        Intent data = result.getData();
                        Uri imageUri = data.getData();
                        ruta = getRealPathFromURI(imageUri);
                        imagen.setImageURI(imageUri);
                    }else{
                        FancyToast.makeText(getApplicationContext(),"Error al cargar la imagen",FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();
                    }
                }
            }
    );

    private String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private void cargarImagen() { //Abro el almacenamiento interno del movil
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        activityResultLauncher.launch(intent);
    }

    private void anadirMascota(Animal animal) { //Inserto el nuevo animal en la BD
        ContentValues cv = new ContentValues();
        cv.put("ID_USUARIO",animal.getIdUsuario());
        cv.put("NOMBRE",animal.getNombre());
        cv.put("ANYO",animal.getAnyo());
        cv.put("MES",animal.getMes());
        cv.put("ESPECIE",animal.getEspecie());
        cv.put("RAZA",animal.getRaza());
        cv.put("SEXO",animal.getSexo());
        cv.put("IMAGEN",animal.getImagen());
        db.insert(DBSalud.ANIMAL_TABLE_ANIMAL,null,cv);
    }


    private void cambiarImagen(String seleccionado) {
        switch (seleccionado){
            case "Perro":
                imagen.setImageResource(R.drawable.default_dog);
                nombreFotoDefecto = "default_dog";
                break;
            case "Gato":
                imagen.setImageResource(R.drawable.default_cat);
                nombreFotoDefecto = "default_cat";
                break;
            case "Pajaro":
                imagen.setImageResource(R.drawable.default_bird);
                nombreFotoDefecto = "default_bird";
                break;
            case "Pinguino":
                imagen.setImageResource(R.drawable.default_penguin);
                nombreFotoDefecto = "default_penguin";
                break;
            case "Rana":
                imagen.setImageResource(R.drawable.default_frog);
                nombreFotoDefecto = "default_frog";
                break;
            case "Conejo":
                imagen.setImageResource(R.drawable.default_rabbit);
                nombreFotoDefecto = "default_rabbit";
                break;
            case "Cerdo":
                imagen.setImageResource(R.drawable.default_pig);
                nombreFotoDefecto = "default_pig";
                break;
            default:
                imagen.setImageResource(R.drawable.add_pet_icon);
                nombreFotoDefecto = "add_pet_icon";
                break;
        }
    }

    private Usuario getUsuario() { // Recojo el usuario registrado
        Usuario usuario = new Usuario();
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        return usuario;
    }
}