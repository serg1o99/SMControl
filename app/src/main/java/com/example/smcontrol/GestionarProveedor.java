package com.example.smcontrol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import model.Proveedor;
import model.Trabajador;

public class GestionarProveedor extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private EditText et_codigo,et_nombre,et_nombreEmpresa,et_correo,et_direccion,et_telefono,et_fecha;
    //
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    //
    public String nombre,nombreEmpresa,correo,direccion,telefono,codigo,fecha;
    //
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestionar_proveedor);
        //
        drawerLayout = findViewById(R.id.gestion_proveedor);
        navigationView = findViewById(R.id.nav_view_prov__);
        toolbar = findViewById(R.id.toolbarprov_);

        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toogle);
        toogle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        //
        et_codigo        =  (EditText) findViewById(R.id.txt_cod);
        et_nombre        =  (EditText) findViewById(R.id.txt_nom);
        et_correo        =  (EditText) findViewById(R.id.txt_correo_prov);
        et_direccion     =  (EditText) findViewById(R.id.txt_direccion);
        et_telefono      =  (EditText) findViewById(R.id.txt_telefono);
        et_fecha         =  (EditText) findViewById(R.id.txt_fecha);
        et_nombreEmpresa =  (EditText) findViewById(R.id.txt_nom_empresa);
        inicializarFirebase();
    }

    private void inicializarFirebase(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase=FirebaseDatabase.getInstance().getInstance();
        databaseReference=firebaseDatabase.getReference();
    }

    public void crearProveedor(View view) {

        codigo      = et_codigo.getText().toString();
        nombre      = et_nombre.getText().toString();
        correo      = et_correo.getText().toString();
        direccion   = et_direccion.getText().toString();
        telefono    = et_telefono.getText().toString();
        fecha       = et_fecha.getText().toString();
        nombreEmpresa = et_nombreEmpresa.getText().toString();

        if( this.codigo.equals("") || nombre.equals("") || correo.equals("") || direccion.equals("") || telefono.equals("") || nombreEmpresa.equals("") || fecha.equals("")){
            validarCampos();
        }else   {
            insertar();
        }
    }

    public void insertar()  {
        Proveedor obj = new Proveedor();
        obj.setCodigo(codigo);
        obj.setNombreProveedor(nombre);
        obj.setCorreo(correo);
        obj.setDireccion(direccion);
        obj.setTelefono(telefono);
        obj.setFecha(fecha);
        obj.setNombreEmpresa(nombreEmpresa);

        databaseReference.child("Proveedor").child(""+obj.getCodigo()).setValue(obj);
        Toast.makeText(this,"Agregado",Toast.LENGTH_SHORT).show();
        limpiarCampos();
    }

    public void validarCampos() {
        if(codigo.isEmpty()) {
            Toast.makeText(this,"Campo codigo obligatorio",Toast.LENGTH_SHORT).show();
            et_codigo.requestFocus();
        }else if(nombre.isEmpty()) {
            Toast.makeText(this,"Campo nombre obligatorio",Toast.LENGTH_SHORT).show();
            et_nombre.requestFocus();
        }else if(correo.isEmpty())    {
            Toast.makeText(this,"Campo correo obligatorio",Toast.LENGTH_SHORT).show();
            et_correo.requestFocus();
        }else if(direccion.isEmpty()) {
            Toast.makeText(this,"Campo direccion obligatorio",Toast.LENGTH_SHORT).show();
            et_direccion.requestFocus();
        }else if(telefono.isEmpty())  {
            Toast.makeText(this,"Campo telefono obligatorio",Toast.LENGTH_SHORT).show();
            et_telefono.requestFocus();
        }else if(fecha.isEmpty())     {
            Toast.makeText(this,"Campo fecha obligatorio",Toast.LENGTH_SHORT).show();
            et_fecha.requestFocus();
        }else if(nombreEmpresa.isEmpty())     {
            Toast.makeText(this,"Campo nombre empresa obligatorio",Toast.LENGTH_SHORT).show();
            et_nombreEmpresa.requestFocus();
        }
    }

    public void limpiarCampos(){
        et_codigo.setText("");
        et_nombre.setText("");
        et_correo.setText("");
        et_direccion.setText("");
        et_telefono.setText("");
        et_fecha.setText("");
        et_nombreEmpresa.setText("");
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