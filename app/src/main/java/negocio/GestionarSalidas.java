package negocio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smcontrol.R;
import com.example.smcontrol.TrabajadorActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import model.Producto;
import model.Salida;
import model.Static;

public class GestionarSalidas extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private EditText et_nombre_empleado,et_codigo_salida,et_codigo_producto,et_nombre_producto,et_stock,et_cantidad,et_fecha,et_descripcion;
    //
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    //
    public String nombre_empleado,codigo_salida,codigo_producto,nombre_producto,stock_producto,cantidad,fecha,descripcion,precio_producto,categoria_producto;
    int cant,stock_prod;
    //
    FirebaseAuth mAuth;
    //
    View header;
    TextView correoTrabajador,nombreTrabajador;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestionar_salidas);
        //
        drawerLayout = findViewById(R.id.Gestionar_salida);
        navigationView = findViewById(R.id.nav_view_gsal);
        //
        header = navigationView.getHeaderView(0);
        correoTrabajador = (TextView) header.findViewById(R.id.tv_email);
        correoTrabajador.setText(Static.correo);
        nombreTrabajador = (TextView) header.findViewById(R.id.tv_nombre);
        nombreTrabajador.setText(Static.nombre);
        //
        toolbar = findViewById(R.id.toolbar_gsal);

        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toogle);
        toogle.syncState();
        //
        navigationView.setNavigationItemSelectedListener(this);
        //
        et_nombre_empleado = (EditText) findViewById(R.id.txt_cod_empleado);
        et_codigo_salida   = (EditText) findViewById(R.id.txt_cod_salida);
        et_codigo_producto = (EditText) findViewById(R.id.txt_cod_prod);
        et_nombre_producto = (EditText) findViewById(R.id.txt_nom_prod);
        et_stock           = (EditText) findViewById(R.id.txt_stocks);
        et_cantidad        = (EditText) findViewById(R.id.txt_cantidad_salidas);
        et_fecha           = (EditText) findViewById(R.id.txt_fecha_salida);
        et_descripcion     = (EditText) findViewById(R.id.txt_descripcion_salida);
        inicializarFirebase();
        cargarDatos();
    }

    private void inicializarFirebase(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase=FirebaseDatabase.getInstance().getInstance();
        databaseReference=firebaseDatabase.getReference();
    }

    public void cargarDatos(){
        codigo_producto    = getIntent().getStringExtra("codigo");
        nombre_producto    = getIntent().getStringExtra("nombre");
        stock_producto     = getIntent().getStringExtra("stock");
        precio_producto    = getIntent().getStringExtra("precio");
        categoria_producto = getIntent().getStringExtra("categoria");

        et_nombre_empleado.setText(Static.nombre);
        et_codigo_producto.setText(codigo_producto);
        et_nombre_producto.setText(nombre_producto);
        et_stock.setText(stock_producto);
    }

    public void procesarSalida(View view) {
        nombre_empleado = et_nombre_empleado.getText().toString();
        codigo_salida   = et_codigo_salida.getText().toString();
        codigo_producto = et_codigo_producto.getText().toString();
        nombre_producto = et_nombre_producto.getText().toString();
        stock_producto  = et_stock.getText().toString();
        cantidad        = et_cantidad.getText().toString();
        fecha           = et_fecha.getText().toString();
        descripcion     = et_descripcion.getText().toString();

        if(codigo_salida.isEmpty() || cantidad.isEmpty() || fecha.isEmpty() || descripcion.isEmpty()){
            validarCampos();
        }else if(Character.isDigit(cantidad.charAt(0))){
            cant = Integer.parseInt(cantidad);
            stock_prod = Integer.parseInt(stock_producto);
            if (cant > stock_prod) {
                Toast.makeText(getApplicationContext(), "La cantidad ingresada es mayor al stock disponible", Toast.LENGTH_SHORT).show();
            } else {
                salida();
            }
        }
    }

    public void salida(){
        AlertDialog.Builder alerta=new AlertDialog.Builder(this,R.style.AppCompatAlertDialogStyle);
        alerta.setMessage("¿Está seguro de que quiere crear una nueva salida ?").setTitle("Gestionar salida").setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Salida obj = new Salida();
                obj.setDni(Static.dni);
                obj.setCod_salida(codigo_salida);
                obj.setNombre_prod(nombre_producto);
                obj.setFecha_salida(fecha);
                obj.setDescripcion(descripcion);
                obj.setCantidad_salida(Integer.parseInt(cantidad));
                databaseReference.child("Salidas").child(obj.getCod_salida()).setValue(obj);
                actualizarProducto();
                Toast.makeText(getApplicationContext(),"Salida Procesada",Toast.LENGTH_SHORT).show();
                Intent i =new Intent(getApplicationContext(), SalidasActivity.class);
                startActivity(i);
                finish();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    public void actualizarProducto()    {
        int nuevoStock = stock_prod - cant;
        Producto obj = new Producto();
        obj.setCodigo(codigo_producto);
        obj.setNombre(nombre_producto);
        obj.setStock(""+nuevoStock);
        obj.setPrecio(precio_producto);
        obj.setCategoria(categoria_producto);
        databaseReference.child("Producto").child(""+obj.getCodigo()).setValue(obj);
    }

    public void validarCampos() {
        if(codigo_salida.isEmpty()) {
            Toast.makeText(this,"Campo codigo salida obligatorio",Toast.LENGTH_SHORT).show();
            et_codigo_salida.requestFocus();
        }else if(cantidad.isEmpty()) {
            Toast.makeText(this,"Campo cantidad obligatorio",Toast.LENGTH_SHORT).show();
            et_cantidad.requestFocus();
        }else if(fecha.isEmpty())    {
            Toast.makeText(this,"Campo fecha obligatorio",Toast.LENGTH_SHORT).show();
            et_fecha.requestFocus();
        }else if(descripcion.isEmpty()) {
            Toast.makeText(this,"Campo descripcion obligatorio",Toast.LENGTH_SHORT).show();
            et_descripcion.requestFocus();
        }else if(cant>stock_prod){
            Toast.makeText(this,"Cantida mayor al stock disponible",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Static.OpcionesNav(item,this);
        return true;
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