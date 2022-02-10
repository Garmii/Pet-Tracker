package Adaptador;

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
import modelo.Seguimiento;

public class AdaptadorSeguimiento extends RecyclerView.Adapter<AdaptadorSeguimiento.ViewHolderSeguimiento> implements View.OnClickListener{

    private ArrayList<Seguimiento> listaSeguimiento;
    private View.OnClickListener listener;

    public AdaptadorSeguimiento(ArrayList<Seguimiento> listaSeguimiento) {
        this.listaSeguimiento = listaSeguimiento;
    }

    @NonNull
    @Override
    public ViewHolderSeguimiento onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_seguimientos, parent, false);

        view.setOnClickListener(this);

        return new ViewHolderSeguimiento(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderSeguimiento holder, int position) {

        holder.rellenarDatos(listaSeguimiento.get(position));

    }

    @Override
    public int getItemCount() {
        return listaSeguimiento.size();
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

    public class ViewHolderSeguimiento extends RecyclerView.ViewHolder {

        TextView tipo;
        TextView peso;
        TextView descripcion;
        TextView fecha;
        TextView vacuna;


        public ViewHolderSeguimiento(@NonNull View itemView) {
            super(itemView);
            tipo = itemView.findViewById(R.id.tipoSeguimiento);
            peso = itemView.findViewById(R.id.pesoSeguimiento);
            descripcion = itemView.findViewById(R.id.descripcionSeguimiento);
            fecha = itemView.findViewById(R.id.fechaSeguimiento);
            vacuna = itemView.findViewById(R.id.vacunaSeguimiento);

        }

        public void rellenarDatos(Seguimiento seguimiento) {

           tipo.setText(seguimiento.getTipo());
        }
    }
}

