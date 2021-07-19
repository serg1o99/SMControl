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

import model.Foto;
import model.Proveedor;
import model.Static;
import model.Trabajador;

public class GestionarProveedor extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private EditText et_codigo,et_nombre,et_nombreEmpresa,et_correo,et_direccion,et_telefono;

    private StorageReference storageReference;
    private ProgressDialog progressDialog;
    //
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    //
    public String nombre,nombreEmpresa,correo,direccion,telefono,codigo,url;
    //
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    FirebaseAuth mAuth;
    //
    View header;
    TextView correoTrabajador,nombreTrabajador;
    //para la imagen
    private Button subir;
    private  Uri downloadurl;
    Foto objFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestionar_proveedor);
        //
        drawerLayout = findViewById(R.id.gestion_proveedor);
        navigationView = findViewById(R.id.nav_view_prov__);
        //
        header = navigationView.getHeaderView(0);
        correoTrabajador = (TextView) header.findViewById(R.id.tv_email);
        correoTrabajador.setText(Static.correo);
        nombreTrabajador = (TextView) header.findViewById(R.id.tv_nombre);
        nombreTrabajador.setText(Static.nombre);
        //
        toolbar = findViewById(R.id.toolbarprov_);
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
        et_codigo        =  (EditText) findViewById(R.id.txt_cod);
        et_nombre        =  (EditText) findViewById(R.id.txt_nom);
        et_correo        =  (EditText) findViewById(R.id.txt_correo_prov);
        et_direccion     =  (EditText) findViewById(R.id.txt_direccion);
        et_telefono      =  (EditText) findViewById(R.id.txt_telefono);
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
        nombreEmpresa = et_nombreEmpresa.getText().toString();

        if( this.codigo.equals("") || nombre.equals("") || correo.equals("") || direccion.equals("") || telefono.equals("") || nombreEmpresa.equals("")){
            validarCampos();
        }else if (!subir.isEnabled())   {
            Toast.makeText(this,"Debe subir una foto",Toast.LENGTH_SHORT).show();
        }else   {
            insertar();
        }
    }

    public void insertar()  {
        AlertDialog.Builder alerta=new AlertDialog.Builder(this,R.style.AppCompatAlertDialogStyle);
        alerta.setMessage("¿Está seguro de que quiere crear un nuevo Proveedor ?").setTitle("Registrar").setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Proveedor obj = new Proveedor();
                downloadurl=objFoto.getDownloadurl();
                obj.setCodigo("PV0"+codigo);
                obj.setNombre(nombre);
                obj.setCorreo(correo);
                obj.setDireccion(direccion);
                obj.setTelefono(telefono);
                obj.setEmpresa(nombreEmpresa);
                obj.setUrl(""+downloadurl);
                databaseReference.child("Proveedor").child(""+obj.getCodigo()).setValue(obj);
                Toast.makeText(getApplicationContext() ,"Proveedor Agregado",Toast.LENGTH_SHORT).show();
                limpiarCampos();
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

    public void validarCampos() {
        if(codigo.isEmpty()) {
            Toast.makeText(this,"Campo código obligatorio",Toast.LENGTH_SHORT).show();
            et_codigo.requestFocus();
        }else if(nombre.isEmpty()) {
            Toast.makeText(this,"Campo nombre obligatorio",Toast.LENGTH_SHORT).show();
            et_nombre.requestFocus();
        }else if(correo.isEmpty())    {
            Toast.makeText(this,"Campo correo obligatorio",Toast.LENGTH_SHORT).show();
            et_correo.requestFocus();
        }else if(direccion.isEmpty()) {
            Toast.makeText(this,"Campo dirección obligatorio",Toast.LENGTH_SHORT).show();
            et_direccion.requestFocus();
        }else if(telefono.isEmpty())  {
            Toast.makeText(this,"Campo teléfono obligatorio",Toast.LENGTH_SHORT).show();
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

    public void SelecFoto(View view){
        CropImage.startPickImageActivity(GestionarProveedor.this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        objFoto = new Foto(this,this,requestCode,resultCode,data,subir,"Img_Proveedor");
        objFoto.generadorDeFoto();
        if(objFoto.color==true) {
            subir.setBackgroundColor(getResources().getColor(R.color.teal_200));
        }
        downloadurl = objFoto.getDownloadurl();
    }

}