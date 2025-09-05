package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditProfileActivity extends AppCompatActivity {

    private EditText editTextName, editTextEmail;
    private TextView toolbarTitle;
    private Spinner spinnerBloodGroup;
    private RadioGroup radioGroupGender;
    private RadioButton radioMale, radioFemale, radioOther;
    private Button buttonUpdate;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Edit Profile");
        // Initialize views
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        spinnerBloodGroup = findViewById(R.id.spinnerBloodGroup);
        radioGroupGender = findViewById(R.id.radioGroupGender);
        radioMale = findViewById(R.id.radioMale);
        radioFemale = findViewById(R.id.radioFemale);
        radioOther = findViewById(R.id.radioOther);
        buttonUpdate = findViewById(R.id.buttonUpdate);

        loadUserData();

        buttonUpdate.setOnClickListener(v -> updateUserProfile());
    }

    private void loadUserData() {
        db.collection("users").document(uid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        editTextName.setText(documentSnapshot.getString("name"));
                        editTextEmail.setText(documentSnapshot.getString("email")); // usually not editable

                        // Blood group
                        String bloodGroup = documentSnapshot.getString("bloodGroup");
                        String[] bloodGroupsArray = getResources().getStringArray(R.array.blood_groups);
                        for (int i = 0; i < bloodGroupsArray.length; i++) {
                            if (bloodGroupsArray[i].equalsIgnoreCase(bloodGroup)) {
                                spinnerBloodGroup.setSelection(i);
                                break;
                            }
                        }

                        // Gender
                        String gender = documentSnapshot.getString("gender");
                        if (gender != null) {
                            switch (gender.toLowerCase()) {
                                case "male":
                                    radioMale.setChecked(true);
                                    break;
                                case "female":
                                    radioFemale.setChecked(true);
                                    break;
                                case "other":
                                    radioOther.setChecked(true);
                                    break;
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to load user data", Toast.LENGTH_SHORT).show());
    }

    private void updateUserProfile() {
        String name = editTextName.getText().toString().trim();
        String bloodGroup = spinnerBloodGroup.getSelectedItem().toString();
        String gender = getSelectedGender();

        if (name.isEmpty() || gender.isEmpty()) {
            Toast.makeText(this, "Name and gender are required", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("users").document(uid)
                .update("name", name,
                        "bloodGroup", bloodGroup,
                        "gender", gender)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show();
                    // Redirect to Profile screen (if exists), otherwise Home
                    startActivity(new Intent(EditProfileActivity.this, ProfileActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show());
    }

    private String getSelectedGender() {
        int selectedId = radioGroupGender.getCheckedRadioButtonId();
        if (selectedId == R.id.radioMale) return "Male";
        if (selectedId == R.id.radioFemale) return "Female";
        if (selectedId == R.id.radioOther) return "Other";
        return "";
    }
}
