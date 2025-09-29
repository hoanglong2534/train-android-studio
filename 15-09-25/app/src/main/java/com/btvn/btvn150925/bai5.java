package com.btvn.btvn150925;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class bai5 extends AppCompatActivity {

    private RecyclerView recyclerViewStudents;
    private StudentAdapter studentAdapter;
    private List<Student> studentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bai5);

        recyclerViewStudents = findViewById(R.id.recyclerViewStudents);
        recyclerViewStudents.setLayoutManager(new LinearLayoutManager(this));

        // Tạo danh sách mẫu
        studentList = new ArrayList<>();
        studentList.add(new Student("Nguyễn Văn A", "SV001", "CNTT1"));
        studentList.add(new Student("Trần Thị B", "SV002", "CNTT2"));
        studentList.add(new Student("Lê Văn C", "SV003", "CNTT1"));
        studentList.add(new Student("Phạm Thị D", "SV004", "CNTT3"));

        // Set Adapter
        studentAdapter = new StudentAdapter(studentList);
        recyclerViewStudents.setAdapter(studentAdapter);
    }
}
