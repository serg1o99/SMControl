package com.example.smcontrol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import model.Producto;
import model.Static;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    public CardView cardTrabajador, cardProducto, cardCategoria, cardProveedor,  cardReporte;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    FirebaseAuth mAuth;
    //
    View header;
    TextView correoTrabajador,nombreTrabajador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        //
        header = navigationView.getHeaderView(0);
        correoTrabajador = (TextView) header.findViewById(R.id.tv_email);
        correoTrabajador.setText(Static.correo);
        nombreTrabajador = (TextView) header.findViewById(R.id.tv_nombre);
        nombreTrabajador.setText(Static.nombre);
        //
        toolbar = findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toogle);
        toogle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance();

        cardTrabajador = (CardView) findViewById(R.id.cardTrabajador);
        cardProducto = (CardView) findViewById(R.id.cardProducto);
        cardCategoria = (CardView) findViewById(R.id.cardCategoria);
        cardProveedor = (CardView) findViewById(R.id.cardProveedor);
        cardReporte = (CardView) findViewById(R.id.cardReporte);

        cardTrabajador.setOnClickListener(this);
        cardProducto.setOnClickListener(this);
        cardCategoria.setOnClickListener(this);
        cardProveedor.setOnClickListener(this);
        cardReporte.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()){
            case R.id.cardTrabajador:
                i = new Intent(this,TrabajadorActivity.class);
                startActivity(i);
                break;
            case R.id.cardProducto:
                i = new Intent(this, ProductoActivity.class);
                startActivity(i);
                break;
            case R.id.cardCategoria:
                i = new Intent(this, CategoriaActivity.class);
                startActivity(i);
                break;
            case R.id.cardProveedor:
                i = new Intent(this, ProveedorActivity.class);
                startActivity(i);
                break;
            case R.id.cardReporte:
                i = new Intent(this, ReporteActivity.class);
                startActivity(i);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {

            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Static.OpcionesNav(item,this);
        return true;
    }
}