package com.example.smcontrol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import model.Entradas;
import model.Static;

public class ReporteEntrada extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener  {

    private List<Entradas> listEntrada=new ArrayList<Entradas>();
    ArrayAdapter<Entradas> arrayAdapterEntrada;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ListView listvw_entrada;
    EditText txtbuscador;
    String t1,t2,t3,t4,t5;
    //
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    TextView correoTrabajador,nombreTrabajador;
    //
    View header;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte_entrada);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        //
        header = navigationView.getHeaderView(0);
        correoTrabajador = (TextView) header.findViewById(R.id.tv_email);
        correoTrabajador.setText(Static.correo);
        nombreTrabajador = (TextView) header.findViewById(R.id.tv_nombre);
        nombreTrabajador.setText(Static.nombre);
        txtbuscador=findViewById(R.id.txt_buscador);
        //
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toogle);
        toogle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        //
        listvw_entrada=findViewById(R.id.lv_datos);
        inicializarFirebase();
        listaEntradas();
        detalle();
        Buscador();


    }

    public void listaEntradas() {
      databaseReference.child("Entradas").addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
           listEntrada.clear();
           for (DataSnapshot dataSnapshot : snapshot.getChildren())     {
           Entradas p = dataSnapshot.getValue(Entradas.class);
           listEntrada.add(p);
           arrayAdapterEntrada = new ArrayAdapter<Entradas>(ReporteEntrada.this, R.layout.lista_items_reporte,listEntrada);
           listvw_entrada.setAdapter(arrayAdapterEntrada);
           }
          }

          @Override
          public void onCancelled(@NonNull @NotNull DatabaseError error) {

          }
      });
    }

    private void inicializarFirebase()   {
        FirebaseApp.initializeApp(this);
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference= firebaseDatabase.getReference();
    }
    public void detalle()   {
        listvw_entrada.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                t1=listEntrada.get(position).getCod_prod();
                t2=listEntrada.get(position).getDni();
                t3=listEntrada.get(position).getFecha_ingreso();
                t4=listEntrada.get(position).getHora_ingreso();
                t5=""+listEntrada.get(position).getCantidad_entrante();
                mostrarDetalle(t1,t2,t3,t4,t5);
            }
        });
    }


    public void mostrarDetalle(String t1,String t2, String t3, String t4,String t5)    {
        AlertDialog.Builder builder= new AlertDialog.Builder(ReporteEntrada.this);
        LayoutInflater inflater= getLayoutInflater();
        View view = inflater.inflate(R.layout.reporte,null);
        builder.setView(view);
        AlertDialog dialog= builder.create();
        dialog.show();
        TextView txt1=view.findViewById(R.id.text1);
        TextView txt2=view.findViewById(R.id.text2);
        TextView txt3=view.findViewById(R.id.text3);
        TextView txt4=view.findViewById(R.id.text4);
        TextView txt5=view.findViewById(R.id.text5);

        txt1.setText("Codigo de producto : "+t1);
        txt2.setText("Dni del almacenero : "+t2);
        txt3.setText("Fecha de entrada : "+t3);
        txt4.setText("Hora de entrada : "+t4);
        txt5.setText("Cantidad entrante : "+t5);
        Button btncancelar= view.findViewById(R.id.btnsalir_reporte);
        btncancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    public void Buscador(){

                txtbuscador.addTextChangedListener(new TextWatcher() {
                Boolean status=false;
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    try {
                        arrayAdapterEntrada.getFilter().filter(s);
                    }catch (Exception e)    {
                      status=true;
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(status) {
                    Toast.makeText(getApplicationContext(),"No hay entradas por mostrar",Toast.LENGTH_SHORT).show();
                    }
                }
            });


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Static.OpcionesNav(item,this);
        return true;
    }

}