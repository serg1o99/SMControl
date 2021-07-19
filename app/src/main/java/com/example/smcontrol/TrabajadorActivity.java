package com.example.smcontrol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import model.Static;
import model.Trabajador;
import model.TrabajadorAdapter;


public class TrabajadorActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView recyclerView;
    ArrayList<Trabajador> listaTrabajador;
    TrabajadorAdapter trabajadorAdapter;
    DatabaseReference database;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    FloatingActionButton fab;
    Trabajador t;
    //
    FirebaseAuth mAuth;
    //
    View header;
    TextView correoTrabajador,nombreTrabajador;
    //variables de guardado para actualizar
    public String uid,dni,correo,rol,nombre,apellido,contraseña,url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trabajador);
        //
        drawerLayout = findViewById(R.id.trabajador);
        navigationView = findViewById(R.id.nav_view__);
        //
        header = navigationView.getHeaderView(0);
        correoTrabajador = (TextView) header.findViewById(R.id.tv_email);
        correoTrabajador.setText(Static.correo);
        nombreTrabajador = (TextView) header.findViewById(R.id.tv_nombre);
        nombreTrabajador.setText(Static.nombre);
        //
        toolbar = findViewById(R.id.toolbar_);
        //floating button
        fab=findViewById(R.id.fab);
        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toogle);
        toogle.syncState();
        //
        navigationView.setNavigationItemSelectedListener(this);

        recyclerView = findViewById(R.id.recyclerView);
        database = FirebaseDatabase.getInstance().getReference("Trabajador");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //metodo para las acciones de los cardview
        OnClickRecyclerViewListener();

        //metodo para las acciones del floating button
        OnClickFloatingButtonListener();
        //método para el llamado a la base de datos
        FirebaseEventListener();

    }



    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
           startActivity(new Intent(this,MenuActivity.class));
        }
    }

    public void OnClickFloatingButtonListener()   {
    fab.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        Intent it=new Intent(getApplicationContext(), GestionarTrabajador.class);
        startActivity(it);
        finish();
        }
    });
    }

    public void OnClickRecyclerViewListener()   {

        listaTrabajador=new ArrayList<>();
        trabajadorAdapter=new TrabajadorAdapter(TrabajadorActivity.this,listaTrabajador);
        trabajadorAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent itactualizar=new Intent(getApplicationContext(),ActualizarTrabajador.class);
                //guardamos los datos a actualizar de cada cardview
                uid=listaTrabajador.get(recyclerView.getChildAdapterPosition(v)).getUid();
                dni=listaTrabajador.get(recyclerView.getChildAdapterPosition(v)).getDni();
                nombre=listaTrabajador.get(recyclerView.getChildAdapterPosition(v)).getNombre();
                apellido=listaTrabajador.get(recyclerView.getChildAdapterPosition(v)).getApellido();
                correo=listaTrabajador.get(recyclerView.getChildAdapterPosition(v)).getCorreo();
                contraseña=listaTrabajador.get(recyclerView.getChildAdapterPosition(v)).getContraseña();
                rol=listaTrabajador.get(recyclerView.getChildAdapterPosition(v)).getRol();
                url=listaTrabajador.get(recyclerView.getChildAdapterPosition(v)).getUrl();
                itactualizar.putExtra("uid",uid);
                itactualizar.putExtra("dni",dni);
                itactualizar.putExtra("nombre",nombre);
                itactualizar.putExtra("apellido",apellido);
                itactualizar.putExtra("correo",correo);
                itactualizar.putExtra("contraseña",contraseña);
                itactualizar.putExtra("rol",rol);
                itactualizar.putExtra("url",url);
                startActivity(itactualizar);
                finish();

            }
        });

        recyclerView.setAdapter(trabajadorAdapter);

    }

    public void FirebaseEventListener() {
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot){
                listaTrabajador.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Trabajador t=dataSnapshot.getValue(Trabajador.class);
                    listaTrabajador.add(t);
                }
                trabajadorAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Static.OpcionesNav(item,this);
        return true;
    }


}