package com.th.btvn;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ShakeDetectionActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private TextView statusText, xValueText, yValueText, zValueText;
    
    // Shake detection variables
    private static final float SHAKE_THRESHOLD = 12.0f;
    private static final int SHAKE_WAIT_TIME_MS = 250;
    private long lastShakeTime;
    
    private float lastX, lastY, lastZ;
    private long lastUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shake_detection);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        statusText = findViewById(R.id.statusText);
        xValueText = findViewById(R.id.xValueText);
        yValueText = findViewById(R.id.yValueText);
        zValueText = findViewById(R.id.zValueText);

        // Initialize sensor
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if (accelerometer == null) {
            statusText.setText("Thiết bị không có cảm biến gia tốc!");
            Toast.makeText(this, "Thiết bị không hỗ trợ cảm biến gia tốc", Toast.LENGTH_LONG).show();
        } else {
            statusText.setText("Sẵn sàng phát hiện lắc...");
        }

        lastUpdate = System.currentTimeMillis();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            // Update UI with current values
            xValueText.setText(String.format("X: %.2f", x));
            yValueText.setText(String.format("Y: %.2f", y));
            zValueText.setText(String.format("Z: %.2f", z));

            long currentTime = System.currentTimeMillis();
            
            if ((currentTime - lastUpdate) > 100) {
                long timeDiff = currentTime - lastUpdate;
                lastUpdate = currentTime;

                // Calculate the total acceleration difference
                float deltaX = Math.abs(lastX - x);
                float deltaY = Math.abs(lastY - y);
                float deltaZ = Math.abs(lastZ - z);
                
                float totalDelta = deltaX + deltaY + deltaZ;
                float speed = Math.abs(totalDelta / timeDiff) * 10000;

                // Check for shake
                if (speed > SHAKE_THRESHOLD) {
                    if ((currentTime - lastShakeTime) > SHAKE_WAIT_TIME_MS) {
                        // Shake detected!
                        statusText.setText("🔔 ĐÃ PHÁT HIỆN LẮC ĐIỆN THOẠI! 🔔");
                        Toast.makeText(this, "Điện thoại đã được lắc!", Toast.LENGTH_SHORT).show();
                        lastShakeTime = currentTime;
                        
                        // Reset status after 2 seconds
                        statusText.postDelayed(() -> {
                            statusText.setText("Sẵn sàng phát hiện lắc...");
                        }, 2000);
                    }
                }

                lastX = x;
                lastY = y;
                lastZ = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used in this implementation
    }
}
