package com.btvn.btv290925;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class bai1 extends AppCompatActivity {

    private EditText edtUsername, edtPassword;
    private Button btnSave, btnClear;
    private TextView tvSavedInfo;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "LoginPrefs";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bai1);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khởi tạo các view
        initViews();
        
        // Khởi tạo SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        
        // Load dữ liệu đã lưu khi mở ứng dụng
        loadSavedData();
        
        // Set click listeners
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveLoginInfo();
            }
        });
        
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearLoginInfo();
            }
        });
    }

    private void initViews() {
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnSave = findViewById(R.id.btnSave);
        btnClear = findViewById(R.id.btnClear);
        tvSavedInfo = findViewById(R.id.tvSavedInfo);
    }

    private void saveLoginInfo() {
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ username và password!", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Lưu dữ liệu vào SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_PASSWORD, password);
        editor.apply();
        
        Toast.makeText(this, "Đã lưu thông tin đăng nhập!", Toast.LENGTH_SHORT).show();
        
        // Hiển thị thông tin đã lưu
        showSavedInfo();
    }

    private void clearLoginInfo() {
        // Xóa dữ liệu từ SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        
        // Xóa dữ liệu trên form
        edtUsername.setText("");
        edtPassword.setText("");
        
        // Ẩn thông tin đã lưu
        tvSavedInfo.setVisibility(View.GONE);
        
        Toast.makeText(this, "Đã xóa thông tin đăng nhập!", Toast.LENGTH_SHORT).show();
    }

    private void loadSavedData() {
        String savedUsername = sharedPreferences.getString(KEY_USERNAME, "");
        String savedPassword = sharedPreferences.getString(KEY_PASSWORD, "");
        
        if (!savedUsername.isEmpty() && !savedPassword.isEmpty()) {
            // Hiển thị dữ liệu đã lưu trên form
            edtUsername.setText(savedUsername);
            edtPassword.setText(savedPassword);
            
            // Hiển thị thông tin đã lưu
            showSavedInfo();
        }
    }

    private void showSavedInfo() {
        String savedUsername = sharedPreferences.getString(KEY_USERNAME, "");
        String savedPassword = sharedPreferences.getString(KEY_PASSWORD, "");
        
        if (!savedUsername.isEmpty() && !savedPassword.isEmpty()) {
            String info = "Thông tin đã lưu:\n" +
                         "Username: " + savedUsername + "\n" +
                         "Password: " + savedPassword;
            tvSavedInfo.setText(info);
            tvSavedInfo.setVisibility(View.VISIBLE);
        }
    }
}