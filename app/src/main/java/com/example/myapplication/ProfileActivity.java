package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {

    TextView toolbarTitle, profileUserName, profileLastDonation, numDonations, eligibleDate;

    private int[] iconResIds = {
            R.drawable.logo_edit_profile,
            R.drawable.logo_donation_history,
            R.drawable.logo_achievement
    };

    private String[] titles = {
            "Edit Profile",
            "Donation History",
            "Achievements"
    };

    private Class<?>[] targetActivities = {
            EditProfileActivity.class,
            DonationHistoryActivity.class,
            AchievementListActivity.class
    };

    private int[] cardViewIds = {
            R.id.editProfile,
            R.id.donationHistory,
            R.id.achievementList
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        FooterHandler.setupFooter(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Profile");

        // Profile text views
        profileUserName = findViewById(R.id.profileUserName);
        profileLastDonation = findViewById(R.id.profileLastDonation);
        numDonations = findViewById(R.id.numDonations);
        eligibleDate = findViewById(R.id.eligibleDate);

        // Loop through and initialize bar items
        for (int i = 0; i < cardViewIds.length; i++) {
            View card = findViewById(cardViewIds[i]);

            ImageView icon = card.findViewById(R.id.bar_item_icon);
            TextView title = card.findViewById(R.id.bar_item_title);

            icon.setImageResource(iconResIds[i]);
            title.setText(titles[i]);

            final int index = i;
            card.setOnClickListener(v -> {
                Intent intent = new Intent(ProfileActivity.this, targetActivities[index]);
                startActivity(intent);
            });
        }

        loadProfileData();
    }

    private void loadProfileData() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // User profile info
        db.collection("users").document(uid).get()
                .addOnSuccessListener(snapshot -> {
                    if (snapshot.exists()) {
                        String name = snapshot.getString("name");
                        String gender = snapshot.getString("gender");
                        Timestamp lastDonationTimestamp = snapshot.getTimestamp("lastDonation");

                        if (name != null)
                            profileUserName.setText("Hello, " + name + "!");

                        if (lastDonationTimestamp != null) {
                            Date lastDate = lastDonationTimestamp.toDate();
                            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.US);
                            profileLastDonation.setText("Last Donation: " + sdf.format(lastDate));

                            Calendar cal = Calendar.getInstance();
                            cal.setTime(lastDate);
                            cal.add(Calendar.MONTH, gender != null && gender.equalsIgnoreCase("female") ? 4 : 3);
                            eligibleDate.setText(sdf.format(cal.getTime()));
                        } else {
                            profileLastDonation.setText("Last Donation: -");
                            eligibleDate.setText("-");
                        }
                    }
                });

        // Donation count
        db.collection("users").document(uid).collection("donations")
                .get()
                .addOnSuccessListener(query -> {
                    int count = 0;
                    for (QueryDocumentSnapshot doc : query) count++;
                    numDonations.setText(String.valueOf(count));
                });
    }
}
