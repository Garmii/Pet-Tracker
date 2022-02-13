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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

                if(actualizarMascota(args) > 0){
                    Intent intent = new Intent();
                    setResult(103,intent);
                    intent.putExtra("animal",animal);
                    intent.putExtra("pos",pos);
                    Log.i("a",animal.toString() + pos);
                    finish();
                }else{
                   FancyToast.makeText(getApplicationContext(),"Error al modificar",FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();
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

    private synchronized int actualizarMascota(String args[]){
        ContentValues contentValues = new ContentValues();
        contentValues.put("NOMBRE",animal.getNombre());
        contentValues.put("ANYO",animal.getAnyo());
        contentValues.put("MES",animal.getMes());
        contentValues.put("ESPECIE",animal.getEspecie());
        contentValues.put("RAZA",animal.getRaza());
        contentValues.put("SEXO",animal.getSexo());
        contentValues.put("IMAGEN",animal.getImagen());
        int filas;
        return filas = db.update(DBSalud.ANIMAL_TABLE_ANIMAL,contentValues,DBSalud.ANIMAL_COL_ID_USUARIO + " =? AND "+ DBSalud.ANIMAL_COL_ID_ANIMAL + " =?",args);

    }

    @NonNull
    private Animal setAnimal() {
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
        Bitmap bm = BitmapFactory.decodeFile(Uri.decode(ruta)); // Pasa la imagen a bitmap
        imagen.setImageBitmap(bm);
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

                        Log.i("RUTA",ruta);
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

    private Animal getAnimal() {
        Animal animal = new Animal();
        animal = (Animal) getIntent().getSerializableExtra("animal");
        return animal;
    }
}