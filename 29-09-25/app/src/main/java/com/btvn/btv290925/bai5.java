package com.btvn.btv290925;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class bai5 extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private Button btnLogin, btnRegister, btnGuest;
    private TextView tvStatus;
    private ProgressBar progressBar;
    
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bai5);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khởi tạo Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        
        // Khởi tạo views
        initViews();
        
        // Set click listeners
        setupClickListeners();
        
        // Kiểm tra user đã đăng nhập chưa
        checkCurrentUser();
    }

    private void initViews() {
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        btnGuest = findViewById(R.id.btnGuest);
        tvStatus = findViewById(R.id.tvStatus);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupClickListeners() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        btnGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginAsGuest();
            }
        });
    }

    private void checkCurrentUser() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {

            goToNotesActivity();
        } else {
            updateStatus("Chưa đăng nhập");
        }
    }

    private void loginUser() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ email và password!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Password phải có ít nhất 6 ký tự!", Toast.LENGTH_SHORT).show();
            return;
        }

        showProgress(true);
        updateStatus("Đang đăng nhập...");

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        showProgress(false);
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateStatus("Đăng nhập thành công: " + user.getEmail());
                            Toast.makeText(bai5.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                            goToNotesActivity();
                        } else {
                            updateStatus("Đăng nhập thất bại");
                            Toast.makeText(bai5.this, "Đăng nhập thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void registerUser() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ email và password!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Password phải có ít nhất 6 ký tự!", Toast.LENGTH_SHORT).show();
            return;
        }

        showProgress(true);
        updateStatus("Đang đăng ký...");

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        showProgress(false);
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateStatus("Đăng ký thành công: " + user.getEmail());
                            Toast.makeText(bai5.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                            goToNotesActivity();
                        } else {
                            updateStatus("Đăng ký thất bại");
                            Toast.makeText(bai5.this, "Đăng ký thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void loginAsGuest() {
        // Tạo tài khoản guest tạm thời
        String guestEmail = "guest_" + System.currentTimeMillis() + "@example.com";
        String guestPassword = "guest123456";

        showProgress(true);
        updateStatus("Đang tạo tài khoản khách...");

        mAuth.createUserWithEmailAndPassword(guestEmail, guestPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        showProgress(false);
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateStatus("Đăng nhập với tài khoản khách: " + user.getEmail());
                            Toast.makeText(bai5.this, "Đăng nhập với tài khoản khách thành công!", Toast.LENGTH_SHORT).show();
                            goToNotesActivity();
                        } else {
                            updateStatus("Tạo tài khoản khách thất bại");
                            Toast.makeText(bai5.this, "Tạo tài khoản khách thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void goToNotesActivity() {
        Intent intent = new Intent(this, NotesActivity.class);
        startActivity(intent);
        finish();
    }

    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnLogin.setEnabled(!show);
        btnRegister.setEnabled(!show);
        btnGuest.setEnabled(!show);
    }

    private void updateStatus(String message) {
        tvStatus.setText(message);
    }
}