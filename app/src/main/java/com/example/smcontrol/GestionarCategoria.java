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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import model.Categoria;
import model.Foto;
import model.Proveedor;
import model.Static;

public class GestionarCategoria extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private EditText et_codigo,et_nombre,et_descripcion;
    //
    private StorageReference storageReference;
    private ProgressDialog progressDialog;
    //
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    //
    public String codigo,nombre,descripcion;
    //
    FirebaseAuth mAuth;
    //
    View header;
    TextView correoTrabajador,nombreTrabajador;

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
        setContentView(R.layout.activity_gestionar_categoria);
        //
        drawerLayout = findViewById(R.id.gestion_categoria);
        navigationView = findViewById(R.id.nav_view_cat);
        //
        header = navigationView.getHeaderView(0);
        correoTrabajador = (TextView) header.findViewById(R.id.tv_email);
        correoTrabajador.setText(Static.correo);
        nombreTrabajador = (TextView) header.findViewById(R.id.tv_nombre);
        nombreTrabajador.setText(Static.nombre);
        //
        toolbar = findViewById(R.id.toolbar_cat);
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
        et_codigo        =  (EditText) findViewById(R.id.txt_cod_cat);
        et_nombre        =  (EditText) findViewById(R.id.txt_nom_cat);
        et_descripcion   =  (EditText) findViewById(R.id.txt_descripcion);
        inicializarFirebase();
    }

    private void inicializarFirebase(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase=FirebaseDatabase.getInstance().getInstance();
        databaseReference=firebaseDatabase.getReference();
    }

    public void crearCategoria(View view) {

        codigo      = et_codigo.getText().toString();
        nombre      = et_nombre.getText().toString();
        descripcion = et_descripcion.getText().toString();
        if( this.codigo.equals("") || nombre.equals("") || descripcion.equals("")){
            validarCampos();
        }else if (!subir.isEnabled())   {
            Toast.makeText(this,"Debe subir una foto",Toast.LENGTH_SHORT).show();
        }else   {
            insertar();
        }
    }

    public void insertar()  {
        AlertDialog.Builder alerta=new AlertDialog.Builder(this,R.style.AppCompatAlertDialogStyle);
        alerta.setMessage("¿Está seguro de que quiere crear una nueva Categoria ?").setTitle("Registrar").setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Categoria obj = new Categoria();
                downloadurl=objFoto.getDownloadurl();
                obj.setCodigo("CT0"+codigo);
                obj.setNombre(nombre);
                obj.setDescripcion(descripcion);
                obj.setUrl(""+downloadurl);
                databaseReference.child("Categoria").child(""+obj.getCodigo()).setValue(obj);
                Toast.makeText(getApplicationContext(),"Categoria agregada",Toast.LENGTH_SHORT).show();
                limpiarCampos();
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
            Toast.makeText(this,"Campo código obligatorio",Toast.LENGTH_SHORT).show();
            et_codigo.requestFocus();
        }else if(nombre.isEmpty()) {
            Toast.makeText(this,"Campo nombre obligatorio",Toast.LENGTH_SHORT).show();
            et_nombre.requestFocus();
        }else if(descripcion.isEmpty())    {
            Toast.makeText(this,"Campo descripción obligatorio",Toast.LENGTH_SHORT).show();
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
        Static.OpcionesNav(item,this);
        return true;
    }

    public void SelecFoto(View view){
        CropImage.startPickImageActivity(GestionarCategoria.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        objFoto = new Foto(this,this,requestCode,resultCode,data,subir,"Img_Categoría");
        objFoto.generadorDeFoto();
        if(objFoto.color==true) {
            subir.setBackgroundColor(getResources().getColor(R.color.teal_200));
        }
        downloadurl = objFoto.getDownloadurl();
    }


}