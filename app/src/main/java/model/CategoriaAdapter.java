package model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smcontrol.R;

import java.util.ArrayList;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.MyViewHolder>{

    Context context;
    ArrayList<Categoria> listaCategoria;

    public CategoriaAdapter(Context context, ArrayList<Categoria> listaCategoria) {
        this.context = context;
        this.listaCategoria = listaCategoria;
    }

    @NonNull
    @Override
    public CategoriaAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.card_list,parent,false);
        return new CategoriaAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriaAdapter.MyViewHolder holder, int position) {
        Categoria c = listaCategoria.get(position);
        holder.nombre.setText(c.getNombre());
        holder.descricion.setText(c.getDescripcion());
        holder.txt.setText("Descripcion: ");
    }

    @Override
    public int getItemCount() {
        return listaCategoria.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder   {
        TextView nombre,descricion,txt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.tvnombre);
            descricion = itemView.findViewById(R.id.tvrol);
            txt = itemView.findViewById(R.id.txt);
        }
    }


}
