package com.elkin.aiassistant;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISO_FLOTANTE_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnActivar = findViewById(R.id.btn_activar);

        btnActivar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verificar si tenemos el permiso de Android para flotar
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(MainActivity.this)) {
                    // Si no lo tenemos, abrimos la configuración para pedirlo
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, PERMISO_FLOTANTE_CODE);
                    Toast.makeText(MainActivity.this, "Por favor, otorga el permiso para mostrar sobre otras apps", Toast.LENGTH_LONG).show();
                } else {
                    // Si ya lo tenemos, encendemos el motor flotante
                    iniciarServicioFlotante();
                }
            }
        });
    }

    private void iniciarServicioFlotante() {
        Intent intent = new Intent(MainActivity.this, FloatingWindowService.class);
        startService(intent);
        Toast.makeText(this, "AI Assistant Activado en Segundo Plano", Toast.LENGTH_SHORT).show();
        // Cierra el menú principal para dejar solo la burbuja en la pantalla
        finish();
    }
}
