package com.safeline.safelinecranes.ui.positions;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.safeline.safelinecranes.R;
import com.safeline.safelinecranes.models.Position;

public class AddPositionActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Intent mIintent;
    Position mPproductType;
    int mInitialPosition;
    boolean editMode;
    Context mContext;
    private TextView posName;
    private TextView addPositionHeader;
    private TextView positionType;
    private Button btnSave;
    private Button btnModifyPos;
    Spinner sStorage;
    private int x;
    private int y;
    private boolean isStorage;
    private String storageString;

    ActivityResultLauncher<Intent> scadaActivityLanuncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        activityResults(data);
                    } else {
                        Toast.makeText(getApplicationContext(), "Canceled", Toast.LENGTH_LONG).show();
                    }
                }
            });

    private void activityResults(Intent intent) {
        x = intent.getIntExtra("x", 0);
        y = intent.getIntExtra("y", 0);
        mPproductType.setX(x);
        mPproductType.setY(y);
        Toast.makeText(getApplicationContext(), "Mooring Plan updated", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_position);
        mContext = this;
        setTitle("Positions");
        mIintent = getIntent();
        editMode = mIintent.getBooleanExtra("editMode", false);
        addPositionHeader = findViewById(R.id.add_position_header);

        if(editMode) {
            addPositionHeader.setText("Edit Position");
        }
        mInitialPosition = mIintent.getIntExtra("position_in_list", 0);
        if(editMode){
            mPproductType = mIintent.getParcelableExtra("Position");
            x = mPproductType.getX();
            y = mPproductType.getY();
            isStorage = mPproductType.isStorage();

        } else {
            mPproductType = new Position();
            x = 0;
            y = 0;
            mPproductType.setX(x);
            mPproductType.setY(y);
            isStorage = false;
        }

        posName = findViewById(R.id.editText_position_name);
        posName.setText(mPproductType.getName());
        positionType = findViewById(R.id.editText_type);
        positionType.setText(mPproductType.getType_of_work());

        btnSave = findViewById(R.id.btn_save_pos);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(posName.getText().toString().trim().equals("")) {
                    new AlertDialog.Builder(mContext)
                            .setTitle("Add new Position")
                            .setMessage("You must give a position name.")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.ok, null).show();
                }
                else {
                    Position newPosition = new Position();
                    if(editMode){
                        newPosition.setId(mPproductType.getId());
                    }
                    if(posName.getText().toString().length() == 0){
                        newPosition.setName("Untitled Position" + mInitialPosition);
                    } else {
                        newPosition.setName(posName.getText().toString());
                    }
                    newPosition.setX(x);
                    newPosition.setY(y);
                    newPosition.setSync(false);
                    newPosition.setType_of_work(positionType.getText().toString());
                    if(sStorage.getSelectedItem().toString().equals("Yes")) isStorage = true; else isStorage = false;
                    newPosition.setStorage(isStorage);
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("Position", newPosition);
                    resultIntent.putExtra("position_in_list", mInitialPosition);
                    resultIntent.putExtra("editMode", editMode);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            }
        });
        btnModifyPos = findViewById(R.id.btn_edit_pos);
        btnModifyPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPproductType.setName(posName.getText().toString());
                mPproductType.setX(x);
                mPproductType.setY(y);
                mPproductType.setType_of_work(positionType.getText().toString());
                if(sStorage.getSelectedItem().toString().equals("Yes")) isStorage = true; else isStorage = false;
                mPproductType.setStorage(isStorage);
                Intent mapIntent = new Intent(AddPositionActivity.this, PositionMapActivity.class);
                mapIntent.putExtra("Position", mPproductType);
                mapIntent.putExtra("editMode", editMode);
                scadaActivityLanuncher.launch(mapIntent);
            }
        });
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(ev.getAction() == MotionEvent.ACTION_DOWN){
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if(!outRect.contains((int)ev.getRawX(), (int)ev.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}