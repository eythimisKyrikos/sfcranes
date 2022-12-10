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
import com.safeline.safelinecranes.db.MigrationDbHelper;
import com.safeline.safelinecranes.models.Position;
import com.safeline.safelinecranes.models.Rope;
import com.safeline.safelinecranes.models.RopeType;

import java.util.ArrayList;

public class RopeListAdapter extends RecyclerView.Adapter<RopeListAdapter.RopeViewHolder>{
    private ArrayList<Rope> mProductList;
    private RopeListAdapter.OnItemClickListener mListener;
    public Context mContext;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(RopeListAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public static class RopeViewHolder extends RecyclerView.ViewHolder {
        public TextView modelTextView;
        public TextView serialTextView;
        public TextView positionTextView;
        public ImageView mDeleteImage;


        public RopeViewHolder(@NonNull View itemView, final RopeListAdapter.OnItemClickListener listener) {
            super(itemView);
            modelTextView = itemView.findViewById(R.id.product_list_type_text_view);
            serialTextView = itemView.findViewById(R.id.rope_pos_list_serial_text_view);
            positionTextView = itemView.findViewById(R.id.rope_pos_list_position_text_view);
            mDeleteImage = itemView.findViewById(R.id.btn_product_delete);

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

    public RopeListAdapter(ArrayList<Rope> productList, Context context) {
        mProductList = productList;
        mContext = context;
    }

    @NonNull
    @Override
    public RopeListAdapter.RopeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rope_list_item, parent, false);
        RopeListAdapter.RopeViewHolder pvh = new RopeListAdapter.RopeViewHolder(v, mListener);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull RopeListAdapter.RopeViewHolder holder, int position) {
        MigrationDbHelper dbHelper = MigrationDbHelper.getInstance(this.mContext);
        Rope currentProduct = mProductList.get(position);
        Position pos = dbHelper.getPositionById(currentProduct.getPosition_id());
        RopeType typeOfRope = dbHelper.getRopeTypeById(currentProduct.getType_id());
        holder.serialTextView.setText(currentProduct.getSerialNr());
        holder.modelTextView.setText(String.valueOf(typeOfRope.getModel()));
        holder.positionTextView.setText(String.valueOf(pos.getName()));
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }
}

