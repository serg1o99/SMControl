package com.example.smcontrol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import model.Static;
import model.Trabajador;

public class ActualizarTrabajador extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //Atributos
    private EditText et_dni,et_nombre,et_apellido,et_ncorreo,et_npass;
    private AutoCompleteTextView atv_rol;
    //FireBase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    //
    View header;
    TextView correoTrabajador,nombreTrabajador;
    //Variables globales
    public String nombre,apellido,correo,contraseña,rol,dni;
    //
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;


    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_trabajador);
        //
        drawerLayout = findViewById(R.id.Actualizar_trabajador);
        navigationView = findViewById(R.id.nav_view_s);
        //
        header = navigationView.getHeaderView(0);
        correoTrabajador = (TextView) header.findViewById(R.id.tv_email);
        correoTrabajador.setText(Static.correo);
        nombreTrabajador = (TextView) header.findViewById(R.id.tv_nombre);
        nombreTrabajador.setText(Static.nombre);
        //
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

        ArrayAdapter arrayAdapter=new ArrayAdapter(this,R.layout.lista_items,getResources().getStringArray(R.array.roles));
        atv_rol.setAdapter(arrayAdapter);
        CargarDatosActualizar();


}


    private void inicializarFirebase(){

        FirebaseApp.initializeApp(this);
        firebaseDatabase=FirebaseDatabase.getInstance().getInstance();
        databaseReference=firebaseDatabase.getReference();

    }

    public void CargarDatosActualizar()     {
        dni=getIntent().getStringExtra("dni");
        nombre=getIntent().getStringExtra("nombre");
        apellido=getIntent().getStringExtra("apellido");
        correo=getIntent().getStringExtra("correo");
        contraseña=getIntent().getStringExtra("contraseña");
        rol=getIntent().getStringExtra("rol");

        et_dni.setText(dni);
        et_nombre.setText(nombre);
        et_apellido.setText(apellido);
        et_ncorreo.setText(correo);
        et_npass.setText(contraseña);

    }

    public void ActualizarTrabajador(View view){
        //declarando variables locales y asignandolos a los atributos que contienen los componentes

        dni         = et_dni.getText().toString();
        nombre      = et_nombre.getText().toString();
        apellido    = et_apellido.getText().toString();
        correo      = et_ncorreo.getText().toString();
        contraseña  = et_npass.getText().toString();
        rol         = atv_rol.getText().toString();

        if( this.dni.equals("") || nombre.equals("") || apellido.equals("") || correo.equals("") || contraseña.equals("") || rol.equals("Escoja el rol") || rol.equals("") )     {
            validarCampos();
        }else   {
            actualizar() ;
        }

    }

    public void EliminarTrabajador(View view){
        //declarando variables locales y asignandolos a los atributos que contienen los componentes

        dni         = et_dni.getText().toString();
        nombre      = et_nombre.getText().toString();
        apellido    = et_apellido.getText().toString();
        correo      = et_ncorreo.getText().toString();
        contraseña  = et_npass.getText().toString();

        if( this.dni.equals("") || nombre.equals("") || apellido.equals("") || correo.equals("") || contraseña.equals("") || rol.equals("Escoja el rol") )     {
            validarCampos();
        }else   {
            eliminar();
        }

    }

    public void actualizar()    {

        AlertDialog.Builder alerta=new AlertDialog.Builder(this,R.style.AppCompatAlertDialogStyle);
        alerta.setMessage("¿Está seguro de que quiere actualizar al trabajador ?").setTitle("Actualizar").setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Trabajador t =new Trabajador();
                t.setDni(et_dni.getText().toString().trim());
                t.setNombre(et_nombre.getText().toString().trim());
                t.setApellido(et_apellido.getText().toString().trim());
                t.setCorreo(et_ncorreo.getText().toString().trim());
                t.setContraseña(et_npass.getText().toString().trim());
                t.setRol(atv_rol.getText().toString().trim());
                databaseReference.child("Trabajador").child(t.getDni()).setValue(t);
                Toast.makeText(getApplicationContext(),"Trabajador Actualizado",Toast.LENGTH_SHORT).show();
                Intent ittrabajador=new Intent(getApplicationContext(),TrabajadorActivity.class);
                startActivity(ittrabajador);
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
        AlertDialog.Builder alerta=new AlertDialog.Builder(this,R.style.AppCompatAlertDialogStyle);
        alerta.setMessage("¿Está seguro de que quiere eliminar al trabajador ?").setTitle("Eliminar").setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Trabajador t =new Trabajador();
                t.setDni(et_dni.getText().toString().trim());
                databaseReference.child("Trabajador").child(t.getDni()).removeValue();
                Toast.makeText(getApplicationContext(),"Trabajador eliminado",Toast.LENGTH_SHORT).show();
                Intent ittrabajador=new Intent(getApplicationContext(),TrabajadorActivity.class);
                startActivity(ittrabajador);
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