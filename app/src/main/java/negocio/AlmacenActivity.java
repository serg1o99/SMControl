package negocio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.smcontrol.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import model.Producto;
import model.ProductoAdapter;
import model.Static;

public class AlmacenActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    RecyclerView recyclerView;
    ArrayList<Producto> listaProducto;
    ProductoAdapter productoAdapter;
    //
    DatabaseReference database;
    //
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    //
    FloatingActionButton fab;
    //
    Producto p;
    //
    FirebaseAuth mAuth;
    //
    View header;
    TextView correoTrabajador,nombreTrabajador,txtmostral;
    //variables globales
    public String codigo,nombre,stock,precio,categoria,url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_almacen);

        //
        drawerLayout = findViewById(R.id.Almacen_layout);
        navigationView = findViewById(R.id.nav_view_almacen);
        //
        header = navigationView.getHeaderView(0);
        correoTrabajador = (TextView) header.findViewById(R.id.tv_email);
        correoTrabajador.setText(Static.correo);
        nombreTrabajador = (TextView) header.findViewById(R.id.tv_nombre);
        nombreTrabajador.setText(Static.nombre);
        //
        toolbar = findViewById(R.id.toolbar_almacen);
        //floating button
        fab=findViewById(R.id.fab);
        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toogle);
        toogle.syncState();
        //
        navigationView.setNavigationItemSelectedListener(this);
        recyclerView = findViewById(R.id.recyclerView_almacen);
        txtmostral= findViewById(R.id.txt_mostral);
        database = FirebaseDatabase.getInstance().getReference("Producto");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //
        listaProducto   = new ArrayList<>();
        productoAdapter = new ProductoAdapter(AlmacenActivity.this,listaProducto);
        recyclerView.setAdapter(productoAdapter);

        OnClickRecyclerViewListener();
        FirebaseEventListener();
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    public void OnClickRecyclerViewListener(){

        listaProducto = new ArrayList<>();
        productoAdapter = new ProductoAdapter(AlmacenActivity.this,listaProducto);
        productoAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                codigo = listaProducto.get(recyclerView.getChildAdapterPosition(v)).getCodigo();
                nombre = listaProducto.get(recyclerView.getChildAdapterPosition(v)).getNombre();
                stock = listaProducto.get(recyclerView.getChildAdapterPosition(v)).getStock();
                precio = listaProducto.get(recyclerView.getChildAdapterPosition(v)).getPrecio();
                categoria = listaProducto.get(recyclerView.getChildAdapterPosition(v)).getCategoria();
                url       = listaProducto.get(recyclerView.getChildAdapterPosition(v)).getUrl();
            }
        });
        recyclerView.setAdapter(productoAdapter);
    }


    public void  FirebaseEventListener(){
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot){
                listaProducto.clear();
                int i=0;
                boolean status=false;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Producto p = dataSnapshot.getValue(Producto.class);
                    if(Integer.parseInt(p.getStock())<1){
                     listaProducto.add(p);
                     status=true;
                    }
                }
                productoAdapter.notifyDataSetChanged();
                txtmostral.setText(status ? "" : "No hay productos por agotarse");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Static.OpcionesNavAlmacenero(item,this);
        return true;
    }

}