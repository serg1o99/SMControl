package negocio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;

import model.Entradas;
import model.Producto;
import model.Static;

public class GestionarEntradas extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private EditText et_codigo,et_nombre,et_stock,et_codentrada,et_dni,et_cantidadentrante,et_fechaentrada;
    public AutoCompleteTextView atv_proveedor;
    //
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    //
    View header;
    TextView correoTrabajador,nombreTrabajador;
    //
    public String dni,codigo,nombre,precio,categoria,proveedor,codentrada,fechaentrada,horaentrada,stock,cantidad,url;
    public int nuevostock;
    //
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestionar_entradas);

        //
        drawerLayout = findViewById(R.id.Gestionar_entrada);
        navigationView = findViewById(R.id.nav_view_gentr);
        //
        header = navigationView.getHeaderView(0);
        correoTrabajador = (TextView) header.findViewById(R.id.tv_email);
        correoTrabajador.setText(Static.correo);
        nombreTrabajador = (TextView) header.findViewById(R.id.tv_nombre);
        nombreTrabajador.setText(Static.nombre);
        //
        toolbar = findViewById(R.id.toolbar_gentra);

        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toogle);
        toogle.syncState();
        //
        navigationView.setNavigationItemSelectedListener(this);
        //
        et_codigo     =  (EditText) findViewById(R.id.txt_code);
        et_nombre     =  (EditText) findViewById(R.id.txt_nombre_prod_entra);
        et_stock      =  (EditText) findViewById(R.id.txt_stock_prod_entra);
        atv_proveedor  =  (AutoCompleteTextView) findViewById(R.id.txt_prov);
        et_fechaentrada =  (EditText) findViewById(R.id.txt_fecha_entrada);
        et_codentrada  =  (EditText) findViewById(R.id.txt_cod_entrada);
        et_dni         = (EditText) findViewById(R.id.txt_nombre_empleado);
        et_cantidadentrante  =  (EditText) findViewById(R.id.txt_cantidad_entrante);
        inicializarFirebase();
        //
        listarProveedor();
        //
        CargarDatosActualizar();
        //fecha
        muestraFecha(et_fechaentrada);

    }

    private void listarProveedor() {
        final ArrayAdapter<String> autoComplete = new ArrayAdapter<>(this, R.layout.lista_items);

        databaseReference.child("Proveedor").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshotCat : snapshot.getChildren()){
                    String nombreProv = snapshotCat.child("empresa").getValue(String.class);
                    autoComplete.add(nombreProv);
                }
                autoComplete.add("Otros");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
        atv_proveedor.setAdapter(autoComplete);
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
        stock        = getIntent().getStringExtra("stock");
        dni          = Static.dni;
        precio       = getIntent().getStringExtra("precio");
        categoria    = getIntent().getStringExtra("categoria");
        url          = getIntent().getStringExtra("url");
        et_codigo.setText(codigo);
        et_nombre.setText(nombre);
        et_stock.setText(stock);
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

    public void procesarEntrada(View view)  {

    proveedor = atv_proveedor.getText().toString();
    codentrada = et_codentrada.getText().toString();
    cantidad = et_cantidadentrante.getText().toString();
    fechaentrada = et_fechaentrada.getText().toString();
    horaentrada = generarHora(horaentrada);
    if(codentrada.isEmpty() || cantidad.isEmpty() || fechaentrada.isEmpty() || proveedor.isEmpty()){
        validarCampos();
    }else   {
        entrada();
    }

    }

    public void entrada(){

        AlertDialog.Builder alerta=new AlertDialog.Builder(this,R.style.AppCompatAlertDialogStyle);
        alerta.setMessage("¿Está seguro de que quiere crear una nueva entrada ?").setTitle("Gestionar entrada").setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Boolean status=false;
                if(status==false)   {
                    Entradas objEntradas= new Entradas();
                    objEntradas.setCod_prod(codigo);
                    objEntradas.setNombre_prod(nombre);
                    objEntradas.setProveedor(proveedor);
                    objEntradas.setCod_entrada("E00"+codentrada);
                    objEntradas.setCantidad_entrante(Integer.parseInt(cantidad));
                    objEntradas.setFecha_ingreso(fechaentrada);
                    objEntradas.setDni(dni);
                    objEntradas.setHora_ingreso(horaentrada);
                    databaseReference.child("Entradas").child(objEntradas.getCod_entrada()).setValue(objEntradas);
                    status=true;
                }

                if(status==true)    {
                    actualizarProducto();
                    Toast.makeText(getApplicationContext(),"Entrada procesada con éxito",Toast.LENGTH_SHORT).show();
                    Intent it=new Intent(getApplicationContext(),EntradasActivity.class);
                    startActivity(it);
                    finish();
                }else   {
                    Toast.makeText(getApplicationContext(),"Ocurrió un error al procesar",Toast.LENGTH_SHORT).show();
                }
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    public int aumentarStock(int stock) {
    nuevostock=stock+Integer.parseInt(cantidad);
    return nuevostock;
    }

    public void actualizarProducto()    {
        Producto obj = new Producto();
        obj.setCodigo(codigo);
        obj.setNombre(nombre);
        obj.setStock(""+aumentarStock(Integer.parseInt(stock)));
        obj.setPrecio(precio);
        obj.setCategoria(categoria);
        obj.setUrl(url);
        databaseReference.child("Producto").child(""+obj.getCodigo()).setValue(obj);
    }

    public void validarCampos() {
        if(codentrada.isEmpty()) {
            Toast.makeText(this,"Campo código entrada obligatorio",Toast.LENGTH_SHORT).show();
            et_codentrada.requestFocus();
        }else if(cantidad.isEmpty()) {
            Toast.makeText(this,"Campo cantidad obligatorio",Toast.LENGTH_SHORT).show();
            et_cantidadentrante.requestFocus();
        }else if(fechaentrada.isEmpty())    {
            Toast.makeText(this,"Campo fecha obligatorio",Toast.LENGTH_SHORT).show();
            et_fechaentrada.requestFocus();
        }else if(proveedor.isEmpty()) {
            Toast.makeText(this,"Campo proveedor obligatorio",Toast.LENGTH_SHORT).show();
            atv_proveedor.requestFocus();
        }
    }

//para la fecha y hora

    public void muestraFecha(EditText fecha){
        final Calendar date=Calendar.getInstance();
        date.get(Calendar.YEAR);
        date.get(Calendar.MONTH);
        date.get(Calendar.DAY_OF_MONTH);


        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy");
        fecha.setText(simpleDateFormat.format(date.getTime()));
    }

    public String generarHora(String hora){
        final Calendar hour=Calendar.getInstance();
        hour.get(Calendar.HOUR_OF_DAY);
        hour.get(Calendar.MINUTE);
        SimpleDateFormat simpleHourFormat=new SimpleDateFormat("HH:mm");
        hora=simpleHourFormat.format(hour.getTime());
        return hora;
    }


}