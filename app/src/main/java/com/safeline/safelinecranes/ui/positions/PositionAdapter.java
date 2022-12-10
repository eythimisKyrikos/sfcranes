package com.safeline.safelinecranes.ui.positions;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.safeline.safelinecranes.R;
import com.safeline.safelinecranes.models.Position;

import java.util.ArrayList;

public class PositionAdapter extends RecyclerView.Adapter<PositionAdapter.PositionViewHolder>{
    private ArrayList<Position> mPositionList;
    private PositionAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(PositionAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public static class PositionViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public ImageView mDeleteImage;

        public PositionViewHolder(@NonNull View itemView, final PositionAdapter.OnItemClickListener listener) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.position_list_type_text_view);
            mDeleteImage = itemView.findViewById(R.id.btn_position_delete);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            mDeleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }

    public PositionAdapter(ArrayList<Position> positionList) {
        mPositionList = positionList;
    }

    @NonNull
    @Override
    public PositionAdapter.PositionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.position_sync_list_item, parent, false);
        PositionAdapter.PositionViewHolder pvh = new PositionAdapter.PositionViewHolder(v, mListener);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull PositionAdapter.PositionViewHolder holder, int position) {
        Position currentPosition = mPositionList.get(position);
        holder.nameTextView.setText(currentPosition.getName());
    }

    @Override
    public int getItemCount() {
        return mPositionList.size();
    }
}
