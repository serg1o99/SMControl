package com.example.smcontrol;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import model.Trabajador;

public class Gestionar_TrabajadorActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //Atributos
    private EditText et_dni,et_nombre,et_apellido,et_ncorreo,et_npass;
    private AutoCompleteTextView atv_rol;
    //FireBase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    //Variables globales
    public String nombre,apellido,correo,contraseña,rol,dni;
    //
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    //
    @RequiresApi(api= Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestionar__trabajador);
        //
        drawerLayout = findViewById(R.id.gestion_trabajador);
        navigationView = findViewById(R.id.nav_view_s);
        toolbar = findViewById(R.id.toolbarr);

        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toogle);
        toogle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        //
        //enlazamos los atributos con los componentes
        et_dni=(EditText) findViewById(R.id.txt_dni);
        et_nombre=(EditText) findViewById(R.id.txt_nombre);
        et_apellido=(EditText) findViewById(R.id.txt_apellido);
        et_ncorreo=(EditText) findViewById(R.id.txt_ncorreo);
        et_npass=(EditText) findViewById(R.id.txt_npass);
        atv_rol=findViewById(R.id.txt_roles);
        inicializarFirebase();

        ArrayAdapter arrayAdapter=new ArrayAdapter(this,R.layout.lista_roles_layout,getResources().getStringArray(R.array.roles));
        atv_rol.setAdapter(arrayAdapter);
    }

    private void inicializarFirebase(){

        FirebaseApp.initializeApp(this);
        firebaseDatabase=FirebaseDatabase.getInstance().getInstance();
        databaseReference=firebaseDatabase.getReference();

    }

    public void crearTrabajador(View view) {
        //declarando variables locales y asignandolos a los atributos que contienen los componentes
        dni=et_dni.getText().toString();
        nombre=et_nombre.getText().toString();
        apellido=et_apellido.getText().toString();
        correo=et_ncorreo.getText().toString();
        contraseña=et_npass.getText().toString();
        rol=atv_rol.getText().toString();
        if( this.dni.equals("") || nombre.equals("") || apellido.equals("") || correo.equals("") || contraseña.equals("") || rol.equals("Escoja el rol") || rol.equals("") )     {
           validarCampos();
        }else   {
         insertar();
        }
    }

    public void insertar()  {

        //instanciamos un objeto de la clase Trabajador
        Trabajador objTrabajador=new Trabajador();
        objTrabajador.setDni(dni);
        objTrabajador.setNombre(nombre);
        objTrabajador.setApellido(apellido);
        objTrabajador.setCorreo(correo);
        objTrabajador.setContraseña(contraseña);
        objTrabajador.setRol(rol);
        databaseReference.child("Trabajador").child(""+objTrabajador.getDni()).setValue(objTrabajador);
        Toast.makeText(this,"Agregado",Toast.LENGTH_SHORT).show();
        limpiarCampos();


    }

    public void validarCampos() {
        if(dni.isEmpty()) {
            Toast.makeText(this,"Campo dni obligatorio",Toast.LENGTH_SHORT).show();
            et_dni.requestFocus();
        }else if(nombre.isEmpty()) {
            Toast.makeText(this,"Campo nombre obligatorio",Toast.LENGTH_SHORT).show();
            et_nombre.requestFocus();
        }else if(apellido.isEmpty())    {
            Toast.makeText(this,"Campo apellido obligatorio",Toast.LENGTH_SHORT).show();
            et_apellido.requestFocus();
        }else if(correo.isEmpty()) {
            Toast.makeText(this,"Campo correo obligatorio",Toast.LENGTH_SHORT).show();
            et_ncorreo.requestFocus();
        }else if(contraseña.isEmpty())  {
            Toast.makeText(this,"Campo contraseña obligatorio",Toast.LENGTH_SHORT).show();
            et_npass.requestFocus();
        }else if(rol.isEmpty())     {
            Toast.makeText(this,"Campo rol obligatorio",Toast.LENGTH_SHORT).show();
            atv_rol.requestFocus();
        }
    }

    public void limpiarCampos(){
        et_dni.setText("");
        et_nombre.setText("");
        et_apellido.setText("");
        et_ncorreo.setText("");
        et_npass.setText("");
        et_dni.requestFocus();

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