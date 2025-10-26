package com.th.btvn;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class BatteryStatusActivity extends AppCompatActivity {

    private TextView batteryPercentText, batteryStatusText, chargingTypeText, batteryTempText, batteryVoltageText;
    private ProgressBar batteryProgressBar;
    private BroadcastReceiver batteryReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_battery_status);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        batteryPercentText = findViewById(R.id.batteryPercentText);
        batteryStatusText = findViewById(R.id.batteryStatusText);
        chargingTypeText = findViewById(R.id.chargingTypeText);
        batteryTempText = findViewById(R.id.batteryTempText);
        batteryVoltageText = findViewById(R.id.batteryVoltageText);
        batteryProgressBar = findViewById(R.id.batteryProgressBar);

        // Create battery receiver
        batteryReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateBatteryInfo(intent);
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register battery receiver
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister battery receiver
        if (batteryReceiver != null) {
            unregisterReceiver(batteryReceiver);
        }
    }

    private void updateBatteryInfo(Intent intent) {
        // Get battery level
        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        float batteryPct = level * 100 / (float) scale;

        // Update battery percentage and progress bar
        batteryPercentText.setText(String.format("%.0f%%", batteryPct));
        batteryProgressBar.setProgress((int) batteryPct);

        // Get charging status
        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                           status == BatteryManager.BATTERY_STATUS_FULL;

        String statusText;
        if (isCharging) {
            if (status == BatteryManager.BATTERY_STATUS_FULL) {
                statusText = "🔋 Pin đầy (100%)";
                batteryStatusText.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            } else {
                statusText = "🔌 Đang sạc";
                batteryStatusText.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
            }
        } else {
            statusText = "📱 Không sạc";
            batteryStatusText.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
        }
        batteryStatusText.setText(statusText);

        // Get charging type (plug type)
        int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        String chargingType;
        if (chargePlug == BatteryManager.BATTERY_PLUGGED_USB) {
            chargingType = "USB";
        } else if (chargePlug == BatteryManager.BATTERY_PLUGGED_AC) {
            chargingType = "AC (Adapter)";
        } else if (chargePlug == BatteryManager.BATTERY_PLUGGED_WIRELESS) {
            chargingType = "Wireless";
        } else {
            chargingType = "Không sạc";
        }
        chargingTypeText.setText("Loại nguồn sạc: " + chargingType);

        // Get battery temperature
        int temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
        if (temperature != -1) {
            float tempCelsius = temperature / 10.0f;
            batteryTempText.setText(String.format("Nhiệt độ: %.1f°C", tempCelsius));
        }

        // Get battery voltage
        int voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
        if (voltage != -1) {
            float voltageValue = voltage / 1000.0f;
            batteryVoltageText.setText(String.format("Điện áp: %.2fV", voltageValue));
        }

        // Update progress bar color based on battery level
        if (batteryPct <= 20) {
            batteryProgressBar.getProgressDrawable().setColorFilter(
                getResources().getColor(android.R.color.holo_red_dark), 
                android.graphics.PorterDuff.Mode.SRC_IN);
        } else if (batteryPct <= 50) {
            batteryProgressBar.getProgressDrawable().setColorFilter(
                getResources().getColor(android.R.color.holo_orange_dark), 
                android.graphics.PorterDuff.Mode.SRC_IN);
        } else {
            batteryProgressBar.getProgressDrawable().setColorFilter(
                getResources().getColor(android.R.color.holo_green_dark), 
                android.graphics.PorterDuff.Mode.SRC_IN);
        }
    }
}
