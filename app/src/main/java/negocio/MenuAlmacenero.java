package negocio;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smcontrol.CategoriaActivity;
import com.example.smcontrol.ProductoActivity;
import com.example.smcontrol.ProveedorActivity;
import com.example.smcontrol.R;
import com.example.smcontrol.ReporteActivity;
import com.example.smcontrol.TrabajadorActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
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

public class MenuAlmacenero extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {


    public CardView cardEntradas, cardSalidas, cardAlmacen,  cardReporte;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    FirebaseAuth mAuth;
    //
    View header;
    TextView correoTrabajador,nombreTrabajador;
    Alerta alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_almacenero);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        //
        header = navigationView.getHeaderView(0);
        correoTrabajador = (TextView) header.findViewById(R.id.tv_email);
        correoTrabajador.setText(Static.correo);
        nombreTrabajador = (TextView) header.findViewById(R.id.tv_nombre);
        nombreTrabajador.setText(Static.nombre);
        //
        toolbar = findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toogle);
        toogle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance();

        cardEntradas= (CardView) findViewById(R.id.cardEntradas);
        cardSalidas= (CardView) findViewById(R.id.cardSalidas);
        cardReporte= (CardView) findViewById(R.id.cardReporteAlmacenero);
        cardAlmacen= (CardView) findViewById(R.id.cardAlmacen);

        cardEntradas.setOnClickListener(this);
        cardSalidas.setOnClickListener(this);
        cardReporte.setOnClickListener(this);
        cardAlmacen.setOnClickListener(this);
        cargarAlerta();

        //

    }


    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()){
            case R.id.cardEntradas:
                alert.ListadeProductos();
                alert.BusquedaStock();
                AlertaStock();
                break;
            case R.id.cardSalidas:
                i = new Intent(this, negocio.SalidasActivity.class);
                startActivity(i);
                break;
            case R.id.cardAlmacen:
                i = new Intent(this, negocio.AlmacenActivity.class);
                startActivity(i);
                break;
            case R.id.cardReporteAlmacenero:
                i = new Intent(this, negocio.ReporteAlmacenActivity.class);
                startActivity(i);
                break;
        }
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
        Static.OpcionesNavAlmacenero(item,this);
        return true;
    }

    private void cargarAlerta() {
    alert=new Alerta(this,this);
    alert.inicializarFirebase();

    }

    private  void AlertaStock(){
        String alerta;
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        LayoutInflater inflater= getLayoutInflater();
        View view= inflater.inflate(R.layout.alerta_stock,null);

        builder.setView(view);
        AlertDialog dialog= builder.create();

        if(alert.BusquedaStock().equals("true"))   {
            dialog.show();
        }else   {
            Toast.makeText(this,"Todo bien",Toast.LENGTH_SHORT).show();
        }

        TextView txt=view.findViewById(R.id.text_dialog);
        txt.setText("Nos estamos quedando sin stock");

        Button btnCancelar= view.findViewById(R.id.btncancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button btnAceptar= view.findViewById(R.id.btnaceptar);
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(getApplicationContext(),negocio.AlmacenActivity.class);
                startActivity(it);
                finish();
            }
        });

    }


}