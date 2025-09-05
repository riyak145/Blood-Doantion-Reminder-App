package com.example.myapplication;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AchievementListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AchievementAdapter adapter;
    private List<AchievementItem> achievementList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement_list);

        recyclerView = findViewById(R.id.recyclerViewAchievements);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        achievementList = new ArrayList<>();
        TextView toolbarTitle = findViewById(R.id.toolbarTitle);
        if (toolbarTitle != null) {
            toolbarTitle.setText("Achievement List"); // ✅ Change toolbar text
        }
        fetchDonationCount();
    }

    private void fetchDonationCount() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .collection("donations")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    int donationCount = querySnapshot.size();

                    achievementList.add(new AchievementItem("First Donation", "Make your first donation!", donationCount, 1));
                    achievementList.add(new AchievementItem("5 Donations", "Donate blood 5 times!", donationCount, 5));
                    achievementList.add(new AchievementItem("10 Donations", "Regular life saver!", donationCount, 10));
                    achievementList.add(new AchievementItem("15 Donations", "Blood hero!", donationCount, 15));
                    achievementList.add(new AchievementItem("25 Donations", "Veteran donor!", donationCount, 25));
                    achievementList.add(new AchievementItem("50 Donations", "True legend!", donationCount, 50));
                    achievementList.add(new AchievementItem("100 Donations", "Savior of 100 lives!", donationCount, 100));

                    adapter = new AchievementAdapter(achievementList);
                    recyclerView.setAdapter(adapter);
                })
                .addOnFailureListener(e -> {
                    // handle failure
                });
    }
}
