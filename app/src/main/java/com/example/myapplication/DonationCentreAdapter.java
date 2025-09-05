package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DonationCentreAdapter extends RecyclerView.Adapter<DonationCentreAdapter.ViewHolder> {

    private List<DonationCentre> centreList;

    public DonationCentreAdapter(List<DonationCentre> centreList) {
        this.centreList = centreList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvLocation, tvPhone;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.textCentreName);
            tvLocation = itemView.findViewById(R.id.textCentreLocation);
            tvPhone = itemView.findViewById(R.id.textCentrePhone);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_donation_centre, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DonationCentre item = centreList.get(position);
        holder.tvName.setText(item.getName());
        holder.tvLocation.setText(item.getLocation());

        List<String> phones = item.getPhone();
        if (phones != null && !phones.isEmpty()) {
            holder.tvPhone.setText(phones.get(0)); // Show first phone number (e.g., tollfree)
        } else {
            holder.tvPhone.setText("N/A");
        }
    }

    @Override
    public int getItemCount() {
        return centreList.size();
    }
}
