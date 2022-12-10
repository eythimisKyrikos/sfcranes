package com.safeline.safelinecranes.ui.resume;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SelectPositionForResumeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectPositionForResumeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Context mContext;
    private ArrayList<Position> mPositions;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private SelectSinglePositionAdapter mAdapter;
    private Button btnShowInspections;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SelectPositionForResumeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SelectPositionForResumeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SelectPositionForResumeFragment newInstance(String param1, String param2) {
        SelectPositionForResumeFragment fragment = new SelectPositionForResumeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_select_position_for_resume, container, false);

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
        btnShowInspections = view.findViewById(R.id.btn_position_list_insert);
        btnShowInspections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAdapter.getSelectedPosition() != null) {
                    Bundle args = new Bundle();
                    args.putParcelable("selectedPosition", mAdapter.getSelectedPosition().getPosition());
                    Navigation.findNavController(view).navigate(R.id.action_nav_resume_to_nav_resume_inspections, args);
                }
            }
        });
    }

    private void buildRecyclerView(View view) {
        mRecyclerView = view.findViewById(R.id.positions_select_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(mContext);
        mAdapter = new SelectSinglePositionAdapter(mPositions);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
}