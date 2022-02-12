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
import android.view.View;

import com.example.login.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import Adaptador.AdaptadorAnimales;
import BD.DBSalud;
import BD.SALUDSqlHelper;
import modelo.Animal;
import modelo.Usuario;

public class Mascotas extends AppCompatActivity {

    ArrayList<Animal> listaAnimales;
    RecyclerView recycler;

    private SALUDSqlHelper saludSqlHelper;
    private SQLiteDatabase db;

    private FloatingActionButton anadirAnimal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
                        if (result.getResultCode() == 101){
                          Intent intent = result.getData();
                          listaAnimales.add((Animal) intent.getSerializableExtra("animal"));
                          recycler.getAdapter().notifyItemInserted(listaAnimales.size());

                          FancyToast.makeText(getApplicationContext(),((Animal) intent.getSerializableExtra("animal")).getNombre() + " añadido correctamente",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,false).show();

                        }else if(result.getResultCode() == 103){
                            Intent intent = result.getData();
                            Animal a = (Animal) intent.getSerializableExtra("animal");
                            String pos = intent.getStringExtra("pos");
                            Log.i("POSssss",pos);
                            Log.i("AASdasdasd",listaAnimales.get(Integer.parseInt(pos)).toString());

                            listaAnimales.set(Integer.parseInt(pos),a);

                            Log.i("AASdasdasd",listaAnimales.get(Integer.parseInt(pos)).toString());
                            recycler.getAdapter().notifyItemChanged(Integer.parseInt(pos));

                            FancyToast.makeText(getApplicationContext(),a.getNombre() + " modificado correctamente",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,false).show();
                        }else if(result.getResultCode() == RESULT_CANCELED){
                        recycler.getAdapter().notifyDataSetChanged();

                        }
                            Log.i("CODIGO",result.getResultCode()+"");
                    }
                }
        );

        adaptadorAnimales.setOnClickListener(new View.OnClickListener() { //Al pulsar sobre un elemento del recycler lanza una actividad con los datos del animal
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(),DetalleMascota.class);
                intent.putExtra("animal",listaAnimales.get(recycler.getChildAdapterPosition(view)));
                activityResultLauncher.launch(intent);
                Log.i("Animal seleccionado",""+listaAnimales.get(recycler.getChildAdapterPosition(view)));

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
                        Intent intent = new Intent(getApplicationContext(),EditarMascota.class);
                        intent.putExtra("animal",listaAnimales.get(viewHolder.getAdapterPosition()));
                        intent.putExtra("pos",String.valueOf(viewHolder.getAdapterPosition()));
                        Log.i("POS",viewHolder.getAdapterPosition()+"");
                        activityResultLauncher.launch(intent);
                            break;
                    case ItemTouchHelper.RIGHT:
                        // TODO Dialogo de borrar
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

       cursor.close();

        Log.i("ANIMALES",animales.toString());
        return animales;
    }

    private Usuario getUsuario() { // Recojo el usuario registrado
        Usuario usuario = new Usuario();
        usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        return usuario;
    }
}