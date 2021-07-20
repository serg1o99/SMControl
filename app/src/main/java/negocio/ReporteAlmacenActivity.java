package negocio;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.smcontrol.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.DocumentException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Seguridad.Alerta;
import model.Entradas;
import model.PDF;
import model.ProductListCallback;
import model.Producto;
import model.Salida;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ReporteAlmacenActivity extends AppCompatActivity {

    private String[] header = {"Id Salida", "Id producto", "DNI Empleado","Nombre producto","Cantidad","Fecha"};
    private String shortText = "Hola";
    private String LongText = "Esto es una prueba test de viewer pdf generador de archivos en pdf";
    private PDF salidasPDF,entradasPDF;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    /*LocalDate date;
    DateTimeFormatter format;
    String fechaActual;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte_almacen);

        entradasPDF = new PDF(getApplicationContext());
        salidasPDF  = new PDF(getApplicationContext());

        inicializarFirebase();
    }

    public void inicializarFirebase(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase  = firebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    public void reporteSalida(View view) {
        databaseReference.child("Salidas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String[]> rows = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren() ){
                    Salida obj = dataSnapshot.getValue(Salida.class);
                    rows.add(new String[]{obj.getCod_salida(),obj.getCod_prod(),obj.getDni(),obj.getNombre_prod(),obj.getCantidad_salida()+"",obj.getFecha_salida()});
                }
                salidasPDF.openDocument();
                salidasPDF.addMetaData("Salida", "Reporte", "almacenero");
                try {
                    salidasPDF.addTittles("SM Control", "Salida de productos", fecha());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
                salidasPDF.createTable(header, rows);
                salidasPDF.closeDocument();
                salidasPDF.viewPDF();
                Toast.makeText(ReporteAlmacenActivity.this, "Reporte de salidas generado", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    public void reporteEntrada(View view) {
        databaseReference.child("Entradas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String[]> rows = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren() ){
                    Entradas obj = dataSnapshot.getValue(Entradas.class);
                    rows.add(new String[]{obj.getCod_entrada(),obj.getCod_prod(),obj.getDni(),obj.getNombre_prod(),obj.getCantidad_entrante()+"",obj.getFecha_ingreso()});
                }
                entradasPDF.openDocument();
                entradasPDF.addMetaData("Entrada", "Reporte", "almacenero");
                try {
                    entradasPDF.addTittles("SM Control", "Entrada de productos", fecha());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
                entradasPDF.createTable(header, rows);
                entradasPDF.closeDocument();
                entradasPDF.viewPDF();
                Toast.makeText(ReporteAlmacenActivity.this, "Reporte de entradas generado", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }


    public String fecha(){
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(c);
    }

}