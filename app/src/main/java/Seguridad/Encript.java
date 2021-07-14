package Seguridad;

import android.util.Base64;
import java.security.MessageDigest;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Encript {



    public SecretKeySpec generarClave(String key) { //método para la clave de encriptación AES
        try {
            byte[] cadena=key.getBytes("UTF-8");
            MessageDigest md=MessageDigest.getInstance("SHA-1");
            cadena = md.digest(cadena);
            cadena = Arrays.copyOf(cadena, 16);
            SecretKeySpec secretKeySpec = new SecretKeySpec(cadena, "AES");
            return  secretKeySpec;
        }catch (Exception e)   {
            return null;
        }

    }


    public String Security(String encriptar)   { //método para encriptar el valor ingresado
    try {
     SecretKeySpec secretKeySpec=generarClave(encriptar);
        Cipher cipher= Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE,secretKeySpec);

        byte[] cadena= encriptar.getBytes("UTF-8");
        byte[] encriptada = cipher.doFinal(cadena);
        String  Security = Base64.encodeToString(encriptada,Base64.DEFAULT);
        return  Security;
    }catch (Exception e)   {
         return "";
    }
    }

}




