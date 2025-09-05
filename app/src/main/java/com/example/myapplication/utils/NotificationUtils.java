package com.example.myapplication.utils;

import android.util.Log;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class NotificationUtils {

    public static void saveNotificationToFirestore(String title, String message) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> notification = new HashMap<>();
        notification.put("title", title);
        notification.put("message", message);
        notification.put("timestamp", Timestamp.now());

        db.collection("users")
                .document(uid)
                .collection("notifications")
                .add(notification)
                .addOnSuccessListener(docRef -> {
                    Log.d("Notification", "Saved to Firestore");
                })
                .addOnFailureListener(e -> {
                    Log.e("Notification", "Error saving notification", e);
                });
    }
}
