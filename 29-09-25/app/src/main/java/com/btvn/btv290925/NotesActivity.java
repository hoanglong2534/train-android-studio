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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class NotesActivity extends AppCompatActivity implements NoteAdapter.OnNoteActionListener {

    private EditText edtTitle, edtContent;
    private Button btnAdd, btnUpdate, btnClear, btnLogout;
    private TextView tvUserInfo;
    private RecyclerView recyclerViewNotes;
    private ProgressBar progressBar;
    
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseHelper firebaseHelper;
    private NoteAdapter noteAdapter;
    private List<Note> noteList;
    private Note selectedNote = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notes);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khởi tạo Firebase
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        firebaseHelper = new FirebaseHelper();
        
        // Kiểm tra user đã đăng nhập chưa
        if (currentUser == null) {
            goToLoginActivity();
            return;
        }
        
        // Khởi tạo views
        initViews();
        
        // Setup RecyclerView
        setupRecyclerView();
        
        // Set click listeners
        setupClickListeners();
        
        // Load dữ liệu
        loadNotes();
        
        // Hiển thị thông tin user
        updateUserInfo();
    }

    private void initViews() {
        edtTitle = findViewById(R.id.edtTitle);
        edtContent = findViewById(R.id.edtContent);
        btnAdd = findViewById(R.id.btnAdd);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnClear = findViewById(R.id.btnClear);
        btnLogout = findViewById(R.id.btnLogout);
        tvUserInfo = findViewById(R.id.tvUserInfo);
        recyclerViewNotes = findViewById(R.id.recyclerViewNotes);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupRecyclerView() {
        noteList = new ArrayList<>();
        noteAdapter = new NoteAdapter(noteList);
        noteAdapter.setOnNoteActionListener(this);
        
        recyclerViewNotes.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewNotes.setAdapter(noteAdapter);
    }

    private void setupClickListeners() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNote();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNote();
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearForm();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    private void updateUserInfo() {
        if (currentUser != null) {
            tvUserInfo.setText("User: " + currentUser.getEmail());
        }
    }

    private void addNote() {
        String title = edtTitle.getText().toString().trim();
        String content = edtContent.getText().toString().trim();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ tiêu đề và nội dung!", Toast.LENGTH_SHORT).show();
            return;
        }

        showProgress(true);
        
        Note note = new Note(title, content, currentUser.getUid());
        
        firebaseHelper.addNote(note, new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(Task<DocumentReference> task) {
                showProgress(false);
                if (task.isSuccessful()) {
                    Toast.makeText(NotesActivity.this, "Thêm ghi chú thành công!", Toast.LENGTH_SHORT).show();
                    clearForm();
                    loadNotes();
                } else {
                    Toast.makeText(NotesActivity.this, "Lỗi khi thêm ghi chú: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateNote() {
        if (selectedNote == null) {
            Toast.makeText(this, "Vui lòng chọn ghi chú để cập nhật!", Toast.LENGTH_SHORT).show();
            return;
        }

        String title = edtTitle.getText().toString().trim();
        String content = edtContent.getText().toString().trim();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ tiêu đề và nội dung!", Toast.LENGTH_SHORT).show();
            return;
        }

        showProgress(true);
        
        selectedNote.setTitle(title);
        selectedNote.setContent(content);
        
        firebaseHelper.updateNote(selectedNote.getId(), selectedNote, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                showProgress(false);
                if (task.isSuccessful()) {
                    Toast.makeText(NotesActivity.this, "Cập nhật ghi chú thành công!", Toast.LENGTH_SHORT).show();
                    clearForm();
                    loadNotes();
                } else {
                    Toast.makeText(NotesActivity.this, "Lỗi khi cập nhật ghi chú: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void clearForm() {
        edtTitle.setText("");
        edtContent.setText("");
        selectedNote = null;
        btnUpdate.setEnabled(false);
    }

    private void loadNotes() {
        showProgress(true);
        
        firebaseHelper.getNotesByUser(currentUser.getUid(), new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                showProgress(false);
                if (task.isSuccessful()) {
                    List<Note> notes = FirebaseHelper.querySnapshotToNotes(task.getResult());
                    noteAdapter.updateList(notes);
                    Toast.makeText(NotesActivity.this, "Đã tải " + notes.size() + " ghi chú", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(NotesActivity.this, "Lỗi khi tải ghi chú: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void logout() {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận đăng xuất")
                .setMessage("Bạn có chắc chắn muốn đăng xuất?")
                .setPositiveButton("Đăng xuất", (dialog, which) -> {
                    mAuth.signOut();
                    goToLoginActivity();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(this, bai5.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnAdd.setEnabled(!show);
        btnUpdate.setEnabled(!show && selectedNote != null);
        btnClear.setEnabled(!show);
        btnLogout.setEnabled(!show);
    }

    // Implement OnNoteActionListener
    @Override
    public void onEditNote(Note note) {
        selectedNote = note;
        edtTitle.setText(note.getTitle());
        edtContent.setText(note.getContent());
        btnUpdate.setEnabled(true);
        Toast.makeText(this, "Đã chọn ghi chú để chỉnh sửa", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteNote(Note note) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa ghi chú này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    showProgress(true);
                    firebaseHelper.deleteNote(note.getId(), new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {
                            showProgress(false);
                            if (task.isSuccessful()) {
                                Toast.makeText(NotesActivity.this, "Đã xóa ghi chú!", Toast.LENGTH_SHORT).show();
                                loadNotes();
                                if (selectedNote != null && selectedNote.getId().equals(note.getId())) {
                                    clearForm();
                                }
                            } else {
                                Toast.makeText(NotesActivity.this, "Lỗi khi xóa ghi chú: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
