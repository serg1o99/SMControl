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

public class ProveedorAdapter extends RecyclerView.Adapter<ProveedorAdapter.MyViewHolder> implements View.OnClickListener{

    Context context;
    ArrayList<Proveedor> listaProveedor;
    private View.OnClickListener listener;

    public ProveedorAdapter(Context context, ArrayList<Proveedor> listaProveedor) {
        this.context = context;
        this.listaProveedor = listaProveedor;
    }

    @NonNull
    @Override
    public ProveedorAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.card_list,parent,false);
        v.setOnClickListener(this);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProveedorAdapter.MyViewHolder holder, int position) {
        Proveedor p = listaProveedor.get(position);
        holder.nombreProveedor.setText(p.getNombreProveedor());
        holder.nombreEmpresa.setText(p.getNombreEmpresa());
        holder.txt.setText("Empresa: ");
    }

    @Override
    public int getItemCount() {
        return listaProveedor.size();
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
        TextView nombreProveedor,nombreEmpresa,txt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreProveedor = itemView.findViewById(R.id.tvnombre);
            nombreEmpresa = itemView.findViewById(R.id.tvrol);
            txt = itemView.findViewById(R.id.txt);
        }
    }
}
