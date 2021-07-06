package com.example.smcontrol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import model.Categoria;
import model.Trabajador;

public class ActualizarCategoria extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private EditText et_codigo,et_nombre,et_descripcion;
    //
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    //
    public String codigo,nombre,descripcion;
    //
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_categoria);
        //
        drawerLayout = findViewById(R.id.Actualizar_categoria);
        navigationView = findViewById(R.id.nav_view_cat);
        toolbar = findViewById(R.id.toolbar_cat);

        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toogle);
        toogle.syncState();
        //
        navigationView.setNavigationItemSelectedListener(this);
        //
        et_codigo        =  (EditText) findViewById(R.id.txt_cod_cat);
        et_nombre        =  (EditText) findViewById(R.id.txt_nom_cat);
        et_descripcion   =  (EditText) findViewById(R.id.txt_descripcion);
        //
        inicializarFirebase();
        //
        CargarDatosActualizar();

    }

    private void inicializarFirebase(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase=FirebaseDatabase.getInstance().getInstance();
        databaseReference=firebaseDatabase.getReference();
    }

    public void CargarDatosActualizar()     {

        codigo      = getIntent().getStringExtra("codigo");
        nombre      = getIntent().getStringExtra("nombre");
        descripcion = getIntent().getStringExtra("descripcion");

        et_codigo.setText(codigo);
        et_nombre.setText(nombre);
        et_descripcion.setText(descripcion);
    }

    public void actualizarCategoria(View view) {

        codigo      = et_codigo.getText().toString();
        nombre      = et_nombre.getText().toString();
        descripcion = et_descripcion.getText().toString();

        if( this.codigo.equals("") || nombre.equals("") || descripcion.equals("")){
            validarCampos();
        }else   {
            actualizar();
        }
    }

    public void eliminarCategoria(View view) {
        codigo      = et_codigo.getText().toString();
        nombre      = et_nombre.getText().toString();
        descripcion = et_descripcion.getText().toString();

        if( this.codigo.equals("") || nombre.equals("") || descripcion.equals("")){
            validarCampos();
        }else   {
            eliminar();
        }
    }

    public void actualizar()    {

        AlertDialog.Builder alerta=new AlertDialog.Builder(this);
        alerta.setMessage("¿Está seguro de que quiere actualizar la categoria ?").setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Categoria obj = new Categoria();
                obj.setCodigo(et_codigo.getText().toString().trim());
                obj.setNombre(et_nombre.getText().toString().trim());
                obj.setDescripcion(et_descripcion.getText().toString().trim());

                databaseReference.child("Categoria").child(obj.getCodigo()).setValue(obj);
                Toast.makeText(getApplicationContext(),"Categoria actualizar",Toast.LENGTH_SHORT).show();
                Intent intent =new Intent(getApplicationContext(),CategoriaActivity.class);
                startActivity(intent);
                finish();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    public void eliminar()  {
        AlertDialog.Builder alerta=new AlertDialog.Builder(this);
        alerta.setMessage("¿Está seguro de que quiere eliminar la categoria?").setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Categoria obj = new Categoria();
                obj.setCodigo(et_codigo.getText().toString().trim());
                databaseReference.child("Categoria").child(obj.getCodigo()).removeValue();
                Toast.makeText(getApplicationContext(),"Categoria eliminada",Toast.LENGTH_SHORT).show();
                Intent intent =new Intent(getApplicationContext(),CategoriaActivity.class);
                startActivity(intent);
                finish();

            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }

        }).show();
    }

    public void validarCampos() {
        if(codigo.isEmpty()) {
            Toast.makeText(this,"Campo codigo obligatorio",Toast.LENGTH_SHORT).show();
            et_codigo.requestFocus();
        }else if(nombre.isEmpty()) {
            Toast.makeText(this,"Campo nombre obligatorio",Toast.LENGTH_SHORT).show();
            et_nombre.requestFocus();
        }else if(descripcion.isEmpty())    {
            Toast.makeText(this,"Campo descripcion obligatorio",Toast.LENGTH_SHORT).show();
            et_descripcion.requestFocus();
        }
    }

    public void limpiarCampos(){
        et_codigo.setText("");
        et_nombre.setText("");
        et_descripcion.setText("");
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
        Intent i;
        switch (item.getItemId()){
            case R.id.nav_home:
                i = new Intent(this,MenuActivity.class);
                startActivity(i);
                break;
            case R.id.nav_trabajador:
                i = new Intent(this,TrabajadorActivity.class);
                startActivity(i);
                break;
            case R.id.nav_producto:
                i = new Intent(this, ProductoActivity.class);
                startActivity(i);
                break;
            case R.id.nav_categoria:
                i = new Intent(this, CategoriaActivity.class);
                startActivity(i);
                break;
            case R.id.nav_proveedor:
                i = new Intent(this, ProveedorActivity.class);
                startActivity(i);
                break;
            case R.id.nav_reporte:
                i = new Intent(this, ReporteActivity.class);
                startActivity(i);
                break;
        }
        return true;
    }

}