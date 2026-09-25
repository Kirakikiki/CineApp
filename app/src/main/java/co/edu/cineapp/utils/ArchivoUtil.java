package co.edu.cineapp.utils;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import co.edu.cineapp.local.MiPeliculaEntity;

public class ArchivoUtil {
    public static File exportarDatos(Context context, List<MiPeliculaEntity> lista) throws IOException{
        StringBuilder stringBuilder = new StringBuilder(); //testo que armamos linea por linea

        for (MiPeliculaEntity miPeliculaEntity : lista){
            stringBuilder.append(miPeliculaEntity.titulo).append(" - ").append(miPeliculaEntity.estado).append("\n");
        }

        //getFilesDir = carpeta privada de la app, nadie mas puede leer este archivo
        File archivo = new File(context.getFilesDir(), Constants.ARCHIVO_EXPORT);
        try (FileOutputStream fileOutputStream = new FileOutputStream(archivo)){ //abre el archivo para escribir
            fileOutputStream.write(stringBuilder.toString().getBytes());
        }//se cierra automaticamente al salir del try
        return archivo;
    }
}
