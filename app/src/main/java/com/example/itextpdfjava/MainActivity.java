package com.example.itextpdfjava;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static List<Usuario> listaUsuarios = new ArrayList<>();
    Button btnCrearPDF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(checkPermission()) {
            Toast.makeText(this, "Permiso Aceptado", Toast.LENGTH_LONG).show();
        } else {
            requestPermissions();
        }

        btnCrearPDF = findViewById(R.id.btnCrearPdf);

        listaUsuarios.add(new Usuario("xcheko51x", "Sergio Peralta", "sergiop@local.com"));
        listaUsuarios.add(new Usuario("laurap", "Laura Perez", "laurap@local.com"));
        listaUsuarios.add(new Usuario("juanm", "Juan Morales", "juanm@local.com"));

        btnCrearPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crearPDF();
            }
        });
    }

    public static void crearPDF() {
        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/EjemploITextPDF";

            File dir = new File(path);
            if(!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File(dir, "usuarios.pdf");
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            Document documento = new Document();
            PdfWriter.getInstance(documento, fileOutputStream);

            documento.open();

            Paragraph titulo = new Paragraph(
                    "Lista de Usuarios \n\n\n",
                    FontFactory.getFont("arial", 22, Font.BOLD, BaseColor.BLUE)
            );

            documento.add(titulo);

            PdfPTable tabla = new PdfPTable(3);
            tabla.addCell("USUARIO");
            tabla.addCell("NOMBRE");
            tabla.addCell("CORREO");

            for (int i = 0 ; i < listaUsuarios.size() ; i++) {
                tabla.addCell(listaUsuarios.get(i).usuario);
                tabla.addCell(listaUsuarios.get(i).nombre);
                tabla.addCell(listaUsuarios.get(i).email);
            }

            documento.add(tabla);

            documento.close();


        } catch (FileNotFoundException | DocumentException e) {
            e.printStackTrace();
        }
    }

    private boolean checkPermission() {
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 200);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 200) {
            if(grantResults.length > 0) {
                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if(writeStorage && readStorage) {
                    Toast.makeText(this, "Permiso concedido", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Permiso denegado", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }
    }
}