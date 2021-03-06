package com.example.smcontrol;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.theartofdev.edmodo.cropper.CropImage;

import model.Foto;
import model.Static;
import model.Trabajador;

public class GestionarTrabajador extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //Atributos
    private EditText et_dni,et_nombre,et_apellido,et_ncorreo,et_npass;
    private AutoCompleteTextView atv_rol;

    //para la imagen
    private Button subir;
    private  Uri downloadurl;
    Foto objFoto;
    //FireBase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    //Variables globales
    public String uid,nombre,apellido,correo,contraseña,rol,dni;
    //
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    //
    View header;
    TextView correoTrabajador,nombreTrabajador;

    @RequiresApi(api= Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestionar__trabajador);
        //
        drawerLayout = findViewById(R.id.gestion_trabajador);
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
        //para subir imagen
        subir = (Button) findViewById(R.id.btnsubirfoto);
        //
        navigationView.setNavigationItemSelectedListener(this);
        //
        //enlazamos los atributos con los componentes
        et_dni=(EditText) findViewById(R.id.txt_dni);
        et_nombre=(EditText) findViewById(R.id.txt_nombre_prod_entra);
        et_apellido=(EditText) findViewById(R.id.txt_apellido);
        et_ncorreo=(EditText) findViewById(R.id.txt_ncorreo);
        et_npass=(EditText) findViewById(R.id.txt_npass);
        atv_rol=findViewById(R.id.txt_roles);
        inicializarFirebase();

        ArrayAdapter arrayAdapter=new ArrayAdapter(this,R.layout.lista_items,getResources().getStringArray(R.array.roles));
        atv_rol.setAdapter(arrayAdapter);


    }

    private void inicializarFirebase(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase=FirebaseDatabase.getInstance().getInstance();
        databaseReference=firebaseDatabase.getReference();
        mAuth =  FirebaseAuth.getInstance();
    }

    public void crearTrabajador(View view) {
        //declarando variables locales y asignandolos a los atributos que contienen los componentes
        dni         = et_dni.getText().toString();
        nombre      = et_nombre.getText().toString();
        apellido    = et_apellido.getText().toString();
        correo      = et_ncorreo.getText().toString();
        contraseña  = et_npass.getText().toString();
        rol         = atv_rol.getText().toString();

        if( this.dni.equals("") || nombre.equals("") || apellido.equals("") || correo.equals("") || contraseña.equals("") || rol.equals("Escoja el rol") || rol.equals("") )     {
           validarCampos();
        }else if(dni.length()<8)    {
            Toast.makeText(this,"El dni debe tener 8 dígitos",Toast.LENGTH_SHORT).show();
            et_dni.requestFocus();
        }else if(!Patterns.EMAIL_ADDRESS.matcher(correo).matches())  {
            Toast.makeText(this,"El correo es inválido",Toast.LENGTH_SHORT).show();
            et_ncorreo.requestFocus();

        } else if(contraseña.length()<6) {
            Toast.makeText(this,"La contraseña debe tener 6 a más dígitos ",Toast.LENGTH_SHORT).show();
            et_npass.requestFocus();
        }else if (!subir.isEnabled())   {
            Toast.makeText(this,"Debe subir una foto",Toast.LENGTH_SHORT).show();
        }else   {
         insertar();
        }
    }

    public void insertar()  {
        AlertDialog.Builder alerta=new AlertDialog.Builder(this,R.style.AppCompatAlertDialogStyle);
        alerta.setMessage("¿Está seguro de que quiere crear un nuevo trabajador ?").setTitle("Registrar").setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Registrar();
                Toast.makeText(getApplicationContext(),"Trabajador Agregado",Toast.LENGTH_SHORT).show();
                limpiarCampos();
                Intent intent =new Intent(getApplicationContext(),TrabajadorActivity.class);
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

    private void Registrar(){
        mAuth.createUserWithEmailAndPassword(correo,contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String cod = mAuth.getCurrentUser().getUid();
                    uid=cod;
                    Trabajador obj =new Trabajador();
                    downloadurl=objFoto.getDownloadurl();
                    //Encript ec =new Encript();
                    obj.setUid(uid);
                    obj.setDni(dni);
                    obj.setNombre(nombre);
                    obj.setApellido(apellido);
                    obj.setCorreo(correo);
                    obj.setContraseña(contraseña);
                    obj.setRol(rol);
                    obj.setUrl(""+downloadurl);
                    databaseReference.child("Trabajador").child(uid).setValue(obj);
                }
            }
        });
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Static.OpcionesNav(item,this);
        return true;
    }


    public void SelecFoto(View view){
        CropImage.startPickImageActivity(GestionarTrabajador.this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        objFoto = new Foto(this,this,requestCode,resultCode,data,subir,"Img_Trabajador");
        objFoto.generadorDeFoto();
        if(objFoto.color==true) {
            subir.setBackgroundColor(getResources().getColor(R.color.teal_200));
        }
        downloadurl = objFoto.getDownloadurl();
    }



    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }
}