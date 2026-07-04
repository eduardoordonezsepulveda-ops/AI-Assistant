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
    
    private static final int REQUEST_OVERLAY = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Conectamos los nuevos botones de tu diseño profesional
        Button btnIniciar = findViewById(R.id.btn_iniciar);
        Button btnPersonalidad = findViewById(R.id.btn_personalidad);
        Button btnAjustes = findViewById(R.id.btn_ajustes);

        // 2. Configuramos el botón principal para lanzar la burbuja
        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarBurbuja();
            }
        });

        // 3. Dejamos los otros botones listos para programarlos después
        btnPersonalidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Cargando matriz de personalidad...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void iniciarBurbuja() {
        // Verifica si el celular nos da permiso de mostrar burbujas
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "Por favor, otorga el permiso para la burbuja", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, REQUEST_OVERLAY);
        } else {
            // ¡Lanzamos a Aether!
            Intent servicioBurbuja = new Intent(MainActivity.this, FloatingWindowService.class);
            startService(servicioBurbuja);
            Toast.makeText(this, "Iniciando sistema Aether...", Toast.LENGTH_SHORT).show();
            
            // Cierra la pantalla principal para dejar solo la burbuja flotando
            finish(); 
        }
    }
}

