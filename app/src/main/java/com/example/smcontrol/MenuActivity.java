package com.example.smcontrol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    public CardView cardTrabajador, cardProducto, cardCategoria, cardProveedor,  cardReporte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

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