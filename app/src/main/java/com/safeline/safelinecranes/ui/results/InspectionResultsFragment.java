package com.safeline.safelinecranes.ui.results;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.safeline.safelinecranes.R;
import com.safeline.safelinecranes.db.MigrationDbHelper;
import com.safeline.safelinecranes.models.CardData;
import com.safeline.safelinecranes.models.FinalResult;
import com.safeline.safelinecranes.models.FinishedResults;
import com.safeline.safelinecranes.models.Inspection;
import com.safeline.safelinecranes.models.Position;
import com.safeline.safelinecranes.models.Rope;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InspectionResultsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InspectionResultsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Button returnButton;
    private Context mContext;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private InspectionResultsAdapter mAdapter;
    private FinalResult fResult;
    private ArrayList<CardData> resultList;
    private Inspection mInspection;
    private Position mPosition;
    private ArrayList<CardData> cardDataList;
    private boolean mInViewResults;
    private boolean mNfc;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public InspectionResultsFragment() { }

    public static InspectionResultsFragment newInstance(String param1, String param2) {
        InspectionResultsFragment fragment = new InspectionResultsFragment();
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
            mPosition = getArguments().getParcelable("selectedPosition");
            mInViewResults = getArguments().getBoolean("in_view_mode", false);
            mInspection = getArguments().getParcelable("inspection");
            mNfc = getArguments().getBoolean("nfc", false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inspection_results, container, false);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if(mNfc) {
                    Navigation.findNavController(getView()).navigate(R.id.action_nav_results_to_nav_inspection);
                } else {
                    Bundle args = new Bundle();
                    if(mPosition != null){
                        args.putParcelable("selectedPosition", mPosition);
                    } else {
                        MigrationDbHelper dbHelper = MigrationDbHelper.getInstance(mContext);
                        Position position = dbHelper.getPositionById(mInspection.getPosition());
                        args.putParcelable("selectedPosition", position);
                    }
                    Navigation.findNavController(view).navigate(R.id.action_nav_results_to_nav_resume_inspections, args);
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);

        loadFResult();
        setButtons(view);
        buildRecyclerView(view);

        return view;
    }

    private void setButtons(View view) {
        returnButton = view.findViewById(R.id.btn_resutls_return);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mInViewResults) {
                    Bundle args = new Bundle();
                    if(mPosition != null) {
                        args.putParcelable("selectedPosition", mPosition);
                    } else {
                        MigrationDbHelper dbHelper = MigrationDbHelper.getInstance(mContext);
                        Position position = dbHelper.getPositionById(mInspection.getPosition());
                        args.putParcelable("selectedPosition", position);
                    }
                    Navigation.findNavController(v).navigate(R.id.action_nav_results_to_nav_resume_inspections, args);
//                    Bundle args = new Bundle();
//                    MigrationDbHelper dbHelper = MigrationDbHelper.getInstance(mContext);
//                    Position position = dbHelper.getPositionById(mInspection.getPosition());
//                    args.putParcelable("selectedPosition", position);
//                    Navigation.findNavController(v).navigate(R.id.action_nav_results_to_nav_inspection, args);
                } else {
                    Navigation.findNavController(v).navigate(R.id.action_nav_results_to_nav_resume);
                }
            }
        });
    }
    private void loadFResult() {
        MigrationDbHelper dbHelper = MigrationDbHelper.getInstance(mContext);
        resultList = new ArrayList<>();
        FinishedResults results = dbHelper.getFinishedResultByInspection(mInspection.getId());
        Gson gson = new Gson();
        fResult= gson.fromJson(results.getResults(), FinalResult.class);
        fResult.setInspection(mInspection);
        ArrayList<CardData> cards = convertResults2CardData(fResult);
        for(int j=0; j< cards.size(); j++) {
            resultList.add(cards.get(j));
        }
    }


//    private void loadFResult() {
//        MigrationDbHelper dbHelper = MigrationDbHelper.getInstance(mContext);
//        resultList = new ArrayList<>();
//        for(int i=0; i<mPositions.size(); i++) {
//            Inspection mInspection = dbHelper.getLastInsertedInspectionByPosition(mPositions.get(i).getId());
//            FinishedResults results = dbHelper.getFinishedResultByInspection(mInspection.getId());
//            Inspection inspection = dbHelper.getInspectionById(mInspection.getId());
//            Gson gson = new Gson();
//            fResult= gson.fromJson(results.getResults(), FinalResult.class);
//            fResult.setInspection(mInspection);
//            ArrayList<CardData> cards = convertResults2CardData(fResult);
//            for(int j=0; j< cards.size(); j++) {
//                resultList.add(cards.get(j));
//            }
//        }
//    }

    private void buildRecyclerView(View view) {
        mRecyclerView = view.findViewById(R.id.results_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(mContext);
        mAdapter = new InspectionResultsAdapter(resultList, mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private ArrayList<CardData> convertResults2CardData(FinalResult fResult){
        MigrationDbHelper dbHelper = MigrationDbHelper.getInstance(mContext);
        cardDataList = new ArrayList<>();
        // set inspection condition results
        Inspection inspection = fResult.getInspection();
        Position position = dbHelper.getPositionById(fResult.getInspection().getPosition());
        Rope rope = fResult.getRope();

        List<FinalResult.Action> ropeActions = fResult.getActions();
        for(int k=0; k<ropeActions.size(); k++) {
            FinalResult.Action action = ropeActions.get(k);
            CardData cardDataRope = new CardData();
            cardDataRope.setGaugeLvl(action.getSeverity_level());
            cardDataRope.setAction(action.getAction());
            cardDataRope.setHeader("Position: " + position.getName() + " - Wire: " + rope.getSerialNr());
            cardDataList.add(cardDataRope);
        }
        return cardDataList;
    }
}