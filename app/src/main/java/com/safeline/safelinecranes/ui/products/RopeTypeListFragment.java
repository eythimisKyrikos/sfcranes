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
import com.safeline.safelinecranes.db.Contract;
import com.safeline.safelinecranes.db.MigrationDbHelper;
import com.safeline.safelinecranes.models.Rope;
import com.safeline.safelinecranes.models.RopeType;

import java.util.ArrayList;
import java.util.List;

public class RopeTypeListFragment extends Fragment {

    private RopeTypeAdapter mAdapter;
    ArrayList<RopeType> ropeTypeList;
    private Button buttonInsert;
    private Context mContext;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    ActivityResultLauncher<Intent> ropeTypeActivityLanuncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        activityResults(data);
                    } else {
                        Toast.makeText(mContext, "Canceled", Toast.LENGTH_LONG);
                    }
                }
            });

    public RopeTypeListFragment() { }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public static RopeTypeListFragment newInstance(String param1, String param2) {
        RopeTypeListFragment fragment = new RopeTypeListFragment();
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
        View view =  inflater.inflate(R.layout.fragment_rope_type_list, container, false);
        loadRopeTypeList();
        buildRecyclerView(view);
        setButtons(view);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(view).navigateUp();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);

        return view;
    }

    public void loadRopeTypeList() {
        MigrationDbHelper dbHelper = MigrationDbHelper.getInstance(mContext);
        List<RopeType> types = dbHelper.getAllRopeTypes();
        ropeTypeList = new ArrayList<>(types.size());
        ropeTypeList.addAll(types);
    };


    public void insertItem(RopeType ropeType) {
        MigrationDbHelper dbHelper = MigrationDbHelper.getInstance(mContext);
        dbHelper.addRopeType(ropeType);
        ropeTypeList.add(ropeType);
        mAdapter.notifyItemInserted(ropeTypeList.size()-1);
    }

    public void updateItem(RopeType ropeType, int position) {
        MigrationDbHelper dbHelper = MigrationDbHelper.getInstance(mContext);
        dbHelper.updateRopeType(ropeType);
        RopeType type = ropeTypeList.get(position);
        type.setId(ropeType.getId());
        type.setBreakingForce(ropeType.getBreakingForce());
        type.setDiameter(ropeType.getDiameter());
        type.setLength(ropeType.getLength());
        type.setManufacturer(ropeType.getManufacturer());
        type.setMaterial(ropeType.getMaterial());
        type.setModel(ropeType.getModel());
        type.setType(ropeType.getType());
        ropeTypeList.set(position, type);
        mAdapter.notifyItemChanged(position);
    }

    public void removeItem(int position) {
        RopeType type = ropeTypeList.get(position);
        MigrationDbHelper dbHelper = MigrationDbHelper.getInstance(mContext);
        dbHelper.addObjectToDelete(Contract.RopeTypeTable.TABLE_NAME, type.getId());
        dbHelper.deleteRopeType(type.getId());
        ropeTypeList.remove(position);
        mAdapter.notifyItemRemoved(position);
    }

    public void setButtons(View view) {
        buttonInsert = view.findViewById(R.id.btn_rope_type_insert);
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, RopeTypeActivity.class);
                intent.putExtra("RopeType", new RopeType());
                intent.putExtra("Position", ropeTypeList.size()-1);
                intent.putExtra("editMode", false);
                ropeTypeActivityLanuncher.launch(intent);
            }
        });
    }

    public void buildRecyclerView(View view) {
        mRecyclerView = view.findViewById(R.id.product_type_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(mContext);
        mAdapter = new RopeTypeAdapter(ropeTypeList, mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new RopeTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(mContext, RopeTypeActivity.class);
                intent.putExtra("RopeType", ropeTypeList.get(position));
                intent.putExtra("Position", position);
                intent.putExtra("editMode", true);
                ropeTypeActivityLanuncher.launch(intent);
            }

            @Override
            public void onDeleteClick(final int position) {
                RopeType type = ropeTypeList.get(position);
                if(hasRopes(type)) {
                    new AlertDialog.Builder(mContext)
                            .setTitle("Delete Rope")
                            .setMessage("You cannot delete the type of mooring line " + type.getModel() + " because there are associated ropes with it. Please delete all mooring lines first.")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.ok, null).show();
                } else {
                    if(type.isSync()){
                        new AlertDialog.Builder(mContext)
                                .setTitle("Delete Rope")
                                .setMessage("Do you want to delete mooring line type " + type.getModel() + "?")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        removeItem(position);
                                    }})
                                .setNegativeButton(android.R.string.no, null).show();
                    } else {
                        new AlertDialog.Builder(mContext)
                                .setTitle("Delete Rope")
                                .setMessage("In order to delete this mooring line type you must synchronize the data with the remote server first to update the remaining information.")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton(android.R.string.ok, null).show();
                    }
                }
            }
        });
    }
    private void activityResults(Intent intent){
        boolean editMode = intent.getBooleanExtra("editMode", false);
        if(editMode) {
            int position = intent.getIntExtra("position_in_list", 0);
            RopeType type = intent.getParcelableExtra("RopeType");
            updateItem(type, position);
            mAdapter.notifyItemChanged(position);
            Toast.makeText(mContext, "Saved", Toast.LENGTH_LONG);
        } else {
            RopeType type = intent.getParcelableExtra("RopeType");
            insertItem(type);
            Toast.makeText(mContext, "Saved", Toast.LENGTH_LONG);
        }
    }
    private boolean hasRopes(RopeType type) {
        MigrationDbHelper dbHelper = MigrationDbHelper.getInstance(mContext);
        List<Rope> ropeList = dbHelper.getRopesByRopeType(type);
        if(ropeList.size() > 0) {
            return true;
        } else return false;
    }
}