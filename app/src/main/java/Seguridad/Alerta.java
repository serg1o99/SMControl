package Seguridad;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.smcontrol.R;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import model.ProductListCallback;
import model.Producto;
import negocio.AlmacenActivity;

public class Alerta {

    private Context context;
    private Activity activity;

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private final List<Producto> ListaProducto = new ArrayList<Producto>();

    public Alerta(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    public void inicializarFirebase(){
        FirebaseApp.initializeApp(context);
        firebaseDatabase  = firebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    public void listaProductos(final ProductListCallback myCallback) {
         databaseReference.child("Producto").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ListaProducto.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren() ){
                    Producto obj = dataSnapshot.getValue(Producto.class);
                    ListaProducto.add(obj);
                }
                myCallback.onCallback(ListaProducto);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void AlertaStock(boolean alerta){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = activity.getLayoutInflater();

        View view = inflater.inflate(R.layout.alerta_stock,null);
        builder.setView(view);

        AlertDialog dialog= builder.create();

        if(alerta){
            dialog.show();
        }

        TextView txt=view.findViewById(R.id.text_dialog);
        txt.setText("Algunos productos están por agotarse");
        botones(view,dialog);
    }

    private void botones(View view, Dialog dialog){
        Button btnCancelar = view.findViewById(R.id.btncancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button btnAceptar = view.findViewById(R.id.btnaceptar);
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(activity , AlmacenActivity.class);
                context.startActivity(i);
            }
        });
    }
}
