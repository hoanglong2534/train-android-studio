package com.btvn.btv290925;

import android.content.Context;
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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class bai2 extends AppCompatActivity {

    private EditText edtFilename, edtContent;
    private Button btnSave, btnLoad, btnClear;
    private TextView tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bai2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khởi tạo các view
        initViews();
        
        // Set click listeners
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToFile();
            }
        });
        
        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFromFile();
            }
        });
        
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearContent();
            }
        });
    }

    private void initViews() {
        edtFilename = findViewById(R.id.edtFilename);
        edtContent = findViewById(R.id.edtContent);
        btnSave = findViewById(R.id.btnSave);
        btnLoad = findViewById(R.id.btnLoad);
        btnClear = findViewById(R.id.btnClear);
        tvStatus = findViewById(R.id.tvStatus);
    }

    private void saveToFile() {
        String filename = edtFilename.getText().toString().trim();
        String content = edtContent.getText().toString().trim();
        
        if (filename.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên file!", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (content.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập nội dung nhật ký!", Toast.LENGTH_SHORT).show();
            return;
        }
        
        try {
            // Thêm timestamp vào nội dung
            String timestamp = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
            String contentWithTimestamp = "[" + timestamp + "]\n" + content + "\n\n";
            
            // Ghi file vào Internal Storage
            FileOutputStream fos = openFileOutput(filename, Context.MODE_APPEND);
            fos.write(contentWithTimestamp.getBytes());
            fos.close();
            
            updateStatus("Đã lưu nhật ký vào file: " + filename);
            Toast.makeText(this, "Đã lưu nhật ký thành công!", Toast.LENGTH_SHORT).show();
            
        } catch (IOException e) {
            updateStatus("Lỗi khi lưu file: " + e.getMessage());
            Toast.makeText(this, "Lỗi khi lưu file!", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadFromFile() {
        String filename = edtFilename.getText().toString().trim();
        
        if (filename.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên file!", Toast.LENGTH_SHORT).show();
            return;
        }
        
        try {
            // Đọc file từ Internal Storage
            FileInputStream fis = openFileInput(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            
            reader.close();
            fis.close();
            
            // Hiển thị nội dung
            edtContent.setText(content.toString());
            updateStatus("Đã tải nhật ký từ file: " + filename);
            Toast.makeText(this, "Đã tải nhật ký thành công!", Toast.LENGTH_SHORT).show();
            
        } catch (IOException e) {
            updateStatus("Lỗi khi đọc file: " + e.getMessage());
            Toast.makeText(this, "Không thể đọc file hoặc file không tồn tại!", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearContent() {
        edtContent.setText("");
        updateStatus("Đã xóa nội dung");
        Toast.makeText(this, "Đã xóa nội dung!", Toast.LENGTH_SHORT).show();
    }

    private void updateStatus(String message) {
        tvStatus.setText(message);
    }
}