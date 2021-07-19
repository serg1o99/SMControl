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
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import model.Categoria;
import model.CategoriaAdapter;
import model.Proveedor;
import model.ProveedorAdapter;
import model.Static;

public class CategoriaActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    RecyclerView recyclerView;
    ArrayList<Categoria> listaCategoria;
    CategoriaAdapter categoriaAdapter;
    //
    DatabaseReference database;
    //
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    //
    FloatingActionButton fab;
    //
    Categoria c;
    //
    FirebaseAuth mAuth;
    View header;
    TextView correoTrabajador,nombreTrabajador;

    public String codigo, nombre, descripcion,url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);
        //
        drawerLayout = findViewById(R.id.categoria_layout);
        navigationView = findViewById(R.id.nav_view_cat_);
        //
        header = navigationView.getHeaderView(0);
        correoTrabajador = (TextView) header.findViewById(R.id.tv_email);
        correoTrabajador.setText(Static.correo);
        nombreTrabajador = (TextView) header.findViewById(R.id.tv_nombre);
        nombreTrabajador.setText(Static.nombre);
        //
        toolbar = findViewById(R.id.toolbar_cat__);
        //floating button
        fab=findViewById(R.id.fab);
        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toogle);
        toogle.syncState();
        //
        navigationView.setNavigationItemSelectedListener(this);
        recyclerView = findViewById(R.id.recyclerView_cat);
        database = FirebaseDatabase.getInstance().getReference("Categoria");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //
        listaCategoria   = new ArrayList<>();
        categoriaAdapter = new CategoriaAdapter(CategoriaActivity.this,listaCategoria);
        recyclerView.setAdapter(categoriaAdapter);
        //
        OnClickRecyclerViewListener();
        OnClickFloatingButtonListener();
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

    public void OnClickRecyclerViewListener(){

        listaCategoria = new ArrayList<>();
        categoriaAdapter = new CategoriaAdapter(CategoriaActivity.this,listaCategoria);
        categoriaAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ActualizarCategoria.class);

                codigo = listaCategoria.get(recyclerView.getChildAdapterPosition(v)).getCodigo();
                nombre = listaCategoria.get(recyclerView.getChildAdapterPosition(v)).getNombre();
                descripcion = listaCategoria.get(recyclerView.getChildAdapterPosition(v)).getDescripcion();
                url =   listaCategoria.get(recyclerView.getChildAdapterPosition(v)).getUrl();
                intent.putExtra("codigo",codigo);
                intent.putExtra("nombre",nombre);
                intent.putExtra("descripcion",descripcion);
                intent.putExtra("url",url);
                startActivity(intent);

            }
        });
        recyclerView.setAdapter(categoriaAdapter);
    }

    public void OnClickFloatingButtonListener()   {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(getApplicationContext(),GestionarCategoria.class);
                startActivity(it);
            }
        });
    }

    public void FirebaseEventListener(){
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot){
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Categoria c = dataSnapshot.getValue(Categoria.class);
                    listaCategoria.add(c);
                }
                categoriaAdapter.notifyDataSetChanged();
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