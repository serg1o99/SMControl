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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import model.Categoria;
import model.Proveedor;
import model.Static;

public class ActualizarProveedor extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private EditText et_codigo,et_nombre,et_nombreEmpresa,et_correo,et_direccion,et_telefono;
    //
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    //
    View header;
    TextView correoTrabajador,nombreTrabajador;
    //
    public String nombre,nombreEmpresa,correo,direccion,telefono,codigo,url;
    //
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_proveedor);
        //
        drawerLayout = findViewById(R.id.Actualizar_proveedor);
        navigationView = findViewById(R.id.nav_view_prov__);
        //
        header = navigationView.getHeaderView(0);
        correoTrabajador = (TextView) header.findViewById(R.id.tv_email);
        correoTrabajador.setText(Static.correo);
        nombreTrabajador = (TextView) header.findViewById(R.id.tv_nombre);
        nombreTrabajador.setText(Static.nombre);
        //
        toolbar = findViewById(R.id.toolbarprov_);

        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toogle);
        toogle.syncState();
        //
        navigationView.setNavigationItemSelectedListener(this);
        //
        et_codigo        =  (EditText) findViewById(R.id.txt_cod);
        et_nombre        =  (EditText) findViewById(R.id.txt_nom);
        et_correo        =  (EditText) findViewById(R.id.txt_correo_prov);
        et_direccion     =  (EditText) findViewById(R.id.txt_direccion);
        et_telefono      =  (EditText) findViewById(R.id.txt_telefono);
        et_nombreEmpresa =  (EditText) findViewById(R.id.txt_nom_empresa);
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
        correo      = getIntent().getStringExtra("correo");
        direccion   = getIntent().getStringExtra("direccion");
        telefono    = getIntent().getStringExtra("telefono");
        nombreEmpresa   = getIntent().getStringExtra("nombreEmpresa");
        url             =getIntent().getStringExtra("url");
        et_codigo.setText(codigo);
        et_nombre.setText(nombre);
        et_correo.setText(correo);
        et_direccion.setText(direccion);
        et_telefono.setText(telefono);
        et_nombreEmpresa.setText(nombreEmpresa);

    }

    public void actualizarProveedor(View view) {
        codigo      = et_codigo.getText().toString();
        nombre      = et_nombre.getText().toString();
        correo      = et_correo.getText().toString();
        direccion   = et_direccion.getText().toString();
        telefono    = et_telefono.getText().toString();
        nombreEmpresa = et_nombreEmpresa.getText().toString();

        if( this.codigo.equals("") || nombre.equals("") || correo.equals("") || direccion.equals("") || telefono.equals("") || nombreEmpresa.equals("")){
            validarCampos();
        }else   {
            actualizar();
        }
    }

    public void eliminarProveedor(View view) {
        codigo      = et_codigo.getText().toString();
        nombre      = et_nombre.getText().toString();
        correo      = et_correo.getText().toString();
        direccion   = et_direccion.getText().toString();
        telefono    = et_telefono.getText().toString();
        nombreEmpresa = et_nombreEmpresa.getText().toString();

        if( this.codigo.equals("") || nombre.equals("") || correo.equals("") || direccion.equals("") || telefono.equals("") || nombreEmpresa.equals("") ){
            validarCampos();
        }else   {
            eliminar();
        }
    }

    public void actualizar()    {

        AlertDialog.Builder alerta=new AlertDialog.Builder(this,R.style.AppCompatAlertDialogStyle);
        alerta.setMessage("??Est?? seguro de que quiere actualizar el proveedor ?").setTitle("Actualizar").setPositiveButton("S??", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Proveedor obj = new Proveedor();
                obj.setCodigo(et_codigo.getText().toString().trim());
                obj.setNombre(et_nombre.getText().toString().trim());
                obj.setCorreo(et_correo.getText().toString().trim());
                obj.setDireccion(et_direccion.getText().toString().trim());
                obj.setTelefono(et_telefono.getText().toString().trim());
                obj.setEmpresa(et_nombreEmpresa.getText().toString().trim());
                obj.setUrl(url);
                databaseReference.child("Proveedor").child(""+obj.getCodigo()).setValue(obj);
                Toast.makeText(getApplicationContext(),"Proveedor actualizado",Toast.LENGTH_SHORT).show();
                Intent intent =new Intent(getApplicationContext(),ProveedorActivity.class);
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

    public void eliminar()    {

        AlertDialog.Builder alerta=new AlertDialog.Builder(this,R.style.AppCompatAlertDialogStyle);
        alerta.setMessage("??Est?? seguro de que quiere eliminar el proveedor ?").setTitle("Eliminar").setPositiveButton("S??", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Proveedor obj = new Proveedor();
                obj.setCodigo(et_codigo.getText().toString().trim());

                databaseReference.child("Proveedor").child(""+obj.getCodigo()).removeValue();
                Toast.makeText(getApplicationContext(),"Proveedor eliminado",Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this,"Campo c??digo obligatorio",Toast.LENGTH_SHORT).show();
            et_codigo.requestFocus();
        }else if(nombre.isEmpty()) {
            Toast.makeText(this,"Campo nombre obligatorio",Toast.LENGTH_SHORT).show();
            et_nombre.requestFocus();
        }else if(correo.isEmpty())    {
            Toast.makeText(this,"Campo correo obligatorio",Toast.LENGTH_SHORT).show();
            et_correo.requestFocus();
        }else if(direccion.isEmpty()) {
            Toast.makeText(this,"Campo direcci??n obligatorio",Toast.LENGTH_SHORT).show();
            et_direccion.requestFocus();
        }else if(telefono.isEmpty())  {
            Toast.makeText(this,"Campo tel??fono obligatorio",Toast.LENGTH_SHORT).show();
            et_telefono.requestFocus();
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
        Static.OpcionesNav(item,this);
        return true;
    }
}