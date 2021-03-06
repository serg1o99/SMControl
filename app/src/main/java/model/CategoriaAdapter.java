package model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.smcontrol.R;

import java.util.ArrayList;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.MyViewHolder> implements View.OnClickListener{

    Context context;
    ArrayList<Categoria> listaCategoria;
    private View.OnClickListener listener;

    public CategoriaAdapter(Context context, ArrayList<Categoria> listaCategoria) {
        this.context = context;
        this.listaCategoria = listaCategoria;
    }

    @NonNull
    @Override
    public CategoriaAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.card_list,parent,false);
        v.setOnClickListener(this);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriaAdapter.MyViewHolder holder, int position) {
        Categoria c = listaCategoria.get(position);
        holder.nombre.setText(c.getCodigo());
        holder.descricion.setText(c.getNombre());
        holder.txt.setText("Tipo: ");
        String url=c.getUrl();
        Glide.with(this.context).load(url).placeholder(R.drawable.avatar).error(R.drawable.avatar).into(holder.foto);
    }

    public void setOnClickListener(View.OnClickListener listener)   {
        this.listener=listener;
    }

    @Override
    public void onClick(View v) {
        if(listener!=null)  {
            listener.onClick(v);
        }
    }

    @Override
    public int getItemCount() {
        return listaCategoria.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder   {
        TextView nombre,descricion,txt;
        ImageView foto;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.tvnombre);
            descricion = itemView.findViewById(R.id.tvrol);
            txt = itemView.findViewById(R.id.txt);
            foto= itemView.findViewById(R.id.ivtrabajador);
        }
    }


}
