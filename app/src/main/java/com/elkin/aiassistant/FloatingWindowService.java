package com.elkin.aiassistant;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;

public class FloatingWindowService extends Service {
    private WindowManager windowManager;
    private ImageView floatingView;

    @Override
    public void onCreate() {
        super.onCreate();
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        floatingView = new ImageView(this);
        floatingView.setImageResource(R.drawable.icono_app); // Usa tu icono

        // Configuración de la burbuja (tamaño y posición)
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                150, 150,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 100;
        params.y = 100;

        windowManager.addView(floatingView, params);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (floatingView != null) windowManager.removeView(floatingView);
    }

    @Override
    public IBinder onBind(Intent intent) { return null; }
}
