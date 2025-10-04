package com.btvn.btv290925;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class BookProvider extends ContentProvider {
    
    // Authority - phải khớp với AndroidManifest.xml
    public static final String AUTHORITY = "com.btvn.btv290925.bookprovider";
    
    // Base URI
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/books");
    
    // MIME types
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.btvn.book";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.btvn.book";
    
    // URI matcher codes
    private static final int BOOKS = 1;
    private static final int BOOK_ID = 2;
    
    // UriMatcher
    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "books", BOOKS);
        uriMatcher.addURI(AUTHORITY, "books/#", BOOK_ID);
    }
    
    private BookDatabaseHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new BookDatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor;
        
        switch (uriMatcher.match(uri)) {
            case BOOKS:
                cursor = db.query(BookDatabaseHelper.TABLE_BOOKS, projection, selection, 
                        selectionArgs, null, null, sortOrder);
                break;
            case BOOK_ID:
                String id = uri.getLastPathSegment();
                cursor = db.query(BookDatabaseHelper.TABLE_BOOKS, projection, 
                        BookDatabaseHelper.COLUMN_ID + " = ?", new String[]{id}, 
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case BOOKS:
                return CONTENT_TYPE;
            case BOOK_ID:
                return CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id;
        
        switch (uriMatcher.match(uri)) {
            case BOOKS:
                id = db.insert(BookDatabaseHelper.TABLE_BOOKS, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        
        if (id > 0) {
            Uri newUri = ContentUris.withAppendedId(CONTENT_URI, id);
            getContext().getContentResolver().notifyChange(newUri, null);
            return newUri;
        } else {
            throw new SQLException("Failed to insert row into " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;
        
        switch (uriMatcher.match(uri)) {
            case BOOKS:
                count = db.delete(BookDatabaseHelper.TABLE_BOOKS, selection, selectionArgs);
                break;
            case BOOK_ID:
                String id = uri.getLastPathSegment();
                count = db.delete(BookDatabaseHelper.TABLE_BOOKS, 
                        BookDatabaseHelper.COLUMN_ID + " = ?", new String[]{id});
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;
        
        switch (uriMatcher.match(uri)) {
            case BOOKS:
                count = db.update(BookDatabaseHelper.TABLE_BOOKS, values, selection, selectionArgs);
                break;
            case BOOK_ID:
                String id = uri.getLastPathSegment();
                count = db.update(BookDatabaseHelper.TABLE_BOOKS, values, 
                        BookDatabaseHelper.COLUMN_ID + " = ?", new String[]{id});
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
