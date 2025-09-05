package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.Locale;
import java.util.concurrent.TimeUnit;
import androidx.work.OneTimeWorkRequest;


public class home extends AppCompatActivity {

    Button btnAddBloodDonation, btnAddSDPDonation, btnAddBloodReminder;
    LinearLayout donationCentre, requestForm, achievementList, eduResources,donationImportance, donationProcess, eligibility,bloodTypes, donorBenefits;
    TextView textEligibilityDays, welcomeText;
    long daysLeft;

    boolean isBloodEligible = false, isSDPEligible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        FooterHandler.setupFooter(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView notificationIcon = findViewById(R.id.notificationIcon);
        notificationIcon.setVisibility(View.VISIBLE); // Only on Home screen

        notificationIcon.setOnClickListener(v -> {
            startActivity(new Intent(home.this, NotificationHistoryActivity.class));
        });


        TextView toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Home");

        // UI References
        btnAddBloodDonation = findViewById(R.id.btnAddBloodDonation);
//        btnAddSDPDonation = findViewById(R.id.btnAddSDPDonation);
        welcomeText = findViewById(R.id.welcomeText);
        textEligibilityDays = findViewById(R.id.daysLeftText);
        fetchAndSetUserName();
        // Fetch and update eligibility
        calculateEligibility();

        btnAddBloodDonation.setOnClickListener(v -> {
            if (isBloodEligible) {
                startActivity(new Intent(home.this, AddDonationActivity.class));
            } else {
                Toast.makeText(this, "You are not eligible to donate yet. Please wait for " + daysLeft + " days!",Toast.LENGTH_SHORT).show();
            }
        });

//        btnAddSDPDonation.setOnClickListener(v -> {
//            if (isSDPEligible) {
//                startActivity(new Intent(home.this, AddDonationActivity.class));
//            } else {
//                Toast.makeText(this, "You are not eligible to donate yet. Please wait!", Toast.LENGTH_SHORT).show();
//            }
//        });

        // Grid logic unchanged...
        donationCentre = findViewById(R.id.donationCentre);
//        requestForm = findViewById(R.id.requestForm);
        achievementList = findViewById(R.id.achievementList);
//        eduResources = findViewById(R.id.eduResources);
        donationImportance = findViewById(R.id.donationImportance);
        donationProcess = findViewById(R.id.donationProcess);
        eligibility = findViewById(R.id.eligibility);
        bloodTypes = findViewById(R.id.bloodTypes);
        donorBenefits = findViewById(R.id.donorBenefits);

        donationCentre.setOnClickListener(v -> startActivity(new Intent(home.this, DonationCentreActivity.class)));
//        requestForm.setOnClickListener(v -> startActivity(new Intent(home.this, RequestFormActivity.class)));
        achievementList.setOnClickListener(v -> startActivity(new Intent(home.this, AchievementListActivity.class)));
//        eduResources.setOnClickListener(v -> startActivity(new Intent(home.this, EducationalResourcesActivity.class)));
        donationImportance.setOnClickListener(v -> startActivity(new Intent(home.this, DonationImportanceActivity.class)));
        donationProcess.setOnClickListener(v -> startActivity(new Intent(home.this, DonationProcessActivity.class)));
        eligibility.setOnClickListener(v -> startActivity(new Intent(home.this, EligibilityActivity.class)));
        bloodTypes.setOnClickListener(v -> startActivity(new Intent(home.this, BloodTypesActivity.class)));
        donorBenefits.setOnClickListener(v -> startActivity(new Intent(home.this, DonorBenefitsActivity.class)));
//        btnAddBloodReminder.setOnClickListener(v -> startActivity(new Intent(home.this, DonorBenefitsActivity.class)));
    }

    private void calculateEligibility() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String gender = documentSnapshot.getString("gender");
                        Timestamp lastDonationTimestamp = documentSnapshot.getTimestamp("lastDonation");

                        if (lastDonationTimestamp == null) {
                            textEligibilityDays.setText("You are eligible to donate");
                            isBloodEligible = true;
                            ((TextView) findViewById(R.id.dateToDonate)).setText("No past donation found");
                            return;
                        }

                        Date lastDate = lastDonationTimestamp.toDate();
                        Calendar eligibleDate = Calendar.getInstance();
                        eligibleDate.setTime(lastDate);

                        if (gender != null && gender.equalsIgnoreCase("female")) {
                            eligibleDate.add(Calendar.MONTH, 4);
                        } else {
                            eligibleDate.add(Calendar.MONTH, 3);
                        }

                        // Set the eligible date text
                        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                        String formattedDate = sdf.format(eligibleDate.getTime());
                        ((TextView) findViewById(R.id.dateToDonate)).setText("You can again donate blood on " + formattedDate);

                        // Days left logic
                        Date today = new Date();
                        long millisDiff = eligibleDate.getTimeInMillis() - today.getTime();
                        daysLeft = millisDiff / (1000 * 60 * 60 * 24);

                        if (daysLeft <= 0) {
                            textEligibilityDays.setText("You are eligible to donate");
                            isBloodEligible = true;
                        } else {
                            textEligibilityDays.setText("Eligible in " + daysLeft + " days");
                            isBloodEligible = false;
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    textEligibilityDays.setText("Eligibility check failed");
                    isBloodEligible = false;
                    ((TextView) findViewById(R.id.dateToDonate)).setText("Could not load donation date");
                });
    }


    private void fetchAndSetUserName() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String name = documentSnapshot.getString("name");
                        if (name != null && !name.isEmpty()) {
                            welcomeText.setText("Welcome, " + name + "!");
                        } else {
                            welcomeText.setText("Welcome!");
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    welcomeText.setText("Welcome!");
                });
    }

    private void testNotificationWorkerNow() {
        OneTimeWorkRequest testWork =
                new OneTimeWorkRequest.Builder(DonationReminderWorker.class)
                        .build();

        WorkManager.getInstance(this).enqueue(testWork);
    }

}
