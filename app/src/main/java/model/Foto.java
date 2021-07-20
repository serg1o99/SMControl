package model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.smcontrol.GestionarTrabajador;
import com.example.smcontrol.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

import id.zelory.compressor.Compressor;

public class Foto {

    private Context context;
    private Activity activity;
    private int requestCode;
    private int resultCode;
    private Intent data;

    private StorageReference storageReference,ref;
    public  Boolean color=false;
    private ProgressDialog cargando;
    private Button subir;
    private Uri downloadurl;

    private Bitmap thumb_bitmap;
    private String carpeta;

    public Foto(Context context, Activity activity, int requestCode, int resultCode, Intent data, Button subir, String carpeta) {
        this.context = context;
        this.activity = activity;
        this.requestCode = requestCode;
        this.resultCode = resultCode;
        this.data = data;
        this.subir = subir;
        this.carpeta = carpeta;
        storageReference = FirebaseStorage.getInstance().getReference().child(carpeta);
        cargando = new ProgressDialog(context,R.style.AppCompatAlertDialogStyle);
    }

    public Foto() {
    }
    public void  generadorDeFoto(){
        obtenerFoto();
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)    {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == Activity.RESULT_OK)    {
                Uri resultUri = result.getUri();
                File url = new File(resultUri.getPath());
                comprimirFoto(url);
                Toast.makeText(activity, "Imagen seleccionada",Toast.LENGTH_SHORT).show();
                subir.setEnabled(true);
                color=true;

            }
        }
    }

    private void obtenerFoto(){
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageuri = CropImage.getPickImageResultUri(context, data);
            //recortar imagen
            CropImage.activity(imageuri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(activity);

        }
    }

    private void comprimirFoto(File url){
        try {
            thumb_bitmap = new Compressor(context)
                    .setMaxWidth(640)
                    .setMaxHeight(480)
                    .setQuality(90)
                    .compressToBitmap(url);
        }catch (IOException e){
            e.printStackTrace();
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        thumb_bitmap.compress(Bitmap.CompressFormat.JPEG,90,byteArrayOutputStream);
        final byte [] thumb_byte = byteArrayOutputStream.toByteArray();
        subirFoto(thumb_byte);
    }

    private void subirFoto(byte[] thumb_byte) {
        subir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCarga();
                ref = storageReference.child(autogenerarCodigo(carpeta,"jpg"));
                UploadTask upload = ref.putBytes(thumb_byte);
                cargarAlStorage(upload);
            }
        });
    }

    private void dialogCarga(){
        cargando = new ProgressDialog(context,R.style.AppCompatAlertDialogStyle);
        cargando.setTitle("Subiendo foto...");
        cargando.setMessage("Espere por favor...");
        cargando.show();
    }

    private void cargarAlStorage(@NonNull UploadTask upload){
        Task<Uri> uriTask = upload.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if(!task.isSuccessful()){
                    throw  Objects.requireNonNull(task.getException());
                }
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                downloadurl = task.getResult();
                cargando.dismiss();
                Toast.makeText(context, "Imagen cargada con exito", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public Uri getDownloadurl() {
        return downloadurl;
    }

    public String autogenerarCodigo(String nombre, String formato) {
        int p = (int) (Math.random() * 25 + 1); int s = (int) (Math.random() * 25 + 1);
        int t = (int) (Math.random() * 25 + 1); int c = (int) (Math.random() * 25 + 1);
        int numero1= (int) (Math.random() * 1012 + 2111);
        int numero2= (int) (Math.random() * 1012 + 2111);
        String[] elementos= {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
        final String aleatorio = elementos[p] + elementos[s] + numero1 + elementos[t] + elementos[c] + numero2 + nombre +"."+formato;
        return aleatorio;
    }
}
