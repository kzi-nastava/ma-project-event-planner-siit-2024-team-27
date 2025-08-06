package com.wde.eventplanner.components;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class ShakeDetector implements SensorEventListener {
    private static final float SHAKE_THRESHOLD = 15.0f;
    private static final long SHAKE_TIME_LIMIT = 1500;
    private long lastShakeTime = 0;

    private final OnShakeListener onShakeListener;

    public ShakeDetector(OnShakeListener onShakeListener) {
        this.onShakeListener = onShakeListener;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            float acceleration = (x * x + y * y + z * z) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);

            if (acceleration > SHAKE_THRESHOLD) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastShakeTime > SHAKE_TIME_LIMIT) {
                    lastShakeTime = currentTime;
                    onShakeListener.onShake();
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public interface OnShakeListener {
        void onShake();
    }
}
