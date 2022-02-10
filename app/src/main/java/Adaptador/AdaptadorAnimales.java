package Adaptador;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.R;

import java.util.ArrayList;

import modelo.Animal;

public class AdaptadorAnimales extends RecyclerView.Adapter<AdaptadorAnimales.ViewHolderAnimales> implements View.OnClickListener{

    private ArrayList<Animal> listaAnimales;
    private View.OnClickListener listener;

    public AdaptadorAnimales(ArrayList<Animal> listaAnimales) {
        this.listaAnimales = listaAnimales;
    }

    @NonNull
    @Override
    public ViewHolderAnimales onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_animales, parent, false);

        view.setOnClickListener(this);

        return new ViewHolderAnimales(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderAnimales holder, int position) {

        holder.rellenarDatos(listaAnimales.get(position));

    }

    @Override
    public int getItemCount() {
        return listaAnimales.size();
    }

    @Override
    public void onClick(View view) {
        if(listener != null){
            listener.onClick(view);
        }
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    public class ViewHolderAnimales extends RecyclerView.ViewHolder {

        TextView nombreMascota;
        TextView edadMascota;
        TextView especieMascota;
        TextView razaMascota;
        TextView sexoMascota;
        ImageView imagenMascota;

        public ViewHolderAnimales(@NonNull View itemView) {
            super(itemView);
            nombreMascota = itemView.findViewById(R.id.nombreMascota);
            edadMascota = itemView.findViewById(R.id.edadMascota);
            razaMascota = itemView.findViewById(R.id.razaMascota);
            sexoMascota = itemView.findViewById(R.id.sexoMascota);
            especieMascota = itemView.findViewById(R.id.especieMascota);
            imagenMascota = itemView.findViewById(R.id.imagenMascota);
        }

        public void rellenarDatos(Animal animal) {

            nombreMascota.setText(animal.getNombre());

            if (animal.getMes() > 1) {
                if (animal.getAnyo() > 1) {
                    edadMascota.setText(animal.getAnyo() + " años y " + animal.getMes() + " meses");
                } else {
                    edadMascota.setText(animal.getAnyo() + " año y " + animal.getMes() + " meses");
                }
            }

            else if (animal.getMes() == 1) {
                if (animal.getAnyo() > 1) {
                    edadMascota.setText(animal.getAnyo() + " años y " + animal.getMes() + " mes");
                } else {
                    edadMascota.setText(animal.getAnyo() + " año y " + animal.getMes() + " mes");
                }
            }

            else if(animal.getMes() == 0){
                if (animal.getAnyo() > 1) {
                    edadMascota.setText(animal.getAnyo() + " años y " + animal.getMes() + " meses");
                } else {
                    edadMascota.setText(animal.getAnyo() + " año y " + animal.getMes() + " meses");
                }
            }

            razaMascota.setText(animal.getRaza());
            especieMascota.setText(animal.getEspecie());
            sexoMascota.setText(animal.getSexo());
            imagenMascota.setImageResource(animal.getImagen());
        }
    }
}
