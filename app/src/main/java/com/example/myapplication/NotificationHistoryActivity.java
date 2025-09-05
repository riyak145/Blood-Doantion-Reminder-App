package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NotificationHistoryActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    NotificationAdapter adapter;
    List<NotificationItem> notificationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_history);
        FooterHandler.setupFooter(this);

        findViewById(R.id.toolbarTitle).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.toolbarTitle)).setText("Notifications");

        recyclerView = findViewById(R.id.notificationRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        notificationList = new ArrayList<>();
        adapter = new NotificationAdapter(notificationList);
        recyclerView.setAdapter(adapter);

        fetchNotifications();
    }

    private void fetchNotifications() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .collection("notifications")
                .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    notificationList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String title = doc.getString("message");
                        long timestampMillis = doc.getTimestamp("timestamp").toDate().getTime();
                        String formattedTime = new SimpleDateFormat("MMMM d, yyyy HH:mm", Locale.getDefault()).format(timestampMillis);
                        notificationList.add(new NotificationItem(title, formattedTime));
                    }
                    adapter.notifyDataSetChanged();
                });
    }

    // Notification item model
    public static class NotificationItem {
        public String title;
        public String time;
        public NotificationItem(String title, String time) {
            this.title = title;
            this.time = time;
        }
    }

    // RecyclerView Adapter
    public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
        List<NotificationItem> items;
        public NotificationAdapter(List<NotificationItem> items) {
            this.items = items;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView title, time;
            ViewHolder(View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.notificationTitle);
                time = itemView.findViewById(R.id.notificationTime);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
            android.view.View view = android.view.LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_notification, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            NotificationItem item = items.get(position);
            holder.title.setText(item.title);
            holder.time.setText(item.time);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }
}