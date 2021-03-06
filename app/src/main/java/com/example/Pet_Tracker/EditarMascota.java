package com.example.Pet_Tracker;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.example.login.R;
import com.mcdev.quantitizerlibrary.HorizontalQuantitizer;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.sql.SQLException;

import BD.DBSalud;
import BD.SALUDSqlHelper;
import SharedPreferences.SharedPreferences;
import modelo.Animal;
import modelo.Usuario;

public class EditarMascota extends AppCompatActivity {

    private SALUDSqlHelper saludSqlHelper;
    private SQLiteDatabase db;

    ImageView imagen;
    EditText nombre;
    EditText raza;
    HorizontalQuantitizer anyo;
    HorizontalQuantitizer mes;
    Spinner especie;
    RadioButton rbMacho;
    RadioButton rbHembra;

    Button botonImagen;
    Button botonEditar;

    private String ruta;
    private String sexo;
    private Animal animal;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedPreferences = new SharedPreferences(this);

        if (sharedPreferences.loadNightModeState()) {
            setTheme(R.style.temaOscuro);
        } else {
            setTheme(R.style.temaClaro);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_mascota);

        imagen = findViewById(R.id.imagenEditarMascota);
        nombre = findViewById(R.id.etEditarNombreMascota);
        raza = findViewById(R.id.etEditarRazaMascota);
        anyo = findViewById(R.id.anyoEditarMascota);
        mes = findViewById(R.id.mesEditarMascota);
        especie = findViewById(R.id.spinnerEditarEspecie);
        rbMacho = findViewById(R.id.rbEditarMacho);
        rbHembra = findViewById(R.id.rbEditarHembra);

        botonImagen = findViewById(R.id.botonEditarImagenMascota);
        botonEditar = findViewById(R.id.botonEditarMascota);

        //Recojo el animal seleccionado
        animal = getAnimal();
        cargarAnimal(animal);
        String pos = getIntent().getStringExtra("pos");

        anyo.setMaxValue(30);
        anyo.setMinValue(0);

        mes.setMaxValue(11);
        mes.setMinValue(0);

        db=null;
        saludSqlHelper = SALUDSqlHelper.getInstance(this);

        try {
            db = saludSqlHelper.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        View.OnClickListener rb = new View.OnClickListener() { // Valores de los radio button
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.rbEditarMacho:
                        sexo = "Macho";
                        break;
                    case R.id.rbEditarHembra:
                        sexo = "Hembra";
                        break;
                }
            }
        };
        rbMacho.setOnClickListener(rb);
        rbHembra.setOnClickListener(rb);

        botonEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animal = setAnimal();

                String args[] = new String[2];
                args[0] = String.valueOf(animal.getIdUsuario());
                args[1] = String.valueOf(animal.getId());

                if (nombre.getText().toString().trim().isEmpty() ||
                        raza.getText().toString().trim().isEmpty()) {
                    FancyToast.makeText(getApplicationContext(), getString(R.string.rellena_campos), FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                } else {

                    if (actualizarMascota(args) > 0) {
                        Intent intent = new Intent();
                        setResult(103, intent);
                        intent.putExtra("animal", animal);
                        intent.putExtra("pos", pos);
                        finish();
                    } else {
                        FancyToast.makeText(getApplicationContext(), getString(R.string.error_modificar), FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    }
                }
            }
        });

        botonImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarImagen();
            }
        });


    }

    private int actualizarMascota(String args[]){
        ContentValues contentValues = new ContentValues();
        contentValues.put("NOMBRE",animal.getNombre());
        contentValues.put("ANYO",animal.getAnyo());
        contentValues.put("MES",animal.getMes());
        contentValues.put("ESPECIE",animal.getEspecie());
        contentValues.put("RAZA",animal.getRaza());
        contentValues.put("SEXO",animal.getSexo());
        contentValues.put("IMAGEN",animal.getImagen());
        return db.update(DBSalud.ANIMAL_TABLE_ANIMAL,contentValues,DBSalud.ANIMAL_COL_ID_USUARIO + " =? AND "+ DBSalud.ANIMAL_COL_ID_ANIMAL + " =?",args);

    }

    @NonNull
    private Animal setAnimal() {
        animal.setId(animal.getId());
        animal.setNombre(nombre.getText().toString());
        animal.setAnyo(anyo.getValue());
        animal.setMes(mes.getValue());
        animal.setEspecie(especie.getSelectedItem().toString());
        animal.setRaza(raza.getText().toString());
        animal.setSexo(sexo);
        animal.setImagen(ruta);
        return animal;
    }

    private void cargarAnimal(Animal animal){
        nombre.setText(animal.getNombre());
        anyo.setValue(animal.getAnyo());
        mes.setValue(animal.getMes());
        raza.setText(animal.getRaza());
        ruta = animal.getImagen();
        if (ruta.contains("default_")) {
            int drawableId = getResources().getIdentifier(ruta, "drawable", getApplicationContext().getPackageName());
            imagen.setImageResource(drawableId);
        } else {
            Bitmap bm = BitmapFactory.decodeFile(Uri.decode(ruta)); // Pasa la imagen a bitmap
            imagen.setImageBitmap(bm);
        }
        //TODO libreria para los numberPicker

        switch (animal.getEspecie()){
            case "Perro":
                especie.setSelection(1);
                break;
            case "Gato":
                especie.setSelection(2);
                break;
            case "Pajaro":
                especie.setSelection(3);
                break;
            case "Pinguino":
                especie.setSelection(4);
                break;
            case "Rana":
                especie.setSelection(5);
                break;
            case "Conejo":
                especie.setSelection(6);
                break;
            case "Cerdo":
                especie.setSelection(7);
                break;
        }

        if(animal.getSexo().equals("Macho")){
            rbMacho.setChecked(true);
            sexo = "Macho";
        }else{
            rbHembra.setChecked(true);
            sexo = "Hembra";
        }
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
                        FancyToast.makeText(getApplicationContext(),getString(R.string.error_cargar_imagen),FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();
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

    private Animal getAnimal() {
        Animal animal = new Animal();
        animal = (Animal) getIntent().getSerializableExtra("animal");
        return animal;
    }
}