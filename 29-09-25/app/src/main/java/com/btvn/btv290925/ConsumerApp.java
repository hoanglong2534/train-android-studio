package com.btvn.btv290925;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class ConsumerApp extends AppCompatActivity {

    private ListView listViewBooks;
    private TextView tvStatus;
    private Button btnRefresh, btnViewProviderInfo;
    private BookConsumerAdapter bookAdapter;
    private List<Book> bookList;
    private ContentResolver contentResolver;
    private Uri contentUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumer_app);

        // Khởi tạo ContentResolver
        contentResolver = getContentResolver();
        contentUri = Uri.parse("content://com.btvn.btv290925.bookprovider/books");

        // Khởi tạo views
        initViews();
        
        // Setup ListView
        setupListView();
        
        // Set click listeners
        setupClickListeners();
        
        // Load dữ liệu ban đầu
        loadBooksFromProvider();
    }

    private void initViews() {
        listViewBooks = findViewById(R.id.listViewBooks);
        tvStatus = findViewById(R.id.tvStatus);
        btnRefresh = findViewById(R.id.btnRefresh);
        btnViewProviderInfo = findViewById(R.id.btnViewProviderInfo);
    }

    private void setupListView() {
        bookList = new ArrayList<>();
        bookAdapter = new BookConsumerAdapter(this, bookList);
        listViewBooks.setAdapter(bookAdapter);
    }

    private void setupClickListeners() {
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadBooksFromProvider();
            }
        });

        btnViewProviderInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProviderInfo();
            }
        });
    }

    private void loadBooksFromProvider() {
        try {
            // Query từ Content Provider
            Cursor cursor = contentResolver.query(
                    contentUri,
                    new String[]{
                        BookDatabaseHelper.COLUMN_ID,
                        BookDatabaseHelper.COLUMN_TITLE,
                        BookDatabaseHelper.COLUMN_AUTHOR,
                        BookDatabaseHelper.COLUMN_YEAR
                    },
                    null, null,
                    BookDatabaseHelper.COLUMN_TITLE + " ASC"
            );

            bookList.clear();
            
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    Book book = new Book();
                    book.setId(cursor.getInt(cursor.getColumnIndexOrThrow(BookDatabaseHelper.COLUMN_ID)));
                    book.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(BookDatabaseHelper.COLUMN_TITLE)));
                    book.setAuthor(cursor.getString(cursor.getColumnIndexOrThrow(BookDatabaseHelper.COLUMN_AUTHOR)));
                    book.setYear(cursor.getInt(cursor.getColumnIndexOrThrow(BookDatabaseHelper.COLUMN_YEAR)));
                    
                    bookList.add(book);
                }
                cursor.close();
            }

            // Cập nhật UI
            bookAdapter.updateList(bookList);
            updateStatus("Đã tải " + bookList.size() + " sách từ Content Provider");
            
            if (bookList.size() > 0) {
                Toast.makeText(this, "Đã tải " + bookList.size() + " sách từ Provider!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Không có sách nào trong Provider!", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            updateStatus("Lỗi khi tải dữ liệu: " + e.getMessage());
            Toast.makeText(this, "Lỗi khi kết nối đến Content Provider!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showProviderInfo() {
        StringBuilder info = new StringBuilder();
        info.append("📱 CONSUMER APP INFO\n\n");
        info.append("🔗 Provider Authority: com.btvn.btv290925.bookprovider\n");
        info.append("🌐 Content URI: ").append(contentUri.toString()).append("\n\n");
        info.append("📊 Dữ liệu hiện tại:\n");
        info.append("📖 Số sách: ").append(bookList.size()).append("\n\n");
        
        if (bookList.size() > 0) {
            info.append("📝 DANH SÁCH SÁCH:\n");
            info.append("═══════════════════════════════════\n");
            
            for (Book book : bookList) {
                info.append("🆔 ID: ").append(book.getId()).append("\n");
                info.append("📖 Tên: ").append(book.getTitle()).append("\n");
                info.append("✍️ Tác giả: ").append(book.getAuthor()).append("\n");
                info.append("📅 Năm: ").append(book.getYear()).append("\n");
                info.append("───────────────────────────────────\n");
            }
        } else {
            info.append("📭 Chưa có sách nào");
        }
        
        new android.app.AlertDialog.Builder(this)
                .setTitle("Consumer App Info")
                .setMessage(info.toString())
                .setPositiveButton("OK", null)
                .show();
    }

    private void updateStatus(String message) {
        tvStatus.setText(message);
    }
}
