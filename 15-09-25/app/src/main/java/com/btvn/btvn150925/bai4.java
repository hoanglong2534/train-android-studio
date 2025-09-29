package com.btvn.btvn150925;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class bai4 extends AppCompatActivity {

    private TextView tvContext;
    private ClipboardManager clipboardManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bai4);

        tvContext = findViewById(R.id.tvContext);
        clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        // Đăng ký Context Menu cho TextView
        registerForContextMenu(tvContext);
    }

    // Tạo Context Menu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (v.getId() == R.id.tvContext) {
            menu.setHeaderTitle("Chọn thao tác");
            menu.add(0, 1, 0, "Copy");
            menu.add(0, 2, 1, "Paste");
            menu.add(0, 3, 2, "Clear");
        }
    }

    // Xử lý khi chọn item trong Context Menu
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1: // Copy
                ClipData clip = ClipData.newPlainText("text", tvContext.getText().toString());
                clipboardManager.setPrimaryClip(clip);
                Toast.makeText(this, "Đã copy", Toast.LENGTH_SHORT).show();
                return true;

            case 2: // Paste
                if (clipboardManager.hasPrimaryClip()) {
                    CharSequence pasteData = clipboardManager.getPrimaryClip().getItemAt(0).getText();
                    tvContext.setText(pasteData);
                    Toast.makeText(this, "Đã paste", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Clipboard trống", Toast.LENGTH_SHORT).show();
                }
                return true;

            case 3: // Clear
                tvContext.setText("");
                Toast.makeText(this, "Đã xóa nội dung", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onContextItemSelected(item);
    }
}
