package com.example.myapplication;

import android.animation.ObjectAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AchievementAdapter extends RecyclerView.Adapter<AchievementAdapter.ViewHolder> {

    private List<AchievementItem> achievementList;

    public AchievementAdapter(List<AchievementItem> achievementList) {
        this.achievementList = achievementList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, percentage;
        ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textTitle);
            description = itemView.findViewById(R.id.textDescription);
            percentage = itemView.findViewById(R.id.textProgressLabel);
            progressBar = itemView.findViewById(R.id.progressAchievement);
        }
    }

    @NonNull
    @Override
    public AchievementAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_achievement, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AchievementItem item = achievementList.get(position);

        holder.title.setText(item.getTitle());
        holder.description.setText(item.getDescription());

        int percent = item.getPercentage();
        holder.percentage.setText(percent + "%");

        ObjectAnimator animation = ObjectAnimator.ofInt(holder.progressBar, "progress", 0, percent);
        animation.setDuration(1000);
        animation.start();
    }

    @Override
    public int getItemCount() {
        return achievementList.size();
    }
}
