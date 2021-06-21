package com.example.smcontrol;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;



public class TrabajadorActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trabajador);


    }

    public void pasar(View view)    {
     Intent it=new Intent(this,Gestionar_TrabajadorActivity.class);
     startActivity(it);
    }
}