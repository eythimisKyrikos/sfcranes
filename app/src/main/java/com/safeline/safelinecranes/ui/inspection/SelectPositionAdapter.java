package com.safeline.safelinecranes.ui.inspection;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.safeline.safelinecranes.R;
import com.safeline.safelinecranes.models.Position;
import com.safeline.safelinecranes.models.SelectPosition;

import java.util.ArrayList;

public class SelectPositionAdapter extends RecyclerView.Adapter<SelectPositionAdapter.SelectPositionViewHolder> {
    ArrayList<SelectPosition> mPositionList = new ArrayList<>();
    ArrayList<Position> selectedPositions = new ArrayList<>();

    public class SelectPositionViewHolder extends RecyclerView.ViewHolder {
        public TextView posTextView;
        public ImageView image;

        public SelectPositionViewHolder(@NonNull View itemView) {
            super(itemView);
            posTextView = itemView.findViewById(R.id.position_list_type_text_view);
            image = itemView.findViewById(R.id.imageViewSelection);
        }

        void bind(final SelectPosition selectedPosition) {
            image.setVisibility(selectedPosition.isSelected() ? View.VISIBLE : View.GONE);
            posTextView.setText(selectedPosition.getPosition().getName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedPosition.setSelected(!selectedPosition.isSelected());
                    image.setVisibility(selectedPosition.isSelected() ? View.VISIBLE : View.GONE);
                }
            });
        }
    }
    public ArrayList<Position> getAll() { return selectedPositions; }
    public ArrayList<Position> getSelectedPositions() {
        ArrayList<Position> selected = new ArrayList<>();
        for(int i=0; i<mPositionList.size(); i++) {
            if(mPositionList.get(i).isSelected()) {
                selected.add(mPositionList.get(i).getPosition());
            }
        }
        return selected;
    }
    public SelectPositionAdapter(ArrayList<Position> positionList) {
        for(int j=0; j<positionList.size(); j++) {
            SelectPosition spos = new SelectPosition();
            spos.setPosition(positionList.get(j));
            spos.setSelected(false);
            mPositionList.add(spos);
        }
    }
    @NonNull
    @Override
    public SelectPositionAdapter.SelectPositionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_position_item, parent, false);
        SelectPositionAdapter.SelectPositionViewHolder spvh = new SelectPositionAdapter.SelectPositionViewHolder(v);
        return spvh;
    }
    @Override
    public void onBindViewHolder(@NonNull SelectPositionAdapter.SelectPositionViewHolder holder, int position) {
        holder.bind(mPositionList.get(position));
    }
    @Override
    public int getItemCount() {
        return mPositionList.size();
    }
}
