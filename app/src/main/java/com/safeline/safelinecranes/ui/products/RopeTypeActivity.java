package com.safeline.safelinecranes.ui.products;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.safeline.safelinecranes.R;
import com.safeline.safelinecranes.models.RopeType;

public class RopeTypeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    TextView txtManufacturer;
    TextView txtModel;
    TextView txtDiameter;
    TextView txtLength;
    TextView txtBreakingForce;
    Spinner sMaterial;
    Intent mIintent;
    RopeType mPproductType;
    int mInitialPosition;
    Context mContext;
    boolean mEditMode;
    ArrayAdapter<CharSequence> materialAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rope_type);
        mContext = this;
        setTitle("Secondary Wire Type Info");
        mIintent = getIntent();
        mPproductType = mIintent.getParcelableExtra("RopeType");
        mInitialPosition = mIintent.getIntExtra("Position", 0);
        mEditMode = mIintent.getBooleanExtra("editMode", false);

        String type = mPproductType.getType();
        String manufacturer = mPproductType.getManufacturer();
        String model = mPproductType.getModel();
        float diameter = mPproductType.getDiameter();
        float length = mPproductType.getLength();
        float breakingForce = mPproductType.getBreakingForce();
        String material = mPproductType.getMaterial();
        txtManufacturer = findViewById(R.id.editText_product_manufacturer);
        txtManufacturer.setText(manufacturer);
        txtModel = findViewById(R.id.editText_product_model);
        txtModel.setText(model);
        txtDiameter = findViewById(R.id.editText_product_diameter);
        txtDiameter.setText(String.valueOf(diameter));
        txtLength = findViewById(R.id.editText_product_length);
        txtLength.setText(String.valueOf(length));
        txtBreakingForce = findViewById(R.id.editText_product_breaking_force);
        txtBreakingForce.setText(String.valueOf(breakingForce));

        sMaterial = findViewById(R.id.spinner_product_meterial);
        materialAdapter = ArrayAdapter.createFromResource(this, R.array.material, android.R.layout.simple_spinner_item);
        materialAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sMaterial.setAdapter(materialAdapter);
        sMaterial.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        sMaterial.setSelection(materialAdapter.getPosition(String.valueOf(material)));

        Button saveBtn = findViewById(R.id.button_product_save);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtManufacturer.getText().toString().trim().equals("") || txtModel.getText().toString().trim().equals("") ||
                        txtDiameter.getText().toString().trim().equals("") || txtLength.getText().toString().trim().equals("") ||
                        txtBreakingForce.getText().toString().trim().equals("")) {
                    new AlertDialog.Builder(mContext)
                            .setTitle("Add new Rope Type")
                            .setMessage("You must fill all fields in form.")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.ok, null).show();
                } else {
                    RopeType type = new RopeType();
                    type.setManufacturer(txtManufacturer.getText().toString());
                    type.setModel(txtModel.getText().toString());
                    type.setDiameter(Float.parseFloat(txtDiameter.getText().toString()));
                    type.setLength(Float.parseFloat(txtLength.getText().toString()));
                    type.setType(txtManufacturer.getText().toString() + " - " + txtModel.getText().toString() + " - " + Float.parseFloat(txtDiameter.getText().toString()));
                    type.setMaterial(sMaterial.getSelectedItem().toString());
                    type.setBreakingForce(Float.parseFloat(txtBreakingForce.getText().toString()));
                    type.setId(mPproductType.getId());
                    if(type.getType().equals("Wire")){
                        type.setMaterial("Wire");
                    }
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("RopeType", type);
                    resultIntent.putExtra("Position", mInitialPosition);
                    resultIntent.putExtra("editMode",mEditMode);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            }
        });

        Button cancelBtn = findViewById(R.id.buttoν_product_cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                RopeType ropeType = new RopeType();
                resultIntent.putExtra("RopeType",ropeType);
                resultIntent.putExtra("editMode",mEditMode);
                setResult(RESULT_CANCELED, resultIntent);
                finish();
            }
        });

    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
        if(text.equals("Wire")){
            sMaterial.setSelection(materialAdapter.getPosition("Steel"));
            sMaterial.setEnabled(false);
            sMaterial.setEnabled(false);
        } else {
            sMaterial.setEnabled(true);
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
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
}