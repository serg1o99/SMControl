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
import com.example.smcontrol.TrabajadorActivity;

import java.util.ArrayList;

public class TrabajadorAdapter extends RecyclerView.Adapter<TrabajadorAdapter.MyViewHolder> implements View.OnClickListener {
    Context context;
    ArrayList<Trabajador> listaTrabajador;
    private View.OnClickListener listener;

    public TrabajadorAdapter(Context context, ArrayList<Trabajador> listaTrabajador) {
        this.context = context;
        this.listaTrabajador = listaTrabajador;
    }

    @NonNull
    @Override
    public TrabajadorAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.card_list,parent,false);
        v.setOnClickListener(this);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TrabajadorAdapter.MyViewHolder holder, int position) {
        Trabajador t=listaTrabajador.get(position);
        holder.nombre.setText(t.getNombre()+" "+t.getApellido());
        holder.rol.setText(t.getRol());
        String url=t.getUrl();
        Glide.with(this.context).load(url).placeholder(R.drawable.avatar).error(R.drawable.avatar).into(holder.foto);

    }

    @Override
    public int getItemCount() {
     return listaTrabajador.size();
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

    public static class MyViewHolder extends RecyclerView.ViewHolder   {
        TextView nombre,rol;
        ImageView foto;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.tvnombre);
            rol = itemView.findViewById(R.id.tvrol);
            foto= itemView.findViewById(R.id.ivtrabajador);
        }
    }
}
