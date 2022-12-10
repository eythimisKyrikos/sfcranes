package com.safeline.safelinecranes.ui.resume;

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

public class SelectSinglePositionAdapter extends RecyclerView.Adapter<SelectSinglePositionAdapter.SelectSinglePositionViewHolder>{

    ArrayList<SelectPosition> mPositionList = new ArrayList<>();
    int checkedPosition = 0; // -1: no default selection, 0: 1st item selected

    public class SelectSinglePositionViewHolder extends RecyclerView.ViewHolder {
        public TextView posTextView;
        public ImageView image;

        public SelectSinglePositionViewHolder(@NonNull View itemView) {
            super(itemView);
            posTextView = itemView.findViewById(R.id.position_list_type_text_view);
            image = itemView.findViewById(R.id.imageViewSelection);
        }

        void bind(final SelectPosition selectedPosition) {
            if(checkedPosition == -1) {
                image.setVisibility(View.GONE);
            } else {
                if(checkedPosition == getAdapterPosition()) {
                    image.setVisibility(View.VISIBLE);
                } else {
                    image.setVisibility(View.GONE);
                }
            }
            posTextView.setText(selectedPosition.getPosition().getName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    image.setVisibility(View.VISIBLE);
                    if(checkedPosition != getAdapterPosition()) {
                        notifyItemChanged(checkedPosition);
                        checkedPosition = getAdapterPosition();
                    }
                }
            });
        }
    }

    public SelectPosition getSelectedPosition() {
        if(checkedPosition != -1) {
            return mPositionList.get(checkedPosition);
        }
        return null;
    }
    public SelectSinglePositionAdapter(ArrayList<Position> positionList) {
        for(int j=0; j<positionList.size(); j++) {
            SelectPosition spos = new SelectPosition();
            spos.setPosition(positionList.get(j));
            spos.setSelected(false);
            mPositionList.add(spos);
        }
    }
    @NonNull
    @Override
    public SelectSinglePositionAdapter.SelectSinglePositionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_position_item, parent, false);
        SelectSinglePositionAdapter.SelectSinglePositionViewHolder spvh = new SelectSinglePositionAdapter.SelectSinglePositionViewHolder(v);
        return spvh;
    }
    @Override
    public void onBindViewHolder(@NonNull SelectSinglePositionAdapter.SelectSinglePositionViewHolder holder, int position) {
        holder.bind(mPositionList.get(position));
    }
    @Override
    public int getItemCount() {
        return mPositionList.size();
    }
}
