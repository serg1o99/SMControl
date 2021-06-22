package com.example.smcontrol;

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


import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import model.Trabajador;
import model.TrabajadorAdapter;


public class TrabajadorActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView recyclerView;
    ArrayList<Trabajador> listaTrabajador;
    TrabajadorAdapter trabajadorAdapter;
    //FirebaseFirestore db;
    DatabaseReference database;
    //
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trabajador);
        //
        drawerLayout = findViewById(R.id.trabajador);
        navigationView = findViewById(R.id.nav_view__);
        toolbar = findViewById(R.id.toolbar_);

        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toogle);
        toogle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        //

        recyclerView = findViewById(R.id.recyclerView);
        database = FirebaseDatabase.getInstance().getReference("Trabajador");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // db=FirebaseFirestore.getInstance();
        listaTrabajador=new ArrayList<>();
        trabajadorAdapter=new TrabajadorAdapter(TrabajadorActivity.this,listaTrabajador);
        recyclerView.setAdapter(trabajadorAdapter);

        //EventChangeListener();

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot){
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Trabajador t=dataSnapshot.getValue(Trabajador.class);
                    listaTrabajador.add(t);
                }
                trabajadorAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

    }

    /* FIRESTORE
    private void EventChangeListener() {
        db.collection("Trabajador").orderBy("dni", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
            if(error !=null)    {
                if(progressDialog.isShowing())
                    progressDialog.dismiss();
                Log.e("Firestore error",error.getMessage());
                return;
            }
            for (DocumentChange dc : value.getDocumentChanges())    {
            if(dc.getType() == DocumentChange.Type.ADDED)   {
            listaTrabajador.add(dc.getDocument().toObject(Trabajador.class));
            }

            trabajadorAdapter.notifyDataSetChanged();
                if(progressDialog.isShowing())
                    progressDialog.dismiss();
            }
            }
        });
    }
*/

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
        Intent i;
        switch (item.getItemId()){
            case R.id.nav_home:
                i = new Intent(this,MenuActivity.class);
                startActivity(i);
                break;
            case R.id.nav_trabajador:
                i = new Intent(this,TrabajadorActivity.class);
                startActivity(i);
                break;
            case R.id.nav_producto:
                i = new Intent(this, ProductoActivity.class);
                startActivity(i);
                break;
            case R.id.nav_categoria:
                i = new Intent(this, CategoriaActivity.class);
                startActivity(i);
                break;
            case R.id.nav_proveedor:
                i = new Intent(this, ProveedorActivity.class);
                startActivity(i);
                break;
            case R.id.nav_reporte:
                i = new Intent(this, ReporteActivity.class);
                startActivity(i);
                break;
        }
        return true;
    }
}