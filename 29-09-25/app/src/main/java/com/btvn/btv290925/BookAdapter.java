package com.btvn.btv290925;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class BookAdapter extends BaseAdapter {
    private Context context;
    private List<Book> bookList;
    private OnBookActionListener listener;

    public interface OnBookActionListener {
        void onEditBook(Book book);
        void onDeleteBook(int bookId);
        void onRefreshList();
    }

    public BookAdapter(Context context, List<Book> bookList) {
        this.context = context;
        this.bookList = bookList;
    }

    public void setOnBookActionListener(OnBookActionListener listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return bookList.size();
    }

    @Override
    public Object getItem(int position) {
        return bookList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return bookList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
            holder = new ViewHolder();
            holder.tvTitle = convertView.findViewById(R.id.tvTitle);
            holder.tvAuthor = convertView.findViewById(R.id.tvAuthor);
            holder.tvYear = convertView.findViewById(R.id.tvYear);
            holder.btnEdit = convertView.findViewById(R.id.btnEdit);
            holder.btnDelete = convertView.findViewById(R.id.btnDelete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Book book = bookList.get(position);

        // Hiển thị thông tin sách
        holder.tvTitle.setText(book.getTitle());
        holder.tvAuthor.setText("Tác giả: " + book.getAuthor());
        holder.tvYear.setText("Năm: " + book.getYear());

        // Xử lý sự kiện nút Sửa
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onEditBook(book);
                }
            }
        });

        // Xử lý sự kiện nút Xóa
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onDeleteBook(book.getId());
                }
            }
        });

        return convertView;
    }

    // Cập nhật danh sách
    public void updateList(List<Book> newBookList) {
        this.bookList = newBookList;
        notifyDataSetChanged();
    }

    // ViewHolder pattern để tối ưu hiệu suất
    static class ViewHolder {
        TextView tvTitle;
        TextView tvAuthor;
        TextView tvYear;
        Button btnEdit;
        Button btnDelete;
    }
}
