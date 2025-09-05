package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DonationHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DonationAdapter adapter;
    private List<DonationItem> donationList;
    private TextView emptyMessage, toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_history);
        FooterHandler.setupFooter(this);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Donation History");
        recyclerView = findViewById(R.id.donationRecyclerView);
        emptyMessage = findViewById(R.id.emptyMessageText);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        donationList = new ArrayList<>();
        adapter = new DonationAdapter(donationList);
        recyclerView.setAdapter(adapter);

        fetchDonations();
    }

    private void fetchDonations() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .collection("donations")
                .orderBy("date", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    donationList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Timestamp timestamp = doc.getTimestamp("date");
                        String location = doc.getString("location");
                        String notes = doc.getString("notes");

                        donationList.add(new DonationItem(timestamp != null ? timestamp.toDate() : null, location, notes));

                    }

                    adapter.notifyDataSetChanged();

                    if (donationList.isEmpty()) {
                        recyclerView.setVisibility(View.GONE);
                        emptyMessage.setVisibility(View.VISIBLE);
                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                        emptyMessage.setVisibility(View.GONE);
                    }
                })
                .addOnFailureListener(e -> {
                    emptyMessage.setText("Failed to load donations.");
                    emptyMessage.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                });
    }
}
