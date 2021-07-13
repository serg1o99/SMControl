package model;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import com.example.smcontrol.Acceso;
import com.example.smcontrol.CategoriaActivity;
import com.example.smcontrol.MenuActivity;
import com.example.smcontrol.ProductoActivity;
import com.example.smcontrol.ProveedorActivity;
import com.example.smcontrol.R;
import com.example.smcontrol.ReporteActivity;
import com.example.smcontrol.TrabajadorActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Static {

    public static String nombre;
    public static String correo;

    public static void OpcionesNav(MenuItem item, Context context){
        Intent i;
        switch (item.getItemId()){
            case R.id.nav_home:
                i = new Intent(context, MenuActivity.class);
                context.startActivity(i);
                break;
            case R.id.nav_trabajador:
                i = new Intent(context, TrabajadorActivity.class);
                context.startActivity(i);
                break;
            case R.id.nav_producto:
                i = new Intent(context, ProductoActivity.class);
                context.startActivity(i);
                break;
            case R.id.nav_categoria:
                i = new Intent(context, CategoriaActivity.class);
                context.startActivity(i);
                break;
            case R.id.nav_proveedor:
                i = new Intent(context, ProveedorActivity.class);
                context.startActivity(i);
                break;
            case R.id.nav_reporte:
                i = new Intent(context, ReporteActivity.class);
                context.startActivity(i);
                break;
            case R.id.nav_salir:
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                i = new Intent(context, Acceso.class);
                context.startActivity(i);
                break;
        }
    }

    public static void OpcionesNavAlmacenero(MenuItem item, Context context){
        Intent i;
        switch (item.getItemId()){
            case R.id.nav_home:
                i = new Intent(context, negocio.MenuAlmacenero.class);
                context.startActivity(i);
                break;
            case R.id.nav_Entradas:
                i = new Intent(context, negocio.EntradasActivity.class);
                context.startActivity(i);
                break;
            case R.id.nav_Salidas:
                i = new Intent(context, negocio.SalidasActivity.class);
                context.startActivity(i);
                break;
            case R.id.nav_Almacen:
                i = new Intent(context, negocio.AlmacenActivity.class);
                context.startActivity(i);
                break;
            case R.id.nav_ReporteAlmacenero:
                i = new Intent(context, negocio.ReporteAlmacenActivity.class);
                context.startActivity(i);
                break;
            case R.id.nav_salir:
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                i = new Intent(context, Acceso.class);
                context.startActivity(i);
                break;
        }
    }

}
