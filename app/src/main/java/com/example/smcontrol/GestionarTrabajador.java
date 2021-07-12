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
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

import Security.Encript;
import id.zelory.compressor.Compressor;
import model.Static;
import model.Trabajador;

public class GestionarTrabajador extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //Atributos
    private EditText et_dni,et_nombre,et_apellido,et_ncorreo,et_npass;
    private AutoCompleteTextView atv_rol;
    private static  final  int GALLERY_INTENT=1;
    //para la imagen
    private StorageReference storageReference;
    private ProgressDialog cargando;
    private Button subir;
    Bitmap thumb_bitmap = null;
    private  Uri downloadurl;
    //FireBase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    //Variables globales
    public String nombre,apellido,correo,contraseña,rol,dni;
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
        subir=(Button) findViewById(R.id.btnsubirfoto);
        storageReference = FirebaseStorage.getInstance().getReference().child("Img_Trabajador");
        cargando = new ProgressDialog(this,R.style.AppCompatAlertDialogStyle);
        //
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

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageuri = CropImage.getPickImageResultUri(this, data);
            //recortar imagen
            CropImage.activity(imageuri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(GestionarTrabajador.this);
        }
       if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)    {
         CropImage.ActivityResult result= CropImage.getActivityResult(data);
         if(resultCode == RESULT_OK)    {
          Uri resultUri = result.getUri();
          File url= new File(resultUri.getPath());
          //comprimiendo imagen
          try {
              thumb_bitmap = new Compressor(this)
                      .setMaxWidth(640)
                      .setMaxHeight(480)
                      .setQuality(90)
                      .compressToBitmap(url);
          }catch (IOException e)    {
              e.printStackTrace();
          }
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             thumb_bitmap.compress(Bitmap.CompressFormat.JPEG,90,byteArrayOutputStream);
             final byte [] thumb_byte = byteArrayOutputStream.toByteArray();

             // fin del compresor....
             //codigo autogenerado

            subir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cargando.setTitle("Subiendo foto...");
                    cargando.setMessage("Espere por favor...");
                    cargando.show();
                    StorageReference ref= storageReference.child(autogenerarcodigofoto());
                    UploadTask uploadTask = ref.putBytes(thumb_byte);
                    //subir al storage
                    Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                          if(!task.isSuccessful())  {
                          throw Objects.requireNonNull(task.getException());

                          }
                            return ref.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            downloadurl = task.getResult();
                           cargando.dismiss();
                            Toast.makeText(GestionarTrabajador.this,"Imagen cargada con éxito",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

         }
       }
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
        }else if(!Patterns.EMAIL_ADDRESS.matcher(correo).matches())  {
            Toast.makeText(this,"El correo es inválido",Toast.LENGTH_SHORT).show();
            et_ncorreo.requestFocus();

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
                    Trabajador obj =new Trabajador();
                    //Encript ec =new Encript();
                    obj.setDni(dni);
                    obj.setNombre(nombre);
                    obj.setApellido(apellido);
                    obj.setCorreo(correo);
                    obj.setContraseña(contraseña);
                    obj.setRol(rol);
                    obj.setUrl(""+downloadurl);
                    databaseReference.child("Trabajador").child(cod).setValue(obj);
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

    public void SubirFoto(View view)     {
        CropImage.startPickImageActivity(GestionarTrabajador.this);
    }


    public String autogenerarcodigofoto() {
    int p = (int) (Math.random() * 25 + 1); int s = (int) (Math.random() * 25 + 1);
    int t = (int) (Math.random() * 25 + 1); int c = (int) (Math.random() * 25 + 1);
    int numero1= (int) (Math.random() * 1012 + 2111);
    int numero2= (int) (Math.random() * 1012 + 2111);
    String[] elementos= {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
    final String aleatorio = elementos[p] + elementos[s] + numero1 + elementos[t] + elementos[c] + numero2 + "trabajador.jpg";
    return aleatorio;
    }


}