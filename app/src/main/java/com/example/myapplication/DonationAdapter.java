package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class DonationAdapter extends RecyclerView.Adapter<DonationAdapter.ViewHolder> {

    private List<DonationItem> donationList;

    public DonationAdapter(List<DonationItem> donationList) {
        this.donationList = donationList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvLocation, tvNote;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.donationDate);
            tvLocation = itemView.findViewById(R.id.donationLocation);
            tvNote = itemView.findViewById(R.id.donationNote);
        }
    }

    @NonNull
    @Override
    public DonationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_donation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonationAdapter.ViewHolder holder, int position) {
        DonationItem item = donationList.get(position);

        // Handle null dates gracefully
        if (item.getDate() != null) {
            String formattedDate = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
                    .format(item.getDate());
            holder.tvDate.setText("Donated on " + formattedDate);
        } else {
            holder.tvDate.setText("Donation date not available");
        }

        holder.tvLocation.setText(item.getLocation() != null ? item.getLocation() : "Location not available");
        holder.tvNote.setText(item.getNotes() != null ? item.getNotes() : "No notes provided");
    }

    @Override
    public int getItemCount() {
        return donationList.size();
    }
}
