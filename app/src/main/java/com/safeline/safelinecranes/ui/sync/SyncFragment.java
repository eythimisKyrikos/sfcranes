package com.safeline.safelinecranes.ui.sync;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.safeline.safelinecranes.R;
import com.safeline.safelinecranes.db.MigrationDbHelper;
import com.safeline.safelinecranes.models.DeletedObject;
import com.safeline.safelinecranes.models.FinishedResults;
import com.safeline.safelinecranes.models.Position;
import com.safeline.safelinecranes.models.Rope;
import com.safeline.safelinecranes.models.RopeType;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SyncFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SyncFragment extends Fragment implements View.OnClickListener{

    private Context mContext;

    private ImageView syncPositionIcon;
    private ImageView syncRopesIcon;
    private ImageView syncInspectionIcon;
    private ImageView syncDeletedIcon;
    private ImageView syncRopeTypesIcon;
    private Button syncBtn;

    public SyncFragment() { }

    public static SyncFragment newInstance(String param1, String param2) {
        SyncFragment fragment = new SyncFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sync, container, false);

        initView(view);
        loadSyncList(view);
        setButtons(view);

        return view;
    }

    private void initView(View view) {
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
        syncPositionIcon = view.findViewById(R.id.btn_sync_icon);
        syncRopesIcon = view.findViewById(R.id.ropes_sync_icon);
        syncRopeTypesIcon = view.findViewById(R.id.ropetypes_sync_icon);
        syncInspectionIcon = view.findViewById(R.id.inspection_sync_icon);
        syncDeletedIcon = view.findViewById(R.id.results_sync_icon);
        syncBtn  = view.findViewById(R.id.btn_sync);
    }

    private void loadSyncList(View view) {
        MigrationDbHelper dbHelper = MigrationDbHelper.getInstance(mContext);
        List<Position> posList = dbHelper.getAllNonSyncPositions();
        if(posList.size() > 0) {
            syncPositionIcon.setBackgroundResource(R.drawable.ic_non_sync);
        } else {
            syncPositionIcon.setBackgroundResource(R.drawable.ic_check_green);
        }

        List<RopeType> ropesTypes = dbHelper.getAllNonSyncRopeTypes();
        if(ropesTypes.size() > 0) {
            syncRopeTypesIcon.setBackgroundResource(R.drawable.ic_non_sync);
        } else {
            syncRopeTypesIcon.setBackgroundResource(R.drawable.ic_check_green);
        }

        List<Rope> ropes = dbHelper.getAllNonSyncRopes();
        if(ropes.size() > 0) {
            syncRopesIcon.setBackgroundResource(R.drawable.ic_non_sync);
        } else {
            syncRopesIcon.setBackgroundResource(R.drawable.ic_check_green);
        }

        List<DeletedObject> deletedItems = dbHelper.getDeletedObjects();
        if(deletedItems.size() > 0) {
            syncDeletedIcon.setBackgroundResource(R.drawable.ic_non_sync);
        } else {
            syncDeletedIcon.setBackgroundResource(R.drawable.ic_check_green);
        }

        List<FinishedResults> unSyncedResults = dbHelper.getUnsyncedFinishedResults();
        if(unSyncedResults.size() > 0) {
            syncInspectionIcon.setBackgroundResource(R.drawable.ic_non_sync);
        } else {
            syncInspectionIcon.setBackgroundResource(R.drawable.ic_check_green);
        }
    }

    private void setButtons(View view) {
        syncBtn = view.findViewById(R.id.btn_sync);
        syncBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        final LoadingDialog loadingDialog = new LoadingDialog(getActivity());
        loadingDialog.startingLoadingDialog();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingDialog.dismissDialog();
            }
        }, 15000);
        Intent intent = new Intent(mContext, BackgroundProcess.class);
        intent.putExtra(BackgroundProcess.BUNDLED_LISTENER, new ResultReceiver(new Handler()) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                super.onReceiveResult(resultCode, resultData);
                if (resultCode == Activity.RESULT_OK) {
                    boolean posSynced = resultData.getBoolean("positions");
                    if(posSynced) syncPositionIcon.setBackgroundResource(R.drawable.ic_check_green);
                    boolean ropesTypesSynced = resultData.getBoolean("rope_types");
                    if(ropesTypesSynced) syncRopeTypesIcon.setBackgroundResource(R.drawable.ic_check_green);
                    boolean ropesSynced = resultData.getBoolean("ropes");
                    if(ropesSynced) syncRopesIcon.setBackgroundResource(R.drawable.ic_check_green);
                    boolean results = resultData.getBoolean("resultDetails");
                    if(results) syncInspectionIcon.setBackgroundResource(R.drawable.ic_check_green);
                    boolean syncDeleted = resultData.getBoolean("deleted");
                    if(syncDeleted) syncDeletedIcon.setBackgroundResource(R.drawable.ic_check_green);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadingDialog.dismissDialog();
                        }
                    }, 0);
                    Toast.makeText(getActivity(), "Synchronization completed.", Toast.LENGTH_LONG).show();
                } else {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadingDialog.dismissDialog();
                        }
                    }, 0);
                    Toast.makeText(getActivity(), "Synchronization failed.", Toast.LENGTH_LONG).show();
                }
            }
        });
        mContext.startService(intent);
    }
}