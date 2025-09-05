package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class Settings extends AppCompatActivity {

    private int[] iconResIds = {
            R.drawable.logo_account_prefernce,
            R.drawable.logo_about_us,
            R.drawable.logo_privacy_policy,
            R.drawable.logo_shield,
            R.drawable.logo_question_mark,
            R.drawable.logo_logout
    };

    private int[] titleResIds = {
            R.string.account_preferences,
            R.string.about_us,
            R.string.privacy_policy,
            R.string.terms_conditions,
            R.string.help_support,
            R.string.logout
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        FooterHandler.setupFooter(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Settings");

        LinearLayout settingsContainer = findViewById(R.id.settings_content);

        for (int i = 0; i < settingsContainer.getChildCount(); i++) {
            View itemView = settingsContainer.getChildAt(i);

            ImageView icon = itemView.findViewById(R.id.bar_item_icon);
            TextView title = itemView.findViewById(R.id.bar_item_title);

            // Set icon and title
            icon.setImageResource(iconResIds[i]);
            title.setText(getString(titleResIds[i]));

            // Optional: Add click listeners for future intent handling
            final int index = i;
            itemView.setOnClickListener(v -> {
                switch (index) {
                    case 0:
                        // Account & Preferences
                        startActivity(new Intent(Settings.this, AccountPreferencesActivity.class));
                        break;
                    case 1:
                        // About Us
                        startActivity(new Intent(Settings.this, AboutUsActivity.class));
                        break;
                    case 2:
                        // Privacy Policy
                        startActivity(new Intent(Settings.this, PrivacyPolicyActivity.class));
                        break;
                    case 3:
                        // Terms & Conditions
                        startActivity(new Intent(Settings.this, TermsConditionsActivity.class));
                        break;
                    case 4:
                        // Help & Support
                        startActivity(new Intent(Settings.this, HelpSupportActivity.class));
                        break;
                    case 5:
                        // Logout
                        FirebaseAuth.getInstance().signOut(); // Sign out from Firebase
                        Intent intent = new Intent(Settings.this, authChoice.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear back stack
                        startActivity(intent);
                        finish(); // Finish current activity
                        break;

                }
            });
        }
    }
}
