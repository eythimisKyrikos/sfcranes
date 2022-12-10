package com.safeline.safelinecranes.ui.results;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.safeline.safelinecranes.R;
import com.safeline.safelinecranes.models.CardData;

import java.util.ArrayList;

import pl.pawelkleczkowski.customgauge.CustomGauge;

public class InspectionResultsAdapter extends RecyclerView.Adapter<InspectionResultsAdapter.InspectionResultsViewHolder> {
    private ArrayList<CardData> mCardData;
    private View v;
    private Context mContext;

    public static class InspectionResultsViewHolder extends RecyclerView.ViewHolder {
        public CustomGauge gaugeLvl;
        public TextView actions;
        public TextView header;

        public InspectionResultsViewHolder(@NonNull View itemView) {
            super(itemView);
            gaugeLvl = itemView.findViewById(R.id.gauge1);
            actions = itemView.findViewById(R.id.recommendations_text_view);
            header = itemView.findViewById(R.id.header_text_view);
        }
    }

    public InspectionResultsAdapter(ArrayList<CardData> cardDataList, Context context) {
        mCardData = cardDataList;
        this.mContext = context;
    }
    @NonNull
    @Override
    public InspectionResultsAdapter.InspectionResultsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_results_item, parent, false);
        InspectionResultsAdapter.InspectionResultsViewHolder rvh = new InspectionResultsAdapter.InspectionResultsViewHolder(v);
        return rvh;
    }
    @Override
    public void onBindViewHolder(@NonNull InspectionResultsAdapter.InspectionResultsViewHolder holder, int position) {
        CardData currentCardData = mCardData.get(position);
        holder.gaugeLvl.setValue(currentCardData.getGaugeLvl());
        holder.actions.setText(currentCardData.getAction());
        holder.header.setText(currentCardData.getHeader());
        if(currentCardData.getGaugeLvl() > 89) {
            v.setBackgroundColor(ContextCompat.getColor(mContext, R.color.light_red));
        } else if (currentCardData.getGaugeLvl() > 69 && currentCardData.getGaugeLvl() < 90) {
            v.setBackgroundColor(ContextCompat.getColor(mContext, R.color.light_orange));
        } else if (currentCardData.getGaugeLvl() > 34 && currentCardData.getGaugeLvl() < 69) {
            v.setBackgroundColor(ContextCompat.getColor(mContext, R.color.light_yellow));
        } else {
            v.setBackgroundColor(ContextCompat.getColor(mContext, R.color.light_green));
        }
    }
    @Override
    public int getItemCount() {
        return mCardData.size();
    }
}
