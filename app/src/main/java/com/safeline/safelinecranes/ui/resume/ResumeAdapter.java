package com.safeline.safelinecranes.ui.resume;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.safeline.safelinecranes.R;
import com.safeline.safelinecranes.db.MigrationDbHelper;
import com.safeline.safelinecranes.models.Inspection;
import com.safeline.safelinecranes.models.Position;

import java.util.ArrayList;

public class ResumeAdapter extends RecyclerView.Adapter<ResumeAdapter.ResumeViewHolder> {

    private ArrayList<Inspection> mInspectionList;
    private ResumeAdapter.OnItemClickListener mListener;
    private Context mContext;

    public interface OnItemClickListener {
        void onViewResultsClick(int position);
        void onResumeClick(int position);
    }

    public void setOnItemClickListener(ResumeAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public static class ResumeViewHolder extends RecyclerView.ViewHolder {
        public TextView dateTextView;
        public Button viewDetailsBtn;
        public TextView typeTextView;
        public TextView locationTextView;
        public TextView finishedTextView;

        public ResumeViewHolder(@NonNull View itemView, final ResumeAdapter.OnItemClickListener listener) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.inspection_date);
            viewDetailsBtn = itemView.findViewById(R.id.btn_inspections_details);
            typeTextView = itemView.findViewById(R.id.inspection_type_text);
            locationTextView = itemView.findViewById(R.id.inspection_location_text);
            finishedTextView = itemView.findViewById(R.id.inspection_finished_text);

            viewDetailsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            if(viewDetailsBtn.getText().toString().equals("Resume")) {
                                listener.onResumeClick(position);
                            }
                            else {
                                listener.onViewResultsClick(position);
                            }
                        }
                    }
                }
            });
        }
    }

    public ResumeAdapter(ArrayList<Inspection> inspectionList, Context context) {
        mInspectionList = inspectionList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ResumeAdapter.ResumeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.resume_inspection_item, parent, false);
        ResumeAdapter.ResumeViewHolder rvh = new  ResumeAdapter.ResumeViewHolder(v, mListener);
        return rvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ResumeAdapter.ResumeViewHolder holder, int position) {
        Inspection currentInspection = mInspectionList.get(position);
        holder.dateTextView.setText(currentInspection.getDate_created());
        MigrationDbHelper dbHelper = MigrationDbHelper.getInstance(mContext);
        Position pos = dbHelper.getPositionById(currentInspection.getPosition());
        holder.typeTextView.setText(pos.getType_of_work());
        holder.locationTextView.setText(pos.getName());
        holder.viewDetailsBtn.setText(currentInspection.isFinished() ? "View" : "Resume");
        holder.finishedTextView.setText(currentInspection.isFinished() ? "YES" : "NO");
    }

    @Override
    public int getItemCount() {
        return mInspectionList.size();
    }
}