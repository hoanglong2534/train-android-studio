package com.btvn.btvn150925;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class bai3 extends AppCompatActivity {

    private TextView tvAlarm;
    private Button btnSetAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bai3);

        tvAlarm = findViewById(R.id.tvAlarm);
        btnSetAlarm = findViewById(R.id.btnSetAlarm);

        btnSetAlarm.setOnClickListener(v -> {
            // Lấy giờ hiện tại
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            // Mở TimePickerDialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    bai3.this,
                    (TimePicker view, int selectedHour, int selectedMinute) -> {
                        String time = String.format("%02d:%02d", selectedHour, selectedMinute);
                        tvAlarm.setText("Báo thức đã được đặt lúc " + time);
                        Toast.makeText(bai3.this,
                                "Đã đặt báo thức lúc " + time,
                                Toast.LENGTH_SHORT).show();
                    },
                    hour, minute, true
            );

            timePickerDialog.show();
        });
    }
}
