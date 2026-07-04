package com.elkin.aiassistant;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout panelPrincipal = findViewById(R.id.panel_principal);
        Button btnIniciar = findViewById(R.id.btn_iniciar_ia);
        TextView textoEstado = findViewById(R.id.texto_estado);

        // Animación suave de entrada
        Animation animacionEntrada = AnimationUtils.loadAnimation(this, R.anim.entrada_suave);
        panelPrincipal.startAnimation(animacionEntrada);

        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textoEstado.setText("¡SISTEMA CONECTADO!");
                textoEstado.setTextColor(Color.parseColor("#FF007F"));
            }
        });
    }
}
