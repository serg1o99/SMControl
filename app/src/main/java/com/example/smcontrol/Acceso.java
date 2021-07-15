package com.example.smcontrol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Seguridad.Alerta;
import model.Producto;
import model.Static;

public class Acceso extends AppCompatActivity {
    private EditText txtemail;
    private EditText txtpass;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    String email,pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceso);
        txtemail    = (EditText) findViewById(R.id.et_correo);
        txtpass     = (EditText) findViewById(R.id.et_pass);
        mAuth       = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void validacion(View view)   {
        email = txtemail.getText().toString();
        pass  =  txtpass.getText().toString();
        if(email.isEmpty() && pass.isEmpty())  {
            Toast.makeText(getApplicationContext(),"Algún campo está vacío",Toast.LENGTH_SHORT).show();
        }else if(email.isEmpty()) {
            Toast.makeText(getApplicationContext(),"Introduzca su email",Toast.LENGTH_SHORT).show();
            txtemail.requestFocus();
        }else if(pass.isEmpty())    {
            Toast.makeText(getApplicationContext(),"Introduzca su contraseña",Toast.LENGTH_SHORT).show();
            txtpass.requestFocus();
        }else {
            login();
        }
    }

    private void login(){
        mAuth.signInWithEmailAndPassword(txtemail.getText().toString().trim(),txtpass.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String id = mAuth.getCurrentUser().getUid();
                    databaseReference.child("Trabajador").child(id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String nombre = snapshot.child("nombre").getValue().toString();
                            String correo = snapshot.child("correo").getValue().toString();
                            String rol = snapshot.child("rol").getValue().toString();
                            String dni = snapshot.child("dni").getValue().toString();
                            Static.nombre = nombre;
                            Static.correo = correo;
                            Static.dni    = dni;
                            if(rol.equals("Administrador")){
                                startActivity( new Intent(getApplicationContext(),MenuActivity.class));
                                finish();
                            }else{
                                Intent it=new Intent(getApplicationContext(),negocio.MenuAlmacenero.class);
                                startActivity(it);
                                finish();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) { }
                    });
                    startActivity( new Intent(getApplicationContext(),MenuActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Email y/o Contraseña incorrecto .",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed(){}
}