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

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.MyViewHolder> implements View.OnClickListener{

    Context context;
    ArrayList<Producto> listaProducto;
    private View.OnClickListener listener;

    public ProductoAdapter(Context context, ArrayList<Producto> listaProducto) {
        this.context = context;
        this.listaProducto = listaProducto;
    }

    @NonNull
    @Override
    public ProductoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.card_list,parent,false);
        v.setOnClickListener(this);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoAdapter.MyViewHolder holder, int position) {
        Producto p = listaProducto.get(position);
        holder.nombreProveedor.setText(p.getNombre());
        holder.nombreEmpresa.setText(p.getCategoria());
        holder.txt.setText("Categoria: ");
    }

    @Override
    public int getItemCount() {
        return listaProducto.size();
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
