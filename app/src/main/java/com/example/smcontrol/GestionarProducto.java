package com.example.smcontrol;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import model.Categoria;
import model.Producto;
import model.Proveedor;
import model.Static;

public class GestionarProducto extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private EditText et_codigo,et_nombre,et_stock,et_precio;
    public AutoCompleteTextView atv_categoria;
    private static  final  int GALLERY_INTENT = 1;
    //
    private StorageReference storageReference;
    private ProgressDialog progressDialog;
    //
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    //
    View header;
    TextView correoTrabajador,nombreTrabajador;

    public String codigo,nombre,stock,precio,categoria;
    //
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestionar_producto);
        //
        drawerLayout = findViewById(R.id.gestion_producto);
        navigationView = findViewById(R.id.nav_view_prod);
        //
        header = navigationView.getHeaderView(0);
        correoTrabajador = (TextView) header.findViewById(R.id.tv_email);
        correoTrabajador.setText(Static.correo);
        nombreTrabajador = (TextView) header.findViewById(R.id.tv_nombre);
        nombreTrabajador.setText(Static.nombre);
        //
        toolbar = findViewById(R.id.toolbar_prod);

        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toogle);
        toogle.syncState();
        //
        storageReference = FirebaseStorage.getInstance().getReference();
        //
        navigationView.setNavigationItemSelectedListener(this);
        //
        et_codigo      =  (EditText) findViewById(R.id.txt_codigo);
        et_nombre      =  (EditText) findViewById(R.id.txt_nombre);
        et_stock       =  (EditText) findViewById(R.id.txt_stock);
        et_precio      =  (EditText) findViewById(R.id.txt_precio);
        atv_categoria  = (AutoCompleteTextView) findViewById(R.id.txt_categorias);
        //
        inicializarFirebase();
        listarCategorias();
    }

    private void listarCategorias() {
        final ArrayAdapter<String> autoComplete = new ArrayAdapter<>(this, R.layout.lista_items);
        databaseReference.child("Categoria").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshotCat : snapshot.getChildren()){
                    String nombreCat = snapshotCat.child("nombre").getValue(String.class);
                    System.out.println(nombreCat );
                    autoComplete.add(nombreCat);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
        atv_categoria.setAdapter(autoComplete);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_INTENT && resultCode == RESULT_OK)  {
            Uri uri=data.getData();
            StorageReference filePath=storageReference.child("img_Productos").child(uri.getLastPathSegment());
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getApplicationContext(),"Se subio exitosamente la foto.",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void inicializarFirebase(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase=FirebaseDatabase.getInstance().getInstance();
        databaseReference=firebaseDatabase.getReference();
    }

    public void crearProducto(View view) {

        codigo       = et_codigo.getText().toString();
        nombre       = et_nombre.getText().toString();
        stock        = et_stock.getText().toString();
        precio       = et_precio.getText().toString();
        categoria    = atv_categoria.getText().toString();

        if( this.codigo.equals("") || nombre.equals("") || stock.equals("") || precio.equals("") || categoria.equals("") || categoria.equals("Escoja la categoria")){
            validarCampos();
        }else   {
            insertar();
        }
    }

    public void insertar()  {
        AlertDialog.Builder alerta=new AlertDialog.Builder(this,R.style.AppCompatAlertDialogStyle);
        alerta.setMessage("¿Está seguro de que quiere crear un nuevo Producto ?").setTitle("Registrar").setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Producto obj = new Producto();
                obj.setCodigo(codigo);
                obj.setNombre(nombre);
                obj.setStock(stock);
                obj.setPrecio(precio);
                obj.setCategoria(categoria);

                databaseReference.child("Producto").child(""+obj.getCodigo()).setValue(obj);
                Toast.makeText(getApplicationContext(),"Producto Agregado",Toast.LENGTH_SHORT).show();
                limpiarCampos();
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
            atv_categoria.requestFocus();
        }
    }

    public void limpiarCampos(){
        et_codigo.setText("");
        et_nombre.setText("");
        et_stock.setText("");
        et_precio.setText("");
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

    public void SubirFoto(View view){
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,GALLERY_INTENT);
    }
}