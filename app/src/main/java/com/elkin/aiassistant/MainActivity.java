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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Estos IDs tienen que ser IGUALES a los del XML
        Button btnIniciar = findViewById(R.id.btn_iniciar);

        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificarPermisosYIniciar();
            }
        });
    }

    private void verificarPermisosYIniciar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            // Abrir configuración para dar permiso
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivity(intent);
            Toast.makeText(this, "Por favor, permite que la app se muestre sobre otras", Toast.LENGTH_LONG).show();
        } else {
            // Iniciar burbuja
            startService(new Intent(this, FloatingWindowService.class));
            finish();
        }
    }
}
