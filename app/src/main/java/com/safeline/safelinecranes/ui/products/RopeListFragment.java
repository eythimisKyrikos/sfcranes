package com.safeline.safelinecranes.ui.products;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.safeline.safelinecranes.R;
import com.safeline.safelinecranes.db.Contract;
import com.safeline.safelinecranes.db.MigrationDbHelper;
import com.safeline.safelinecranes.models.Position;
import com.safeline.safelinecranes.models.Rope;

import java.util.ArrayList;
import java.util.List;

public class RopeListFragment extends Fragment {

    private RopeListAdapter mAdapter;
    ArrayList<Rope> ropeList;
    private Button buttonInsert;
    private Context mContext;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    private boolean returnResults;

    private ActivityResultLauncher<Intent> ropeActivityLanuncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        ropeActivityResults(data);
                    } else {
                        Toast.makeText(mContext, "Canceled", Toast.LENGTH_LONG);
                    }
                }
            });

    public RopeListFragment() { }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public static RopeListFragment newInstance(String param1, String param2) {
        RopeListFragment fragment = new RopeListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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
        View view = inflater.inflate(R.layout.fragment_rope_list, container, false);

        loadRopeList();
        buildRecyclerView(view);
        setButtons(view);

        if(returnResults) {
            mAdapter.notifyItemInserted(ropeList.size()-1);
        }

//        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
//            @Override
//            public void handleOnBackPressed() {
//                Navigation.findNavController(view).navigateUp();
//            }
//        };
//        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);

        return view;
    }

    public void loadRopeList() {
        MigrationDbHelper dbHelper = MigrationDbHelper.getInstance(mContext);
        List<Rope> ropes = dbHelper.getAllRopesAndTails();
        ropeList = new ArrayList<>(ropes.size());
        ropeList.addAll(ropes);
    }
    public void setButtons(View view) {
        buttonInsert = view.findViewById(R.id.btn_product_list_insert);
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, RopeActivity.class);
                intent.putExtra("Rope", new Rope());
                intent.putExtra("Position", ropeList.size()-1);
                intent.putExtra("editMode", false);
                ropeActivityLanuncher.launch(intent);
            }
        });
    }
    public void buildRecyclerView(View view) {
        mRecyclerView = view.findViewById(R.id.product_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(mContext);
        mAdapter = new RopeListAdapter(ropeList, mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new RopeListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (ropeList.get(position).isHasRetire()) {
                    new AlertDialog.Builder(mContext)
                            .setTitle("Modify Wire")
                            .setMessage("The wire you are trying to delete has been retired. You cannot modify the settings of a retired wire.")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.ok, null).show();
                } else {
                    Intent intent = new Intent(mContext, RopeActivity.class);
                    intent.putExtra("Rope", ropeList.get(position));
                    intent.putExtra("Position", position);
                    intent.putExtra("editMode", true);
                    ropeActivityLanuncher.launch(intent);
                }
            }

            @Override
            public void onDeleteClick(final int position) {
                MigrationDbHelper dbHelper = MigrationDbHelper.getInstance(mContext);
                Rope rope = dbHelper.getRopeById(ropeList.get(position).getId());
                if(rope.isHasRetire()) {
                    new AlertDialog.Builder(mContext)
                            .setTitle("Delete Rope")
                            .setMessage("If you delete rope with Serial Number " + rope.getSerialNr() + " all history data will be lost. Do you really want to continue?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    removeItem(position);
                                }})
                            .setNegativeButton(android.R.string.no, null).show();
                } else {
                    new AlertDialog.Builder(mContext)
                            .setTitle("Delete Rope")
                            .setMessage("The rope you are trying to delete has not been retired. You must first set rope to retirement before deleting it.")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.ok, null).show();
                }
            }
        });
    }

    public void updateItem(Rope rope, int position) {
        MigrationDbHelper dbHelper = MigrationDbHelper.getInstance(mContext);
        dbHelper.updateRope(rope);
        Rope newRope = ropeList.get(position);
        newRope.setId(rope.getId());
        newRope.setPosition_id(rope.getPosition_id());
        newRope.setType_id(rope.getType_id());
        newRope.setDate(rope.getDate());
        newRope.setWorkHours(rope.getWorkHours());
        newRope.setSerialNr(rope.getSerialNr());
        newRope.setRetireDate(rope.getRetireDate());
        newRope.setHasRetire(rope.isHasRetire());
        newRope.setByteImage(rope.getByteImage());
        newRope.setSync(false);
        ropeList.set(position, newRope);
        mAdapter.notifyItemChanged(position);
    }
    public void insertItem(Rope rope) {
        MigrationDbHelper dbHelper = MigrationDbHelper.getInstance(mContext);
        dbHelper.addRope(rope);
        ropeList.add(rope);
        mAdapter.notifyItemInserted(ropeList.size()-1);
    }
    public void removeItem(int position) {
        Rope rope = ropeList.get(position);
        if(rope.isSync()) {
            MigrationDbHelper dbHelper = MigrationDbHelper.getInstance(mContext);
            Position pos = dbHelper.getPositionById(rope.getPosition_id());
            String ropeType = dbHelper.getTypeofRope(rope.getType_id());
            dbHelper.addObjectToDelete(Contract.RopeTable.TABLE_NAME, rope.getId());
            dbHelper.deleteRope(rope);
            ropeList.remove(position);
            mAdapter.notifyItemRemoved(position);
        } else {
            new AlertDialog.Builder(mContext)
                    .setTitle("Delete Rope")
                    .setMessage("In order to delete this mooring line you must synchronize the data with the remote server first to update the remaining information for this mooring line.")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.ok, null).show();
        }
    }
    private void ropeActivityResults(Intent intent){
        Rope rope = intent.getParcelableExtra("Rope");
        boolean editMode = intent.getBooleanExtra("editMode", false);
        int position = intent.getIntExtra("Position", 0);
        if(editMode) {
            updateItem(rope, position);
        } else {
            insertItem(rope);
        }
    }
}