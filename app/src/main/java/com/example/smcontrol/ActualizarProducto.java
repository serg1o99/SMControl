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
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import model.Producto;
import model.Proveedor;

public class ActualizarProducto extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private EditText et_codigo,et_nombre,et_stock,et_precio,et_categoria;
    //
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    //
    public String codigo,nombre,stock,precio,categoria;
    //
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_producto);
        //
        drawerLayout = findViewById(R.id.Actualizar_producto);
        navigationView = findViewById(R.id.nav_view_prod);
        toolbar = findViewById(R.id.toolbar_prod);

        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toogle);
        toogle.syncState();
        //
        navigationView.setNavigationItemSelectedListener(this);
        //
        et_codigo     =  (EditText) findViewById(R.id.txt_codigo);
        et_nombre     =  (EditText) findViewById(R.id.txt_nombre);
        et_stock      =  (EditText) findViewById(R.id.txt_stock);
        et_precio     =  (EditText) findViewById(R.id.txt_precio);
        et_categoria  =  (EditText) findViewById(R.id.txt_categoria);
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

        codigo       = getIntent().getStringExtra("codigo");
        nombre       = getIntent().getStringExtra("nombre");
        stock        = getIntent().getStringExtra("stock");
        precio       = getIntent().getStringExtra("precio");
        categoria    = getIntent().getStringExtra("categoria");

        et_codigo.setText(codigo);
        et_nombre.setText(nombre);
        et_stock.setText(stock);
        et_precio.setText(precio);
        et_categoria.setText(categoria);
    }

    public void actualizarProducto(View view) {
        codigo       = et_codigo.getText().toString();
        nombre       = et_nombre.getText().toString();
        stock        = et_stock.getText().toString();
        precio       = et_precio.getText().toString();
        categoria    = et_categoria.getText().toString();


        if( this.codigo.equals("") || nombre.equals("") || stock.equals("") || precio.equals("") || categoria.equals("") ){
            validarCampos();
        }else   {
            actualizar();
        }
    }

    public void eliminarProducto(View view) {
        codigo       = et_codigo.getText().toString();
        nombre       = et_nombre.getText().toString();
        stock        = et_stock.getText().toString();
        precio       = et_precio.getText().toString();
        categoria    = et_categoria.getText().toString();


        if( this.codigo.equals("") || nombre.equals("") || stock.equals("") || precio.equals("") || categoria.equals("") ){
            validarCampos();
        }else   {
            eliminar();
        }
    }

    public void actualizar()    {
        AlertDialog.Builder alerta=new AlertDialog.Builder(this);
        alerta.setMessage("¿Está seguro de que quiere actualizar el producto ?").setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Producto obj = new Producto();
                obj.setCodigo(et_codigo.getText().toString().trim());
                obj.setNombre(et_nombre.getText().toString().trim());
                obj.setStock(et_stock.getText().toString().trim());
                obj.setPrecio(et_precio.getText().toString().trim());
                obj.setCategoria(et_categoria.getText().toString().trim());

                databaseReference.child("Producto").child(""+obj.getCodigo()).setValue(obj);
                Toast.makeText(getApplicationContext(),"Producto actualizado",Toast.LENGTH_SHORT).show();
                Intent intent =new Intent(getApplicationContext(),ProductoActivity.class);
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
        AlertDialog.Builder alerta=new AlertDialog.Builder(this);
        alerta.setMessage("¿Está seguro de que quiere eliminar el producto ?").setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Producto obj = new Producto();
                obj.setCodigo(et_codigo.getText().toString().trim());

                databaseReference.child("Producto").child(""+obj.getCodigo()).removeValue();
                Toast.makeText(getApplicationContext(),"Producto eliminado",Toast.LENGTH_SHORT).show();
                Intent intent =new Intent(getApplicationContext(),ProductoActivity.class);
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
        }else if(stock.isEmpty())    {
            Toast.makeText(this,"Campo correo obligatorio",Toast.LENGTH_SHORT).show();
            et_stock.requestFocus();
        }else if(precio.isEmpty()) {
            Toast.makeText(this,"Campo direccion obligatorio",Toast.LENGTH_SHORT).show();
            et_precio.requestFocus();
        }else if(categoria.isEmpty()) {
            Toast.makeText(this, "Campo telefono obligatorio", Toast.LENGTH_SHORT).show();
            et_categoria.requestFocus();
        }
    }

    public void limpiarCampos(){
        et_codigo.setText("");
        et_nombre.setText("");
        et_stock.setText("");
        et_precio.setText("");
        et_categoria.setText("");
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