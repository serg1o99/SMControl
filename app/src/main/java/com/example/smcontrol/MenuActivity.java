package com.example.smcontrol;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import com.google.android.material.navigation.NavigationView;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    public CardView cardTrabajador, cardProducto, cardCategoria, cardProveedor,  cardReporte;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toogle);
        toogle.syncState();

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
                break;
        }
    }
}