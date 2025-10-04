package com.btvn.btv290925;

import android.net.Uri;
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

public class bai4 extends AppCompatActivity implements BookAdapter.OnBookActionListener {

    private EditText edtTitle, edtAuthor, edtYear;
    private Button btnAdd, btnUpdate, btnClear, btnViewProvider;
    private ListView listViewBooks;
    
    private BookManager bookManager;
    private BookAdapter bookAdapter;
    private List<Book> bookList;
    private Book selectedBook = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bai4);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khởi tạo các view
        initViews();
        
        // Khởi tạo BookManager
        bookManager = new BookManager(this);
        
        // Load dữ liệu và setup ListView
        loadBooks();
        setupListView();
        
        // Set click listeners
        setupClickListeners();
    }

    private void initViews() {
        edtTitle = findViewById(R.id.edtTitle);
        edtAuthor = findViewById(R.id.edtAuthor);
        edtYear = findViewById(R.id.edtYear);
        btnAdd = findViewById(R.id.btnAdd);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnClear = findViewById(R.id.btnClear);
        btnViewProvider = findViewById(R.id.btnViewProvider);
        listViewBooks = findViewById(R.id.listViewBooks);
    }

    private void setupListView() {
        bookAdapter = new BookAdapter(this, bookList);
        bookAdapter.setOnBookActionListener(this);
        listViewBooks.setAdapter(bookAdapter);
    }

    private void setupClickListeners() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBook();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBook();
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearForm();
            }
        });

        btnViewProvider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProviderInfo();
            }
        });
    }

    private void addBook() {
        String title = edtTitle.getText().toString().trim();
        String author = edtAuthor.getText().toString().trim();
        String yearStr = edtYear.getText().toString().trim();

        if (title.isEmpty() || author.isEmpty() || yearStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int year = Integer.parseInt(yearStr);
            if (year < 1000 || year > 3000) {
                Toast.makeText(this, "Năm xuất bản phải từ 1000 đến 3000!", Toast.LENGTH_SHORT).show();
                return;
            }

            Book book = new Book(title, author, year);
            Uri result = bookManager.addBook(book);

            if (result != null) {
                Toast.makeText(this, "Thêm sách thành công!", Toast.LENGTH_SHORT).show();
                clearForm();
                loadBooks();
            } else {
                Toast.makeText(this, "Lỗi khi thêm sách!", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Năm xuất bản phải là số!", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateBook() {
        if (selectedBook == null) {
            Toast.makeText(this, "Vui lòng chọn sách để cập nhật!", Toast.LENGTH_SHORT).show();
            return;
        }

        String title = edtTitle.getText().toString().trim();
        String author = edtAuthor.getText().toString().trim();
        String yearStr = edtYear.getText().toString().trim();

        if (title.isEmpty() || author.isEmpty() || yearStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int year = Integer.parseInt(yearStr);
            if (year < 1000 || year > 3000) {
                Toast.makeText(this, "Năm xuất bản phải từ 1000 đến 3000!", Toast.LENGTH_SHORT).show();
                return;
            }

            selectedBook.setTitle(title);
            selectedBook.setAuthor(author);
            selectedBook.setYear(year);

            int result = bookManager.updateBook(selectedBook);

            if (result > 0) {
                Toast.makeText(this, "Cập nhật sách thành công!", Toast.LENGTH_SHORT).show();
                clearForm();
                loadBooks();
            } else {
                Toast.makeText(this, "Lỗi khi cập nhật sách!", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Năm xuất bản phải là số!", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearForm() {
        edtTitle.setText("");
        edtAuthor.setText("");
        edtYear.setText("");
        selectedBook = null;
        btnUpdate.setEnabled(false);
    }

    private void loadBooks() {
        bookList = bookManager.getAllBooks();
        if (bookAdapter != null) {
            bookAdapter.updateList(bookList);
        }
    }

    private void showProviderInfo() {
        int totalBooks = bookManager.getBookCount();
        
        StringBuilder providerInfo = new StringBuilder();
        providerInfo.append("📚 THÔNG TIN CONTENT PROVIDER\n\n");
        providerInfo.append("🔗 Authority: ").append(BookProvider.AUTHORITY).append("\n");
        providerInfo.append("📁 Database: ").append("BookProvider.db").append("\n");
        providerInfo.append("📋 Bảng: books\n");
        providerInfo.append("📖 Tổng số sách: ").append(totalBooks).append("\n\n");
        providerInfo.append("🌐 URI: ").append(BookProvider.CONTENT_URI.toString()).append("\n\n");
        
        if (totalBooks > 0) {
            providerInfo.append("📝 DANH SÁCH SÁCH:\n");
            providerInfo.append("═══════════════════════════════════\n");
            
            for (Book book : bookList) {
                providerInfo.append("🆔 ID: ").append(book.getId()).append("\n");
                providerInfo.append("📖 Tên: ").append(book.getTitle()).append("\n");
                providerInfo.append("✍️ Tác giả: ").append(book.getAuthor()).append("\n");
                providerInfo.append("📅 Năm: ").append(book.getYear()).append("\n");
                providerInfo.append("───────────────────────────────────\n");
            }
        } else {
            providerInfo.append("📭 Chưa có sách nào trong database");
        }
        
        new AlertDialog.Builder(this)
                .setTitle("Content Provider Info")
                .setMessage(providerInfo.toString())
                .setPositiveButton("OK", null)
                .show();
    }

    // Implement OnBookActionListener
    @Override
    public void onEditBook(Book book) {
        selectedBook = book;
        edtTitle.setText(book.getTitle());
        edtAuthor.setText(book.getAuthor());
        edtYear.setText(String.valueOf(book.getYear()));
        btnUpdate.setEnabled(true);
        Toast.makeText(this, "Đã chọn sách để chỉnh sửa", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteBook(int bookId) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa sách này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    int result = bookManager.deleteBook(bookId);
                    if (result > 0) {
                        Toast.makeText(this, "Đã xóa sách!", Toast.LENGTH_SHORT).show();
                        loadBooks();
                        if (selectedBook != null && selectedBook.getId() == bookId) {
                            clearForm();
                        }
                    } else {
                        Toast.makeText(this, "Lỗi khi xóa sách!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    @Override
    public void onRefreshList() {
        loadBooks();
    }
}