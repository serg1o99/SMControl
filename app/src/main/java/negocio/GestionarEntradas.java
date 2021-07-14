package negocio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smcontrol.R;
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

import model.Entradas;
import model.Producto;
import model.Static;

public class GestionarEntradas extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private EditText et_codigo,et_nombre,et_stock,et_dni,et_codentrada,et_cantidadentrante,et_fechaentrada,et_proveedor;
    //
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    //
    View header;
    TextView correoTrabajador,nombreTrabajador;
    //
    public String dni,codigo,nombre,precio,categoria,proveedor,codentrada,fechaentrada;
    public int stock,cantidadentrante,nuevostock;
    //
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestionar_entradas);

        //
        drawerLayout = findViewById(R.id.Actualizar_producto);
        navigationView = findViewById(R.id.nav_view_prod);
        //
        header = navigationView.getHeaderView(0);
        correoTrabajador = (TextView) header.findViewById(R.id.tv_email);
        correoTrabajador.setText(Static.correo);
        nombreTrabajador = (TextView) header.findViewById(R.id.tv_nombre);
        nombreTrabajador.setText(Static.nombre);
        //
        toolbar = findViewById(R.id.toolbar_prod);

        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toogle);
        toogle.syncState();
        //
        navigationView.setNavigationItemSelectedListener(this);
        //
        et_codigo     =  (EditText) findViewById(R.id.txt_codigo);
        et_nombre     =  (EditText) findViewById(R.id.txt_nombre);
        et_stock      =  (EditText) findViewById(R.id.txt_stock);
        et_dni        =  (EditText) findViewById(R.id.txt_dni);
        et_proveedor  =  (EditText) findViewById(R.id.txt_proveedor);
        et_fechaentrada =  (EditText) findViewById(R.id.txt_fec_entrada);
        et_codentrada  =  (EditText) findViewById(R.id.txt_cod_entrada);
        et_cantidadentrante  =  (EditText) findViewById(R.id.txt_cantidad);
        inicializarFirebase();
        //
        CargarDatosActualizar();

    }

    private void inicializarFirebase(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase=FirebaseDatabase.getInstance().getInstance();
        databaseReference=firebaseDatabase.getReference();
        mAuth =  FirebaseAuth.getInstance();
    }

    public void CargarDatosActualizar()     {

        codigo       = getIntent().getStringExtra("codigo");
        nombre       = getIntent().getStringExtra("nombre");
        stock        = Integer.parseInt(getIntent().getStringExtra("stock"));
        dni          = Static.dni;
        precio       = getIntent().getStringExtra("precio");
        categoria    = getIntent().getStringExtra("categoria");
        et_codigo.setText(codigo);
        et_nombre.setText(nombre);
        et_stock.setText(""+stock);
        et_dni.setText(Static.nombre);
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

    public void ProcesarEntrada(View view)  {
    Boolean status=false;
    proveedor = et_proveedor.getText().toString();
    codentrada = et_codentrada.getText().toString();
    cantidadentrante = Integer.parseInt(et_cantidadentrante.getText().toString());
    fechaentrada = et_fechaentrada.getText().toString();

    if(status==false)   {
        Entradas objEntradas= new Entradas();
        objEntradas.setCod_prod(codigo);
        objEntradas.setNombre_prod(nombre);
        objEntradas.setProveedor(proveedor);
        objEntradas.setCod_entrada(codentrada);
        objEntradas.setCantidad_entrante(cantidadentrante);
        objEntradas.setFecha_ingreso(fechaentrada);
        objEntradas.setDni(dni);
        databaseReference.child("Entradas").child(objEntradas.getCod_entrada()).setValue(objEntradas);
        actualizarProducto();
        status=true;
    }

    if(status==true)    {
        Toast.makeText(this,"Entrada procesada con éxito",Toast.LENGTH_SHORT).show();
        Intent it=new Intent(this,EntradasActivity.class);
        startActivity(it);
        finish();
    }

    }

    public int aumentarStock(int stock) {
    nuevostock=stock+cantidadentrante;
    return nuevostock;
    }

    public void actualizarProducto()    {
        Producto obj = new Producto();
        obj.setCodigo(codigo);
        obj.setNombre(nombre);
        obj.setStock(""+aumentarStock(stock));
        obj.setPrecio(precio);
        obj.setCategoria(categoria);
        databaseReference.child("Producto").child(""+obj.getCodigo()).setValue(obj);
    }


}