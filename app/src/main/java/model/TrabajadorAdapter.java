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
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.tvnombre);
            rol = itemView.findViewById(R.id.tvrol);
        }
    }
}
