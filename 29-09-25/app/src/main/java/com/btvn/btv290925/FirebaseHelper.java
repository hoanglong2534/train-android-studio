package com.btvn.btv290925;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseHelper {
    private static final String TAG = "FirebaseHelper";
    private static final String COLLECTION_NOTES = "notes";
    
    private FirebaseFirestore db;
    private CollectionReference notesRef;

    public FirebaseHelper() {
        db = FirebaseFirestore.getInstance();
        notesRef = db.collection(COLLECTION_NOTES);
    }

    // Thêm ghi chú mới
    public void addNote(Note note, OnCompleteListener<DocumentReference> listener) {
        Map<String, Object> noteData = new HashMap<>();
        noteData.put("title", note.getTitle());
        noteData.put("content", note.getContent());
        noteData.put("userId", note.getUserId());
        noteData.put("createdAt", note.getCreatedAt());
        noteData.put("updatedAt", note.getUpdatedAt());

        notesRef.add(noteData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "Note added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.w(TAG, "Error adding note", e);
                    }
                })
                .addOnCompleteListener(listener);
    }

    // Lấy tất cả ghi chú của user
    public void getNotesByUser(String userId, OnCompleteListener<QuerySnapshot> listener) {
        notesRef.whereEqualTo("userId", userId)
                .orderBy("updatedAt", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(listener);
    }

    // Cập nhật ghi chú
    public void updateNote(String noteId, Note note, OnCompleteListener<Void> listener) {
        Map<String, Object> noteData = new HashMap<>();
        noteData.put("title", note.getTitle());
        noteData.put("content", note.getContent());
        noteData.put("updatedAt", new Date());

        notesRef.document(noteId)
                .update(noteData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Note updated successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.w(TAG, "Error updating note", e);
                    }
                })
                .addOnCompleteListener(listener);
    }

    // Xóa ghi chú
    public void deleteNote(String noteId, OnCompleteListener<Void> listener) {
        notesRef.document(noteId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Note deleted successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.w(TAG, "Error deleting note", e);
                    }
                })
                .addOnCompleteListener(listener);
    }

    // Lấy ghi chú theo ID
    public void getNoteById(String noteId, OnCompleteListener<DocumentSnapshot> listener) {
        notesRef.document(noteId)
                .get()
                .addOnCompleteListener(listener);
    }

    // Chuyển đổi DocumentSnapshot thành Note
    public static Note documentToNote(DocumentSnapshot document) {
        if (document.exists()) {
            Note note = new Note();
            note.setId(document.getId());
            note.setTitle(document.getString("title"));
            note.setContent(document.getString("content"));
            note.setUserId(document.getString("userId"));
            
            // Xử lý Date
            if (document.getDate("createdAt") != null) {
                note.setCreatedAt(document.getDate("createdAt"));
            }
            if (document.getDate("updatedAt") != null) {
                note.setUpdatedAt(document.getDate("updatedAt"));
            }
            
            return note;
        }
        return null;
    }

    // Chuyển đổi QuerySnapshot thành List<Note>
    public static List<Note> querySnapshotToNotes(QuerySnapshot querySnapshot) {
        List<Note> notes = new ArrayList<>();
        for (QueryDocumentSnapshot document : querySnapshot) {
            Note note = documentToNote(document);
            if (note != null) {
                notes.add(note);
            }
        }
        return notes;
    }
}
