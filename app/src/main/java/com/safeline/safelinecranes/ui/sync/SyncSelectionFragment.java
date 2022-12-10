package com.safeline.safelinecranes.ui.sync;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.safeline.safelinecranes.R;
import com.safeline.safelinecranes.db.MigrationDbHelper;

public class SyncSelectionFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button syncButton;
    private Button getDataButton;
    private Context mContext;

    public SyncSelectionFragment() { }

    public static SyncSelectionFragment newInstance(String param1, String param2) {
        SyncSelectionFragment fragment = new SyncSelectionFragment();
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
        View view = inflater.inflate(R.layout.fragment_sync_selection, container, false);

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

        syncButton = view.findViewById(R.id.btn_sync);
        getDataButton = view.findViewById(R.id.btn_get_data);

        syncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_nav_sync_options_to_nav_sync);
            }
        });

        getDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_nav_sync_options_to_nav_get_data);
            }
        });

        getDataButton.setEnabled(isEmptyDatabase());

        return view;
    }

    private boolean isEmptyDatabase(){
        MigrationDbHelper dbHelper = MigrationDbHelper.getInstance(this.mContext);
        if(dbHelper.getAllPositions().size() > 0) {
            return false;
        } else if (dbHelper.getAllRopeTypes().size() > 0) {
            return false;
        } else if (dbHelper.getAllRopes().size() > 0) {
            return false;
        } else if (dbHelper.getAllInspections().size() > 0) {
            return false;
        } else if (dbHelper.getFinishedResults().size() >0) {
            return false;
        } else return true;
    }
}