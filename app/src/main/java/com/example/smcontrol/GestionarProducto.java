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
import android.widget.Button;
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
import com.theartofdev.edmodo.cropper.CropImage;

import model.Foto;
import model.Producto;
import model.Static;

public class GestionarProducto extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private EditText et_codigo,et_nombre,et_stock,et_precio;
    public AutoCompleteTextView atv_categoria;
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

    //para la imagen
    private Button subir;
    private  Uri downloadurl;
    Foto objFoto;

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
        //para subir imagen
        subir = (Button) findViewById(R.id.btnsubirfoto);
        //
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
        et_codigo      =  (EditText) findViewById(R.id.txt_code);
        et_nombre      =  (EditText) findViewById(R.id.txt_nombre_prod_entra);
        et_stock       =  (EditText) findViewById(R.id.txt_stock_prod_entra);
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
                    autoComplete.add(nombreCat);
                }
                autoComplete.add("Otros");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
        atv_categoria.setAdapter(autoComplete);
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
        }else if (!subir.isEnabled())   {
            Toast.makeText(this,"Debe subir una foto",Toast.LENGTH_SHORT).show();
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
                downloadurl=objFoto.getDownloadurl();
                obj.setCodigo("PD0"+codigo);
                obj.setNombre(nombre);
                obj.setStock(stock);
                obj.setPrecio(precio);
                obj.setCategoria(categoria);
                obj.setUrl(""+downloadurl);
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
            Toast.makeText(this,"Campo código obligatorio",Toast.LENGTH_SHORT).show();
            et_codigo.requestFocus();
        }else if(nombre.isEmpty()) {
            Toast.makeText(this,"Campo nombre obligatorio",Toast.LENGTH_SHORT).show();
            et_nombre.requestFocus();
        }else if(stock.isEmpty())    {
            Toast.makeText(this,"Campo stock obligatorio",Toast.LENGTH_SHORT).show();
            et_stock.requestFocus();
        }else if(precio.isEmpty()) {
            Toast.makeText(this,"Campo precio obligatorio",Toast.LENGTH_SHORT).show();
            et_precio.requestFocus();
        }else if(categoria.isEmpty()) {
            Toast.makeText(this, "Campo categoría obligatorio", Toast.LENGTH_SHORT).show();
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

    public void SelecFoto(View view){
        CropImage.startPickImageActivity(GestionarProducto.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        objFoto = new Foto(this,this,requestCode,resultCode,data,subir,"Img_Productos");
        objFoto.generadorDeFoto();
        if(objFoto.color==true) {
            subir.setBackgroundColor(getResources().getColor(R.color.teal_200));
        }
        downloadurl = objFoto.getDownloadurl();
    }
}