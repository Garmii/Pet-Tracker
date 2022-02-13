package com.example.Pet_Tracker;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import com.example.login.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import com.shashank.sony.fancygifdialoglib.FancyGifDialogListener;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import Adaptador.AdaptadorAnimales;
import BD.DBSalud;
import BD.SALUDSqlHelper;
import SharedPreferences.SharedPreferences;
import modelo.Animal;
import modelo.Usuario;

public class Mascotas extends AppCompatActivity {

    ArrayList<Animal> listaAnimales;
    RecyclerView recycler;

    private SALUDSqlHelper saludSqlHelper;
    private SQLiteDatabase db;

    private FloatingActionButton anadirAnimal;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedPreferences = new SharedPreferences(this);

        if (sharedPreferences.loadNightModeState() == true) {
            setTheme(R.style.temaOscuro);
        } else {
            setTheme(R.style.temaClaro);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mascotas);

        anadirAnimal = findViewById(R.id.botonAnadir);

        db=null;
        saludSqlHelper = SALUDSqlHelper.getInstance(this);

        try {
            db = saludSqlHelper.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        recycler = findViewById(R.id.recyclerMascotas);
        recycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        //Recojo el usuario registrado
         Usuario usuario = getUsuario();

        //Cargo el adaptador con las mascotas del usuario
        AdaptadorAnimales adaptadorAnimales = getAdaptadorAnimales(usuario);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == 101){ // Vuelvo de crear Animal
                          Intent intent = result.getData();
                          Animal animalRecibido = (Animal) intent.getSerializableExtra("animal");
                          listaAnimales.add(animalRecibido);
                          recycler.getAdapter().notifyItemInserted(listaAnimales.size());
                          Log.i("ANIMAL RECIBIDO AL CREAR",animalRecibido.toString());

                          FancyToast.makeText(getApplicationContext(),animalRecibido.getNombre() + " añadido correctamente",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,false).show();

                        }else if(result.getResultCode() == 103){ // Vuelvo de editar Animal
                            Intent intent = result.getData();
                            Animal a = (Animal) intent.getSerializableExtra("animal");
                            String pos = intent.getStringExtra("pos");
                            listaAnimales.set(Integer.parseInt(pos),a);
                            recycler.getAdapter().notifyItemChanged(Integer.parseInt(pos));

                            Log.i("ANIMAL RECIBIDO AL CREAR",a.toString());
                            Log.i("ANIMAL RECIBIDO AL CREAR",pos);

                            FancyToast.makeText(getApplicationContext(),a.getNombre() + " modificado correctamente",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,false).show();
                        }else{ // Vuelvo de arrastrar a los lados!!!

                            recycler.getAdapter().notifyDataSetChanged();

                        }
                        Log.i("CODIGO RECIBIDO MASCOTAS",result.getResultCode()+"");
                    }
                }
        );

        adaptadorAnimales.setOnClickListener(new View.OnClickListener() { //Al pulsar sobre un elemento del recycler lanza una actividad con los datos del animal
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Mascotas.this,DetalleMascota.class);
                Log.i("ANINMAL ENVIADO A VER DETALLES",listaAnimales.get(recycler.getChildAdapterPosition(view)).toString());
                intent.putExtra("animal",listaAnimales.get(recycler.getChildAdapterPosition(view)));
                activityResultLauncher.launch(intent);

            }
        });


        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) { //Gestos recycler view
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();
                Collections.swap(listaAnimales,fromPosition,toPosition);
                recyclerView.getAdapter().notifyItemMoved(fromPosition,toPosition);

                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                switch (direction){
                    case ItemTouchHelper.LEFT:
                        Intent intent = new Intent(Mascotas.this,EditarMascota.class);
                        intent.putExtra("animal",listaAnimales.get(viewHolder.getAdapterPosition()));
                        intent.putExtra("pos",String.valueOf(viewHolder.getAdapterPosition()));
                        Log.i("ANINMAL ENVIADO EDITAR",listaAnimales.get(viewHolder.getAdapterPosition()).toString());
                        Log.i("POSICION ANINMAL ENVIADO EDITAR",String.valueOf(viewHolder.getAdapterPosition()));
                        activityResultLauncher.launch(intent);
                            break;
                    case ItemTouchHelper.RIGHT:

                        Animal animalSeleccionado = listaAnimales.get(viewHolder.getAdapterPosition());
                        int posicion = viewHolder.getAdapterPosition();
                        Log.i("ANINMAL ENVIADO BORRAR",animalSeleccionado.toString());
                        Log.i("POSICION ANINMAL ENVIADO BORRAR",posicion+"");

                        new FancyGifDialog.Builder(Mascotas.this) // Dialogo para borrar registros
                                .setTitle("Estás seguro que deseas eliminar a "+listaAnimales.get(viewHolder.getAdapterPosition()).getNombre())
                                .setMessage("Esta acción no se podra deshacer, piensatelo bien.")
                                .setTitleTextColor(R.color.black)
                                .setDescriptionTextColor(R.color.black)
                                .setNegativeBtnText("Cancelar")
                                .setPositiveBtnBackground(R.color.black)
                                .setPositiveBtnText("Eliminar")
                                .setNegativeBtnBackground(R.color.black)
                                .setGifResource(R.drawable.dog_crying)
                                .isCancellable(true)
                                .OnPositiveClicked(new FancyGifDialogListener() {//Al pulsar eliminar
                                    @Override
                                    public void OnClick() {
                                        //TODO eliminar en la bd y ArrayList
                                        if(eliminarMascota(animalSeleccionado) > 0){
                                            listaAnimales.remove(posicion);
                                            recycler.getAdapter().notifyItemRemoved(posicion);
                                        FancyToast.makeText(Mascotas.this,"Adios " + animalSeleccionado.getNombre(),FancyToast.LENGTH_LONG,FancyToast.SUCCESS,false).show();
                                        }else{
                                        FancyToast.makeText(Mascotas.this,"Error al eliminar " + animalSeleccionado.getNombre(),FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();
                                        }

                                    }
                                })
                                .OnNegativeClicked(new FancyGifDialogListener() {//Al pulsar cancelar
                                    @Override
                                    public void OnClick() {
                                        FancyToast.makeText(Mascotas.this,"No se ha eliminado a " + animalSeleccionado.getNombre(),FancyToast.LENGTH_LONG,FancyToast.WARNING,false).show();
                                    }
                                })
                                .build();

                        recycler.getAdapter().notifyDataSetChanged();

                        break;
                }


            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(recycler);

        anadirAnimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AnadirMascota.class);
                intent.putExtra("usuario",usuario);
                activityResultLauncher.launch(intent);
            }
        });
    }

    private int eliminarMascota(Animal animal){
        String args[] = new String[1];
        args[0] = String.valueOf(animal.getId());
        return db.delete(DBSalud.ANIMAL_TABLE_ANIMAL,DBSalud.ANIMAL_COL_ID_ANIMAL + " =?",args);
    }

    @NonNull
    private AdaptadorAnimales getAdaptadorAnimales(Usuario usuario) { //Carga las mascotas del usuario registrado en el recycler
        AdaptadorAnimales adaptadorAnimales = new AdaptadorAnimales(cargarArrayListAnimales(usuario));
        recycler.setAdapter(adaptadorAnimales);
        return adaptadorAnimales;
    }

    private ArrayList<Animal> cargarArrayListAnimales(Usuario usuario) {
        String[] args = new String[1];
        args[0] = String.valueOf(usuario.getId());
        return listaAnimales = extraerMascotas(args);
    }

    private ArrayList<Animal> extraerMascotas(String[] args) { //Añade las mascotas del usuario registrado al array del recycler
        ArrayList<Animal> animales = new ArrayList<Animal>();

        Cursor cursor = db.query(DBSalud.ANIMAL_TABLE_ANIMAL,
                null, DBSalud.ANIMAL_COL_ID_USUARIO + "=?",args,null,null,null);

       while(cursor.moveToNext()){
           Animal animal = new Animal();
           animal.setId(cursor.getInt(0));
           animal.setIdUsuario(cursor.getInt(1));
           animal.setNombre(cursor.getString(2));
           animal.setAnyo(cursor.getInt(3));
           animal.setMes(cursor.getInt(4));
           animal.setEspecie(cursor.getString(5));
           animal.setRaza(cursor.getString(6));
           animal.setSexo(cursor.getString(7));
           animal.setImagen(cursor.getString(8));
           animales.add(animal);
       }
        Log.i("ANIMALES EN LA BD",animales.toString());
       cursor.close();

        return animales;
    }

    private Usuario getUsuario() { // Recojo el usuario registrado
        Usuario usuario = new Usuario();
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        return usuario;
    }

}