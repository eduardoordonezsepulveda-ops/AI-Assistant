package com.elkin.aiassistant;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class FloatingWindowService extends Service {

    private WindowManager windowManager;
    private View burbujaView;
    private View panelView;
    private WindowManager.LayoutParams paramsBurbuja;
    private WindowManager.LayoutParams paramsPanel;
    private boolean panelAbierto = false;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // 1. Preparamos el sistema para dibujar sobre otras apps
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        // Cargamos los diseños que creaste
        burbujaView = inflater.inflate(R.layout.burbuja_flotante, null);
        panelView = inflater.inflate(R.layout.panel_flotante, null);

        // 2. Configuramos los permisos y el tamaño de la burbuja
        int tipoVentana;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            tipoVentana = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            tipoVentana = WindowManager.LayoutParams.TYPE_PHONE;
        }

        paramsBurbuja = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                tipoVentana,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        paramsBurbuja.gravity = Gravity.TOP | Gravity.LEFT;
        paramsBurbuja.x = 0;
        paramsBurbuja.y = 100;

        // Configuramos el tamaño del panel (centrado)
        paramsPanel = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                tipoVentana,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        paramsPanel.gravity = Gravity.CENTER;

        // 3. Añadimos la burbuja a la pantalla
        windowManager.addView(burbujaView, paramsBurbuja);

        // 4. Lógica para mover la burbuja con el dedo
        burbujaView.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = paramsBurbuja.x;
                        initialY = paramsBurbuja.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        // Si soltamos el dedo sin moverlo mucho, es un "clic" para abrir el panel
                        int diffX = (int) (event.getRawX() - initialTouchX);
                        int diffY = (int) (event.getRawY() - initialTouchY);
                        if (Math.abs(diffX) < 10 && Math.abs(diffY) < 10) {
                            abrirCerrarPanel();
                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        // Movemos la burbuja siguiendo el dedo
                        paramsBurbuja.x = initialX + (int) (event.getRawX() - initialTouchX);
                        paramsBurbuja.y = initialY + (int) (event.getRawY() - initialTouchY);
                        windowManager.updateViewLayout(burbujaView, paramsBurbuja);
                        return true;
                }
                return false;
            }
        });

        // 5. Botón para cerrar el panel
        Button btnCerrarPanel = panelView.findViewById(R.id.btn_cerrar_panel);
        btnCerrarPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCerrarPanel();
            }
        });
    }

    private void abrirCerrarPanel() {
        if (!panelAbierto) {
            // Escondemos la burbuja y mostramos el panel grande
            burbujaView.setVisibility(View.GONE);
            windowManager.addView(panelView, paramsPanel);
            panelAbierto = true;
        } else {
            // Quitamos el panel grande y volvemos a mostrar la burbuja
            windowManager.removeView(panelView);
            burbujaView.setVisibility(View.VISIBLE);
            panelAbierto = false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (burbujaView != null) windowManager.removeView(burbujaView);
        if (panelAbierto && panelView != null) windowManager.removeView(panelView);
    }
}

