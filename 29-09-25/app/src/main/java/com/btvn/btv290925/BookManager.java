package com.btvn.btv290925;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class BookManager {
    private ContentResolver contentResolver;
    private Uri contentUri;

    public BookManager(Context context) {
        this.contentResolver = context.getContentResolver();
        this.contentUri = BookProvider.CONTENT_URI;
    }

    // Thêm sách mới
    public Uri addBook(Book book) {
        ContentValues values = new ContentValues();
        values.put(BookDatabaseHelper.COLUMN_TITLE, book.getTitle());
        values.put(BookDatabaseHelper.COLUMN_AUTHOR, book.getAuthor());
        values.put(BookDatabaseHelper.COLUMN_YEAR, book.getYear());
        
        return contentResolver.insert(contentUri, values);
    }

    // Lấy tất cả sách
    public List<Book> getAllBooks() {
        List<Book> bookList = new ArrayList<>();
        
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
        
        return bookList;
    }

    // Cập nhật sách
    public int updateBook(Book book) {
        ContentValues values = new ContentValues();
        values.put(BookDatabaseHelper.COLUMN_TITLE, book.getTitle());
        values.put(BookDatabaseHelper.COLUMN_AUTHOR, book.getAuthor());
        values.put(BookDatabaseHelper.COLUMN_YEAR, book.getYear());
        
        Uri uri = Uri.withAppendedPath(contentUri, String.valueOf(book.getId()));
        return contentResolver.update(uri, values, null, null);
    }

    // Xóa sách
    public int deleteBook(int bookId) {
        Uri uri = Uri.withAppendedPath(contentUri, String.valueOf(bookId));
        return contentResolver.delete(uri, null, null);
    }

    // Lấy sách theo ID
    public Book getBook(int bookId) {
        Uri uri = Uri.withAppendedPath(contentUri, String.valueOf(bookId));
        
        Cursor cursor = contentResolver.query(
                uri,
                new String[]{
                    BookDatabaseHelper.COLUMN_ID,
                    BookDatabaseHelper.COLUMN_TITLE,
                    BookDatabaseHelper.COLUMN_AUTHOR,
                    BookDatabaseHelper.COLUMN_YEAR
                },
                null, null, null
        );
        
        Book book = null;
        if (cursor != null && cursor.moveToFirst()) {
            book = new Book();
            book.setId(cursor.getInt(cursor.getColumnIndexOrThrow(BookDatabaseHelper.COLUMN_ID)));
            book.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(BookDatabaseHelper.COLUMN_TITLE)));
            book.setAuthor(cursor.getString(cursor.getColumnIndexOrThrow(BookDatabaseHelper.COLUMN_AUTHOR)));
            book.setYear(cursor.getInt(cursor.getColumnIndexOrThrow(BookDatabaseHelper.COLUMN_YEAR)));
        }
        
        if (cursor != null) {
            cursor.close();
        }
        
        return book;
    }

    // Đếm số lượng sách
    public int getBookCount() {
        Cursor cursor = contentResolver.query(contentUri, null, null, null, null);
        int count = cursor != null ? cursor.getCount() : 0;
        if (cursor != null) {
            cursor.close();
        }
        return count;
    }
}
