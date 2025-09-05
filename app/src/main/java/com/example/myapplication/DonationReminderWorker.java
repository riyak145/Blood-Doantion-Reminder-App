package com.example.myapplication;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.myapplication.utils.NotificationUtils;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DonationReminderWorker extends Worker {

    public DonationReminderWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        String uid = FirebaseAuth.getInstance().getCurrentUser() != null
                ? FirebaseAuth.getInstance().getCurrentUser().getUid()
                : null;

        if (uid == null) return Result.success(); // No logged-in user

        FirebaseFirestore.getInstance().collection("users").document(uid).get()
                .addOnSuccessListener(snapshot -> {
                    if (snapshot.exists()) {
                        Timestamp lastDonation = snapshot.getTimestamp("lastDonation");
                        String gender = snapshot.getString("gender");

                        if (lastDonation != null && gender != null) {
                            int monthsGap = gender.equalsIgnoreCase("female") ? 4 : 3;

                            Calendar eligibleDate = Calendar.getInstance();
                            eligibleDate.setTime(lastDonation.toDate());
                            eligibleDate.add(Calendar.MONTH, monthsGap);

                            Calendar today = Calendar.getInstance();

                            long diffInMillis = eligibleDate.getTimeInMillis() - today.getTimeInMillis();
                            long daysRemaining = diffInMillis / (1000 * 60 * 60 * 24);

                            if (daysRemaining <= 0) {
                                sendNotification("You are eligible to donate today!",
                                        "Save a life today – donate blood again!",
                                        "eligible_today");
                            } else if (daysRemaining == 90 || daysRemaining == 60 || daysRemaining == 30) {
                                sendNotification("Donation Reminder",
                                        "Only " + daysRemaining + " days left to your next donation!",
                                        "reminder_" + daysRemaining);
                            }
                        }
                    }
                });

        return Result.success();
    }

    private void sendNotification(String title, String message, String uniqueKey) {
        Context context = getApplicationContext();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        String milestoneKey = uid + "_" + uniqueKey; // e.g., uid_reminder_90

        db.collection("notification_logs").document(milestoneKey).get().addOnSuccessListener(documentSnapshot -> {
            if (!documentSnapshot.exists()) {
                // Not sent before for this milestone
                NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                String channelId = "donation_channel";

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(
                            channelId, "Donation Reminders", NotificationManager.IMPORTANCE_HIGH
                    );
                    manager.createNotificationChannel(channel);
                }

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                        .setSmallIcon(R.drawable.logo_achievement)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true);

                manager.notify((int) System.currentTimeMillis(), builder.build());

                // Save in Firestore
                NotificationUtils.saveNotificationToFirestore(title, message);

                // Save as "sent" permanently for this milestone
                db.collection("notification_logs").document(milestoneKey)
                        .set(new NotificationLog(title, message, new Date()));
            }
        });
    }
}
