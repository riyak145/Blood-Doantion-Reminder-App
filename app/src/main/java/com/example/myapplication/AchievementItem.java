package com.example.myapplication;

public class AchievementItem {
    private String title;
    private String description;
    private int currentProgress;
    private int target;

    public AchievementItem(String title, String description, int currentProgress, int target) {
        this.title = title;
        this.description = description;
        this.currentProgress = currentProgress;
        this.target = target;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getCurrentProgress() {
        return currentProgress;
    }

    public int getTarget() {
        return target;
    }

    public int getPercentage() {
        return Math.min(100, (int) ((currentProgress * 100.0f) / target));
    }
}
