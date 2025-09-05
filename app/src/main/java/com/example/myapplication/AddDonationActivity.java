package com.example.myapplication;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.utils.NotificationUtils;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddDonationActivity extends AppCompatActivity {

    EditText etDonationDate, etLocation, etNotes;
    Button btnSaveDonation;
    Date selectedDate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_donation);

        etDonationDate = findViewById(R.id.etDonationDate);
        etLocation = findViewById(R.id.etLocation);
        etNotes = findViewById(R.id.etNotes);
        btnSaveDonation = findViewById(R.id.btnSaveDonation);

        // Date Picker
        etDonationDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                calendar.set(year, month, dayOfMonth);
                selectedDate = calendar.getTime();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                etDonationDate.setText(sdf.format(selectedDate));
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        // Save Button Click
        btnSaveDonation.setOnClickListener(v -> {
            String location = etLocation.getText().toString().trim();
            String notes = etNotes.getText().toString().trim();

            if (selectedDate == null || location.isEmpty()) {
                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
                return;
            }

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                String uid = user.getUid();
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                Map<String, Object> donationData = new HashMap<>();
                donationData.put("date", new Timestamp(selectedDate));
                donationData.put("location", location);
                donationData.put("notes", notes);

                db.collection("users")
                        .document(uid)
                        .collection("donations")
                        .add(donationData)
                        .addOnSuccessListener(documentReference -> {
                            // Update last donation date
                            db.collection("users")
                                    .document(uid)
                                    .update("lastDonation", new Timestamp(selectedDate));

                            // ✅ Add Notification
                            String message = "Thank you for donating! You've saved lives again.";
                            NotificationUtils.saveNotificationToFirestore(uid, message);

                            Toast.makeText(this, "Donation saved successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Failed to save donation", Toast.LENGTH_SHORT).show();
                        });
            }
        });

    }
}
