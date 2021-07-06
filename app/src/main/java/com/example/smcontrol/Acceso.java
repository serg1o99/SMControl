package com.example.smcontrol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
        txtemail=(EditText) findViewById(R.id.txt_apellido);
        txtpass=(EditText) findViewById(R.id.et_correo);
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onStart() {
        super.onStart();
        //Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    public void autentificarusuario(View view)   {
        email=txtemail.getText().toString();
        pass=txtpass.getText().toString();
        if(email.isEmpty() && pass.isEmpty())  {
            Toast.makeText(getApplicationContext(),"Algún campo está vacío",Toast.LENGTH_SHORT).show();
        }else if(email.isEmpty()) {
            Toast.makeText(getApplicationContext(),"Introduzca su email",Toast.LENGTH_SHORT).show();
            txtemail.requestFocus();
        }else if(pass.isEmpty())    {
            Toast.makeText(getApplicationContext(),"Introduzca su contraseña",Toast.LENGTH_SHORT).show();
            txtpass.requestFocus();
        }else {
            //---AUTENTICACIÓN---//
            mAuth.signInWithEmailAndPassword(txtemail.getText().toString().trim(),txtpass.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        //updateUI(user);
                        Intent it=new Intent(getApplicationContext(), MenuActivity.class);
                        it.putExtra("email",email);
                        startActivity(it);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Email y/o Contraseña incorrecto .",Toast.LENGTH_SHORT).show();
                        // updateUI(null);
                    }
                    // ...
                }
            });

            //---LOGIN----//

        }
    }

    public void IniciarSesion(View view) {
        databaseReference=FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Trabajador").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String email=txtemail.getText().toString();
                String pass=txtpass.getText().toString();
            if(snapshot.child(email).exists())  {
                if (snapshot.child(pass).exists()) {
                if(snapshot.child("rol").equals("Administrador"))   {

                }else   {

                }

                }else   {

                }

            }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed(){}

}