package com.safeline.safelinecranes.ui.products;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.safeline.safelinecranes.R;
import com.safeline.safelinecranes.models.RopeType;

import java.util.ArrayList;

public class RopeTypeAdapter extends RecyclerView.Adapter<RopeTypeAdapter.RopeTypeViewHolder>{

    private ArrayList<RopeType> mProductTypeList;
    private RopeTypeAdapter.OnItemClickListener mListener;
    public Context mContext;

    public void setOnItemClickListener(RopeTypeAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
    }

    public static class RopeTypeViewHolder extends RecyclerView.ViewHolder {
        public TextView modelTextView;
        public TextView typeTextView;
        public TextView diameterTextView;
        public TextView lengthTextView;
        public TextView manufacturerTextView;
        public TextView materialTextView;
        public ImageView mDeleteImage;

        public RopeTypeViewHolder(@NonNull View itemView, final RopeTypeAdapter.OnItemClickListener listener) {
            super(itemView);
            modelTextView = itemView.findViewById(R.id.product_list_type_model_text_view);
            typeTextView = itemView.findViewById(R.id.product_list_type_type_text_view_);
            diameterTextView = itemView.findViewById(R.id.product_list_type_diameter_text_view);
            lengthTextView = itemView.findViewById(R.id.product_list_type_length_text_view);
            manufacturerTextView = itemView.findViewById(R.id.product_list_type_manufacturer_text_view);
            materialTextView = itemView.findViewById(R.id.product_list_type_material_text_view);
            mDeleteImage = itemView.findViewById(R.id.btn_product_type_delete);

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

    public RopeTypeAdapter(ArrayList<RopeType> ropeTypeList, Context context) {
        mProductTypeList = ropeTypeList;
        mContext = context;
    }

    @NonNull
    @Override
    public RopeTypeAdapter.RopeTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rope_type_list_item, parent, false);
        RopeTypeViewHolder pvh = new RopeTypeViewHolder(v, mListener);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull RopeTypeAdapter.RopeTypeViewHolder holder, int position) {
        RopeType currentRopeType = mProductTypeList.get(position);
        holder.modelTextView.setText(currentRopeType.getModel());
        holder.typeTextView.setText(currentRopeType.getType());
        holder.diameterTextView.setText(String.valueOf(currentRopeType.getDiameter()));
        holder.lengthTextView.setText(String.valueOf(currentRopeType.getLength()));
        holder.manufacturerTextView.setText(currentRopeType.getManufacturer());
        holder.materialTextView.setText(currentRopeType.getMaterial());
    }

    @Override
    public int getItemCount() {
        return mProductTypeList.size();
    }
}

