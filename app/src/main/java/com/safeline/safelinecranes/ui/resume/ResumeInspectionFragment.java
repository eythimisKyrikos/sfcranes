package com.safeline.safelinecranes.ui.resume;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.safeline.safelinecranes.R;
import com.safeline.safelinecranes.db.MigrationDbHelper;
import com.safeline.safelinecranes.models.Inspection;
import com.safeline.safelinecranes.models.Position;
import com.safeline.safelinecranes.models.Rope;
import com.safeline.safelinecranes.ui.inspection.QuizActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ResumeInspectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResumeInspectionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Context mContext;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ResumeAdapter mAdapter;
    private ArrayList<Inspection> inspections;
    private Position mSelectedPosition;

    ActivityResultLauncher<Intent> quizActivityLanuncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
//                        mCurrentPosition = result.getData().getIntExtra("currentPosition", 0);
                        Inspection inspection =  result.getData().getParcelableExtra("inspection");
                        int ropeID = result.getData().getIntExtra("rope_id", 0);
                        inspection.setFinished(true);
                        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                        inspection.setDate_finished(df.format(new Date()));
                        MigrationDbHelper dbHelper = MigrationDbHelper.getInstance(mContext);
                        dbHelper.updateInspection(inspection);
                        Rope updatedRope = dbHelper.getRopeById(ropeID);
                        updatedRope.setWorkHours(updatedRope.getWorkHours() + inspection.getWorkHours());
                        dbHelper.updateRope(updatedRope);
//                        mCurrentPosition++;
//                        if(mCurrentPosition < mSelectedPositions.size()) {
//                            updateView(getView());
//                        } else {
//                            Bundle args = new Bundle();
////                            args.putParcelable("inspection", inspection);
//                            args.putParcelableArrayList("selectedPositions", mSelectedPosition);
//                            Navigation.findNavController(getView()).navigate(R.id.action_nav_new_inspection_parameters_to_nav_results, args);
//                        }
                    } else {
                        Toast.makeText(mContext, "Failed", Toast.LENGTH_LONG);
                    }
                }
            });


    public ResumeInspectionFragment() { }

    public static ResumeInspectionFragment newInstance(String param1, String param2) {
        ResumeInspectionFragment fragment = new ResumeInspectionFragment();
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
            mSelectedPosition = getArguments().getParcelable("selectedPosition");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_resume_inspection, container, false);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(view).navigate(R.id.action_nav_resume_inspections_to_nav_resume);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);
        loadPreviousInspections();
        buildRecyclerView(view);
        return view;
    }

    private void loadPreviousInspections() {
        MigrationDbHelper dbHelper = MigrationDbHelper.getInstance(mContext);
        List<Inspection> inspectionList = dbHelper.getAllInspectionsInPosition(mSelectedPosition);
        inspections = new ArrayList<>(inspectionList.size());
        inspections.addAll(inspectionList);
    }

    private void buildRecyclerView(View view) {
        mRecyclerView = view.findViewById(R.id.inspections_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(mContext);
        mAdapter = new ResumeAdapter(inspections, mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener((new ResumeAdapter.OnItemClickListener() {
            @Override
            public void onViewResultsClick(int position) {
//                ArrayList<Position> positions = new ArrayList<>();
//                positions.add(mSelectedPosition);
                Bundle args = new Bundle();
                args.putParcelable("selectedPosition", mSelectedPosition);
                args.putBoolean("in_view_mode", true);
                args.putParcelable("inspection",inspections.get(position));
                Navigation.findNavController(view).navigate(R.id.action_nav_resume_inspections_to_nav_results, args);
            }

            @Override
            public void onResumeClick(int position) {
                MigrationDbHelper dbHelper = MigrationDbHelper.getInstance(mContext);
                Inspection inspection = dbHelper.getLastInsertedInspectionByPosition(mSelectedPosition.getId());
                Intent resultIntent = new Intent(mContext, QuizActivity.class);
                resultIntent.putExtra("inspection", inspection);
                resultIntent.putExtra("currentPosition", mSelectedPosition);
                quizActivityLanuncher.launch(resultIntent);
            }
        }));
    }
}