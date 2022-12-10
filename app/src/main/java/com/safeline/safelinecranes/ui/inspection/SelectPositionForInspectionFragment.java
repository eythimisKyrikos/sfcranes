package com.safeline.safelinecranes.ui.inspection;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.safeline.safelinecranes.R;
import com.safeline.safelinecranes.db.MigrationDbHelper;
import com.safeline.safelinecranes.models.Position;

import java.util.ArrayList;
import java.util.List;

public class SelectPositionForInspectionFragment extends Fragment {

    private Context mContext;
    private ArrayList<Position> mPositions;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private SelectPositionAdapter mAdapter;
    private Button btnStartInspection;

    public SelectPositionForInspectionFragment() { }


    public static SelectPositionForInspectionFragment newInstance(String param1, String param2) {
        SelectPositionForInspectionFragment fragment = new SelectPositionForInspectionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_select_position_for_inspection, container, false);

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( keyCode == KeyEvent.KEYCODE_BACK ) {

                    // leave this blank in order to disable the back press

                    return true;
                } else {
                    return false;
                }
            }
        });

        loadPositionList();
        buildRecyclerView(view);
        setButtons(view);

        return view;
    }

    private void loadPositionList() {
        MigrationDbHelper dbHelper = MigrationDbHelper.getInstance(mContext);
        List<Position> positions = dbHelper.getAllPositions();
        mPositions = new ArrayList<>(positions.size());
        mPositions.addAll(positions);
    }

    private void setButtons(View view) {
        btnStartInspection = view.findViewById(R.id.btn_position_list_insert);
        btnStartInspection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Position> selectedPositions = mAdapter.getSelectedPositions();
                if(selectedPositions.size() <= 0)
                new AlertDialog.Builder(mContext)
                        .setTitle("Select positions")
                        .setMessage("You must select choose at least one position in order to start an inspection.  ")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.ok, null).show();
                else {
                    Bundle args = new Bundle();
                    args.putParcelableArrayList("selectedPositions", selectedPositions);
                    Navigation.findNavController(view).navigate(R.id.action_nav_inspection_to_nav_new_inspection_parameters, args);
                }
            }
        });
    }

    private void buildRecyclerView(View view) {
        mRecyclerView = view.findViewById(R.id.positions_select_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(mContext);
        mAdapter = new SelectPositionAdapter(mPositions);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
}