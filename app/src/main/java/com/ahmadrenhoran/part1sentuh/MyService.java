package com.ahmadrenhoran.part1sentuh;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.IBinder;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MyService extends Service {

    private final IBinder binder = new LocalBinder();
    private MyServiceCallback callback;

    public interface MyServiceCallback {
        void onDataReceived(String data);
    }

    public void setCallback(MyServiceCallback callback) {
        this.callback = callback;
    }

    @Override
    public IBinder onBind(Intent intent) {
        showButton();
        return binder;
    }

    private void showButton() {
        final WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        LinearLayout layout = new LinearLayout(this);
        Button buttonInService = new Button(this);
        buttonInService.setText("Send Data");

        layout.addView(buttonInService);
        layout.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_LOCAL_FOCUS_MODE,
                PixelFormat.TRANSLUCENT
        );

        windowManager.addView(layout, params);

        buttonInService.setOnClickListener(v -> {
            if (callback != null) {
                callback.onDataReceived("Data from Service");
            } else {
                Toast.makeText(MyService.this, "Callback is null", Toast.LENGTH_SHORT).show();
            }
            windowManager.removeView(layout);
            stopSelf();
        });
    }

    public class LocalBinder extends Binder {
        MyService getService() {
            return MyService.this;
        }
    }
}


