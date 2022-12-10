package com.safeline.safelinecranes.ui.positions;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
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

public class PositionListFragment extends Fragment {

    private PositionAdapter mAdapter;
    ArrayList<Position> positions;
    private Button buttonInsert;

    private Context mContext;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    ActivityResultLauncher<Intent> addPositionActivityLanuncher = registerForActivityResult(
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

    public PositionListFragment() { }

    public static PositionListFragment newInstance() {
        PositionListFragment fragment = new PositionListFragment();
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
                             Bundle savedInstanceState){
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_position_list, container, false);
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
        List<Position> posList = dbHelper.getAllPositionsWithStorage();
        positions = new ArrayList<>(posList.size());
        positions.addAll(posList);
    }
    private void setButtons(View view) {
        buttonInsert = view.findViewById(R.id.btn_position_list_insert);
        buttonInsert.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, AddPositionActivity.class);
                intent.putExtra("Position", new Position());
                intent.putExtra("position_in_list", positions.size()-1);
                intent.putExtra("editMode", false);
                addPositionActivityLanuncher.launch(intent);
            }
        });
    }
    private void buildRecyclerView(View view) {
        mRecyclerView = view.findViewById(R.id.position_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(mContext);
        mAdapter = new PositionAdapter(positions);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new PositionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                MigrationDbHelper dbHelper = MigrationDbHelper.getInstance(mContext);
                Position editedPosition = dbHelper.getPositionByName(positions.get(position).getName());
                Intent intent = new Intent(mContext, AddPositionActivity.class);
                intent.putExtra("Position", editedPosition);
                intent.putExtra("position_in_list", position);
                intent.putExtra("editMode", true);
                addPositionActivityLanuncher.launch(intent);
            }

            @Override
            public void onDeleteClick(final int position) {
                Position pos = positions.get(position);
                if(hasRopes(pos)) {
                    new AlertDialog.Builder(mContext)
                            .setTitle("Delete Position")
                            .setMessage("There are ropes in this position. If you want to remove this position you should remove all ropes first.")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setNeutralButton(android.R.string.ok, null).show();
                } else {
                    if(pos.isSync()){
                        new AlertDialog.Builder(mContext)
                                .setTitle("Delete Position")
                                .setMessage("If you delete position " + pos.getName() + " all history data will be lost. Do you really want to continue?")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        removeItem(position);
                                    }})
                                .setNegativeButton(android.R.string.no, null).show();
                    } else {
                        new AlertDialog.Builder(mContext)
                                .setTitle("Delete Position")
                                .setMessage("You cannot delete this position. You must synchronize the data first in order to sent all the information.")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setNeutralButton(android.R.string.ok, null).show();
                    }
                }
            }
        });
    }

    private void removeItem(int position) {
        Position pos = positions.get(position);
        MigrationDbHelper dbHelper = MigrationDbHelper.getInstance(mContext);
        dbHelper.addObjectToDelete(Contract.PositionTable.TABLE_NAME, pos.getId());
        dbHelper.deletePosition(pos);
        positions.remove(pos);
        mAdapter.notifyItemRemoved(position);
    }
    private void updateItem(Position newPos, int position) {
        MigrationDbHelper dbHelper = MigrationDbHelper.getInstance(mContext);
        dbHelper.updatePosition(newPos);
        Position newPosition = positions.get(position);
        newPosition.setId(newPos.getId());
        newPosition.setName(newPos.getName());
        newPosition.setX(newPos.getX());
        newPosition.setY(newPos.getY());
        positions.set(position, newPosition);
    }
    public void insertItem(Position newPos) {
        MigrationDbHelper dbHelper = MigrationDbHelper.getInstance(mContext);
        dbHelper.addPosition(newPos);
        positions.add(newPos);
        Position newPosition = dbHelper.getPositionByName(newPos.getName());
        mAdapter.notifyItemInserted(positions.size()-1);
    }
    private boolean hasRopes(Position position) {
        MigrationDbHelper dbHelper = MigrationDbHelper.getInstance(mContext);
        List<Rope> ropes = dbHelper.getRopesByPositionForDetailed(position);
        if(ropes.size() > 0) {
            return true;
        } else {
            return false;
        }
    }
    private void activityResults(Intent intent){
        boolean editMode = intent.getBooleanExtra("editMode", false);
        if(editMode) {
            int position = intent.getIntExtra("position_in_list", 0);
            Position pos = intent.getParcelableExtra("Position");
            updateItem(pos, position);
            mAdapter.notifyItemChanged(position);
            Toast.makeText(mContext, "Saved", Toast.LENGTH_LONG);
        } else {
            Position pos = intent.getParcelableExtra("Position");
            insertItem(pos);
            Toast.makeText(mContext, "Saved", Toast.LENGTH_LONG);
        }
    }
}