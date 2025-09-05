package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DonationCentreActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DonationCentreAdapter adapter;
    private List<DonationCentre> centreList;
    private TextView emptyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_centre);

        recyclerView = findViewById(R.id.donationCentreRecyclerView);
        emptyText = findViewById(R.id.emptyDonationCentreMessage);
        centreList = new ArrayList<>();
        adapter = new DonationCentreAdapter(centreList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        fetchDonationCentres();
    }

    private void fetchDonationCentres() {
        FirebaseFirestore.getInstance()
                .collection("donation_centres")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    centreList.clear();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        String name = doc.getString("name");
                        String location = doc.getString("location");
                        String phone = doc.getString("phone");

                        if (name != null && location != null && phone != null) {
                            centreList.add(new DonationCentre(name, location, Collections.singletonList(phone)));
                        }
                    }

                    adapter.notifyDataSetChanged();
                    emptyText.setVisibility(centreList.isEmpty() ? View.VISIBLE : View.GONE);
                })
                .addOnFailureListener(e -> {
                    emptyText.setText("Failed to load donation centres");
                    emptyText.setVisibility(View.VISIBLE);
                });
    }
}
