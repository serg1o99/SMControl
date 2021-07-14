package Seguridad;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.smcontrol.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import model.Producto;

public class Alerta {

    private Context context;
    private Activity activity;
    //firebase
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    //lista
    private List<Producto> ListaProducto=new ArrayList<Producto>();
    private ArrayAdapter<Producto> arrayAdapter;


    public Alerta(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    public void inicializarFirebase(){
        FirebaseApp.initializeApp(context);
        firebaseDatabase=firebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();
    }

    public void ListadeProductos(){
        databaseReference.child("Producto").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren() ){
                    Producto objprod=dataSnapshot.getValue(Producto.class);
                    ListaProducto.add(objprod);
                    arrayAdapter=new ArrayAdapter<Producto>(context, android.R.layout.simple_list_item_1,ListaProducto);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public String BusquedaStock(){
        boolean alerta=false;
        int Stock[]=new int[ListaProducto.size()];
        int i=0;
        while (i<ListaProducto.size() && alerta == false)   {
            Stock[i]=Integer.parseInt(ListaProducto.get(i).getStock());
            if(Stock[i]<1)  {
                alerta=true;
                break;
            }
            i++;
        }
        return ""+alerta;
    }

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    public FirebaseDatabase getFirebaseDatabase() {
        return firebaseDatabase;
    }
}
