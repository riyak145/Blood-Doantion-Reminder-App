package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.widget.ImageView;

public class FooterHandler {

    public static void setupFooter(Activity activity) {
        ImageView home = activity.findViewById(R.id.icon_home);
        ImageView profile = activity.findViewById(R.id.icon_profile);
        ImageView settings = activity.findViewById(R.id.icon_settings);

        home.setOnClickListener(v -> {
            if (!(activity instanceof home)) {
                activity.startActivity(new Intent(activity, home.class));
                activity.finish();
            }
        });

        profile.setOnClickListener(v -> {
            if (!(activity instanceof ProfileActivity)) {
                activity.startActivity(new Intent(activity, ProfileActivity.class));
                activity.finish();
            }
        });

        settings.setOnClickListener(v -> {
            if (!(activity instanceof Settings)) {
                activity.startActivity(new Intent(activity, Settings.class));
                activity.finish();
            }
        });
    }
}
