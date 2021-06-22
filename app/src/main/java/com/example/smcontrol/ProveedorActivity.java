package com.example.smcontrol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import model.Trabajador;
import model.TrabajadorAdapter;

public class ProveedorActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Trabajador> listaTrabajador;
    TrabajadorAdapter trabajadorAdapter;
    //FirebaseFirestore db;
    DatabaseReference database;
    //
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proveedor);
    }
}