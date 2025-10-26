package com.th.btvn;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize buttons
        Button btnExercise1 = findViewById(R.id.btnExercise1);
        Button btnExercise2 = findViewById(R.id.btnExercise2);
        Button btnExercise3 = findViewById(R.id.btnExercise3);

        // Set click listeners
        btnExercise1.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ShakeDetectionActivity.class);
            startActivity(intent);
        });

        btnExercise2.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, BatteryStatusActivity.class);
            startActivity(intent);
        });

        btnExercise3.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, JsonListViewActivity.class);
            startActivity(intent);
        });
    }
}