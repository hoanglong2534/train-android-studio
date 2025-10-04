package com.btvn.btv290925;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class bai3 extends AppCompatActivity implements StudentAdapter.OnStudentActionListener {

    private EditText edtName, edtAge, edtClass;
    private Button btnAdd, btnUpdate, btnClear, btnViewDB;
    private ListView listViewStudents;
    
    private DatabaseHelper databaseHelper;
    private StudentAdapter studentAdapter;
    private List<Student> studentList;
    private Student selectedStudent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bai3);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khởi tạo các view
        initViews();
        
        // Khởi tạo database
        databaseHelper = new DatabaseHelper(this);
        
        // Load dữ liệu và setup ListView
        loadStudents();
        setupListView();
        
        // Set click listeners
        setupClickListeners();
    }

    private void initViews() {
        edtName = findViewById(R.id.edtName);
        edtAge = findViewById(R.id.edtAge);
        edtClass = findViewById(R.id.edtClass);
        btnAdd = findViewById(R.id.btnAdd);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnClear = findViewById(R.id.btnClear);
        btnViewDB = findViewById(R.id.btnViewDB);
        listViewStudents = findViewById(R.id.listViewStudents);
    }

    private void setupListView() {
        studentAdapter = new StudentAdapter(this, studentList, databaseHelper);
        studentAdapter.setOnStudentActionListener(this);
        listViewStudents.setAdapter(studentAdapter);
    }

    private void setupClickListeners() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStudent();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStudent();
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearForm();
            }
        });

        btnViewDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatabaseInfo();
            }
        });
    }

    private void addStudent() {
        String name = edtName.getText().toString().trim();
        String ageStr = edtAge.getText().toString().trim();
        String className = edtClass.getText().toString().trim();

        if (name.isEmpty() || ageStr.isEmpty() || className.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int age = Integer.parseInt(ageStr);
            if (age <= 0 || age > 100) {
                Toast.makeText(this, "Tuổi phải từ 1 đến 100!", Toast.LENGTH_SHORT).show();
                return;
            }

            Student student = new Student(name, age, className);
            long result = databaseHelper.addStudent(student);

            if (result != -1) {
                Toast.makeText(this, "Thêm sinh viên thành công!", Toast.LENGTH_SHORT).show();
                clearForm();
                loadStudents();
            } else {
                Toast.makeText(this, "Lỗi khi thêm sinh viên!", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Tuổi phải là số!", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateStudent() {
        if (selectedStudent == null) {
            Toast.makeText(this, "Vui lòng chọn sinh viên để cập nhật!", Toast.LENGTH_SHORT).show();
            return;
        }

        String name = edtName.getText().toString().trim();
        String ageStr = edtAge.getText().toString().trim();
        String className = edtClass.getText().toString().trim();

        if (name.isEmpty() || ageStr.isEmpty() || className.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int age = Integer.parseInt(ageStr);
            if (age <= 0 || age > 100) {
                Toast.makeText(this, "Tuổi phải từ 1 đến 100!", Toast.LENGTH_SHORT).show();
                return;
            }

            selectedStudent.setName(name);
            selectedStudent.setAge(age);
            selectedStudent.setClassName(className);

            int result = databaseHelper.updateStudent(selectedStudent);

            if (result > 0) {
                Toast.makeText(this, "Cập nhật sinh viên thành công!", Toast.LENGTH_SHORT).show();
                clearForm();
                loadStudents();
            } else {
                Toast.makeText(this, "Lỗi khi cập nhật sinh viên!", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Tuổi phải là số!", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearForm() {
        edtName.setText("");
        edtAge.setText("");
        edtClass.setText("");
        selectedStudent = null;
        btnUpdate.setEnabled(false);
    }

    private void loadStudents() {
        studentList = databaseHelper.getAllStudents();
        if (studentAdapter != null) {
            studentAdapter.updateList(studentList);
        }
    }

    // Implement OnStudentActionListener
    @Override
    public void onEditStudent(Student student) {
        selectedStudent = student;
        edtName.setText(student.getName());
        edtAge.setText(String.valueOf(student.getAge()));
        edtClass.setText(student.getClassName());
        btnUpdate.setEnabled(true);
        Toast.makeText(this, "Đã chọn sinh viên để chỉnh sửa", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteStudent(int studentId) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa sinh viên này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    databaseHelper.deleteStudent(studentId);
                    Toast.makeText(this, "Đã xóa sinh viên!", Toast.LENGTH_SHORT).show();
                    loadStudents();
                    if (selectedStudent != null && selectedStudent.getId() == studentId) {
                        clearForm();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    @Override
    public void onRefreshList() {
        loadStudents();
    }

    private void showDatabaseInfo() {
        List<Student> allStudents = databaseHelper.getAllStudents();
        int totalStudents = databaseHelper.getStudentCount();
        
        StringBuilder dbInfo = new StringBuilder();
        dbInfo.append("📊 THÔNG TIN DATABASE\n\n");
        dbInfo.append("📁 Database: StudentManager.db\n");
        dbInfo.append("📋 Bảng: students\n");
        dbInfo.append("👥 Tổng số sinh viên: ").append(totalStudents).append("\n\n");
        
        if (totalStudents > 0) {
            dbInfo.append("📝 DANH SÁCH CHI TIẾT:\n");
            dbInfo.append("═══════════════════════════════════\n");
            
            for (Student student : allStudents) {
                dbInfo.append("🆔 ID: ").append(student.getId()).append("\n");
                dbInfo.append("👤 Tên: ").append(student.getName()).append("\n");
                dbInfo.append("🎂 Tuổi: ").append(student.getAge()).append("\n");
                dbInfo.append("🏫 Lớp: ").append(student.getClassName()).append("\n");
                dbInfo.append("───────────────────────────────────\n");
            }
        } else {
            dbInfo.append("📭 Chưa có sinh viên nào trong database");
        }
        
        new AlertDialog.Builder(this)
                .setTitle("Thông tin Database")
                .setMessage(dbInfo.toString())
                .setPositiveButton("OK", null)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }
}