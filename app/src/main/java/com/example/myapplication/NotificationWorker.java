package com.example.myapplication;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;

public class NotificationWorker extends Worker {

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid == null) return Result.failure();

        FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(snapshot -> {
                    if (snapshot.exists()) {
                        Timestamp lastDonation = snapshot.getTimestamp("lastDonation");
                        String gender = snapshot.getString("gender");

                        if (lastDonation == null) return;

                        Calendar cal = Calendar.getInstance();
                        cal.setTime(lastDonation.toDate());
                        if ("female".equalsIgnoreCase(gender)) {
                            cal.add(Calendar.MONTH, 4);
                        } else {
                            cal.add(Calendar.MONTH, 3);
                        }

                        Date eligibleDate = cal.getTime();
                        Date now = new Date();

                        long daysLeft = (eligibleDate.getTime() - now.getTime()) / (1000 * 60 * 60 * 24);

                        if (daysLeft <= 0) {
                            showNotification("You're eligible to donate!", "It's time to save lives again. Donate now!");
                        } else if (daysLeft == 30 || daysLeft == 60 || daysLeft == 90) {
                            showNotification("Reminder: Donation Approaching", "You're eligible in " + daysLeft + " days. Stay ready!");
                        }
                    }
                });

        return Result.success();
    }

    private void showNotification(String title, String message) {
        String channelId = "donation_reminder_channel";
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        // Create channel for Android 8+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Donation Reminders", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Reminders to donate blood based on eligibility");
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                .setSmallIcon(R.drawable.logo_shield) // Replace with your own icon
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true);

        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }
}
