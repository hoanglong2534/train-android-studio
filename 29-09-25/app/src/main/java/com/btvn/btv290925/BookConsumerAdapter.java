package com.btvn.btv290925;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class BookConsumerAdapter extends BaseAdapter {
    private Context context;
    private List<Book> bookList;

    public BookConsumerAdapter(Context context, List<Book> bookList) {
        this.context = context;
        this.bookList = bookList;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_book_consumer, parent, false);
            holder = new ViewHolder();
            holder.tvTitle = convertView.findViewById(R.id.tvTitle);
            holder.tvAuthor = convertView.findViewById(R.id.tvAuthor);
            holder.tvYear = convertView.findViewById(R.id.tvYear);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Book book = bookList.get(position);

        // Hiển thị thông tin sách (chỉ đọc)
        holder.tvTitle.setText(book.getTitle());
        holder.tvAuthor.setText("Tác giả: " + book.getAuthor());
        holder.tvYear.setText("Năm: " + book.getYear());

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
    }
}
