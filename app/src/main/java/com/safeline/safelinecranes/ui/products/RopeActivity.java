package com.safeline.safelinecranes.ui.products;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.safeline.safelinecranes.db.MigrationDbHelper;
import com.safeline.safelinecranes.models.Position;
import com.safeline.safelinecranes.models.Rope;
import com.safeline.safelinecranes.models.RopeType;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RopeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Context mContext;
    Intent mIintent;
    Rope mRope;
    Position mPosition;
    boolean mEditMode;
    int mInitialPosition;

    List<RopeType> mTypes;
    List<Position> mPositions;
    byte[] mCertificateImage;

    Spinner sType;
    Spinner sPosition;
    TextView txtSerial;
    TextView txtCertificateDate;
    TextView txtWorkHours;
    TextView nfcText;

    TextView lblType;
    TextView lblModel;
    TextView lnlManufacturer;
    TextView lblDiameter;
    TextView lblLength;
    TextView lblMaterial;
    TextView lblBreakForce;

    Button saveButton;
    Button cancelButton;
    Button certButton;

    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;

    final Calendar certificateCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener certificateDateSelected;

    // list of NFC technologies detected:
    private final String[][] techList = new String[][] {
            new String[] {
                    NfcA.class.getName(),
                    NfcB.class.getName(),
                    NfcF.class.getName(),
                    NfcV.class.getName(),
                    IsoDep.class.getName(),
                    MifareClassic.class.getName(),
                    MifareUltralight.class.getName(), Ndef.class.getName()
            }
    };
    private ActivityResultLauncher<Intent> certificateActivityLanuncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        certificateActivityResults(data);
                    } else {
                        Toast.makeText(mContext, "Canceled", Toast.LENGTH_LONG);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rope);
        setTitle("Secondary Wire Info");
        mContext = this;
        mIintent = getIntent();
        mInitialPosition = mIintent.getIntExtra("Position", 0);
        mRope = mIintent.getParcelableExtra("Rope");
        mEditMode = mIintent.getBooleanExtra("editMode", false);

        initView();
        initButtons();
        initNfcAdapter();
//        MigrationDbHelper dbHelper = MigrationDbHelper.getInstance(this);
//        mRope = dbHelper.getRopeById(mRope.getId());
//        mCertificateImage = mRope.getByteImage();
    }

    private void initView(){
        sType = findViewById(R.id.spinner1_type);
        MigrationDbHelper dbHelper = MigrationDbHelper.getInstance(this);
        mTypes = dbHelper.getAllRopeTypes();
        ArrayAdapter<RopeType> adapterRopeType = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mTypes);
        adapterRopeType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sType.setAdapter(adapterRopeType);
        sType.setOnItemSelectedListener(this);
        if(mEditMode) {
            RopeType rt = getRopeTypeById(mTypes, mRope.getType_id());
            sType.setSelection(adapterRopeType.getPosition(rt));
            sType.setEnabled(!mRope.isHasRetire());
        }

        txtSerial = findViewById(R.id.editText_serial);
        if(mEditMode) {
            txtSerial.setText(mRope.getSerialNr());
            txtSerial.setEnabled(!mRope.isHasRetire());
        }

        txtCertificateDate = findViewById(R.id.editText_product_certificate_date);
        if(mEditMode) {
            txtCertificateDate.setText(mRope.getDate());
            txtCertificateDate.setEnabled(!mRope.isHasRetire());
        }
        certificateDateSelected = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                certificateCalendar.set(Calendar.YEAR, year);
                certificateCalendar.set(Calendar.MONTH, monthOfYear);
                certificateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        txtCertificateDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(RopeActivity.this, certificateDateSelected, certificateCalendar
                        .get(Calendar.YEAR), certificateCalendar.get(Calendar.MONTH),
                        certificateCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        txtWorkHours = findViewById(R.id.editText_product_work_hours);
        if(mEditMode) {
            txtWorkHours.setText(String.valueOf(mRope.getWorkHours()));
        }

        sPosition = findViewById(R.id.spinner_position);
        mPositions = dbHelper.getAllPositions();
        ArrayAdapter<Position> adapterPosition = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mPositions);
        adapterPosition.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sPosition.setAdapter(adapterPosition);
        sPosition.setOnItemSelectedListener(this);
        if(mEditMode) {
            Position pos = getPositionById(mRope.getPosition_id());
            int positionPos = adapterPosition.getPosition(pos);
            sPosition.setSelection(adapterPosition.getPosition(pos));
            System.out.println(positionPos);
        }

        nfcText = findViewById(R.id.editText_nfc);
        if(nfcAdapter == null) {
            nfcText.setEnabled(false);
        } else {
            nfcText.setEnabled(true);
            nfcText.setText(mRope.getTag());
        }


        lblType = findViewById(R.id.value_product_type);
        lblModel = findViewById(R.id.value_product_model);
        lnlManufacturer = findViewById(R.id.value_product_manufacturer);
        lblDiameter = findViewById(R.id.value_product_diameter);
        lblLength = findViewById(R.id.value_product_length);
        lblMaterial = findViewById(R.id.value_product_material);
        lblBreakForce = findViewById(R.id.value_product_breakingForce);
    }
    private void initButtons(){
        saveButton = (Button) findViewById(R.id.button_rope_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtSerial.getText().toString().trim().equals("") || txtCertificateDate.getText().toString().trim().equals("") ||
                        txtWorkHours.getText().toString().trim().equals("")) {
                    new AlertDialog.Builder(mContext)
                            .setTitle("Add new Rope")
                            .setMessage("You must fill all fields in form.")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.ok, null).show();
                } else if (mRope.getPosition_id() == null) {
                    new AlertDialog.Builder(mContext)
                            .setTitle("Add new Rope")
                            .setMessage("You must add a position to mooring line.")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.ok, null).show();
                } else {
                    createUpdateRope();
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("Rope", mRope);;
                    resultIntent.putExtra("Position", mInitialPosition);
                    resultIntent.putExtra("editMode", mEditMode);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            }
        });
        cancelButton = findViewById(R.id.button_rope_cancel);
        if(mRope.isHasRetire()){
            cancelButton.setText("Return");
        }
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                setResult(RESULT_CANCELED, resultIntent);
                finish();
            }
        });
        certButton = findViewById(R.id.btn_camera_cert);
        certButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MigrationDbHelper dbHelper = MigrationDbHelper.getInstance(mContext);
                Intent intent = new Intent(mContext, CertificateActivity.class);
                intent.putExtra("serialNumber", purgeSerialNumber(mRope.getSerialNr()));
                certificateActivityLanuncher.launch(intent);
            }
        });
    }
    private void initNfcAdapter() {
        //Initialise NfcAdapter
        NfcManager nfcManager = (NfcManager) getSystemService(Context.NFC_SERVICE);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        //If no NfcAdapter, display that the device has no NFC
        if (nfcAdapter == null){
            Toast.makeText(this,"NO NFC Capabilities",
                    Toast.LENGTH_SHORT).show();
            finish();
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity(this,0,new Intent(this,this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),PendingIntent.FLAG_MUTABLE);
        }
        else
        {
            pendingIntent = PendingIntent.getActivity(this,0,new Intent(this,this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),0);
        }

        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // creating intent receiver for NFC events:
        IntentFilter filter = new IntentFilter();
        filter.addAction(NfcAdapter.ACTION_TAG_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_TECH_DISCOVERED);
        // enabling foreground dispatch for getting intent from NFC event:
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, new IntentFilter[]{filter}, this.techList);

    }
    @Override
    protected void onPause() {
        super.onPause();
        if (nfcAdapter != null) {
            try {
                nfcAdapter.disableForegroundDispatch(this);
            } catch (IllegalStateException ex) {
                Log.e("ATHTAG", "Error disabling NFC foreground dispatch", ex);
            }
        }
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // Use NFC to read tag
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tag != null) {
            Toast.makeText(this,"NFC Tag was found.", Toast.LENGTH_LONG).show();
            System.out.println("Tag is: " + bytesToHexString(tag.getId()));
            nfcText.setText(bytesToHexString(tag.getId()));
        } else {
            Toast.makeText(this,"No NFC Tag was found.", Toast.LENGTH_LONG).show();
        }
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
        if (adapterView.getItemAtPosition(i) instanceof RopeType && adapterView.getItemAtPosition(i) != null) {
            lblType.setText(((RopeType) adapterView.getItemAtPosition(i)).getType());
            lblModel.setText(((RopeType) adapterView.getItemAtPosition(i)).getModel());
            lnlManufacturer.setText(((RopeType) adapterView.getItemAtPosition(i)).getManufacturer());
            lblDiameter.setText(String.valueOf(((RopeType) adapterView.getItemAtPosition(i)).getDiameter()));
            lblLength.setText(String.valueOf(((RopeType) adapterView.getItemAtPosition(i)).getLength()));
            lblMaterial.setText(((RopeType) adapterView.getItemAtPosition(i)).getMaterial());
            lblBreakForce.setText(String.valueOf(((RopeType) adapterView.getItemAtPosition(i)).getBreakingForce()));
        } else if (adapterView.getItemAtPosition(i) instanceof Position && adapterView.getItemAtPosition(i) != null) {
            mRope.setPosition_id(findPositionIdByName(adapterView.getItemAtPosition(i).toString()));
        } else {
            String text = adapterView.getItemAtPosition(i).toString();
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) { }

    private String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("0x");
        if (src == null || src.length <= 0) {
            return null;
        }
        char[] buffer = new char[2];
        int j = src.length;
        for (int i = 0; i < src.length; i++) {
            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
            System.out.println(buffer);
            stringBuilder.append(buffer);
        }
        return stringBuilder.toString();
    }
    private void certificateActivityResults(Intent intent) {
        mCertificateImage = intent.getByteArrayExtra("certificate");
        mRope.setByteImage(mCertificateImage);
        MigrationDbHelper dbHelper = MigrationDbHelper.getInstance(this);
        dbHelper.updateRope(mRope);
    }
    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        txtCertificateDate.setText(sdf.format(certificateCalendar.getTime()));
    }
    private int findTypeFromName(String name){
        for(RopeType rp : mTypes) {
            if ((rp.getType() + " " + rp.getModel()).equals(name)) {
                return rp.getId();
            }
        }
        return -1;
    }
    private int findPositionIdByName(String positionName) {
        for(Position pos : mPositions){
            if(pos.getName().equals(positionName)) {
                return pos.getId();
            }
        }
        return -1;
    }
    private RopeType getRopeTypeById(List<RopeType> list, int id){
        for(int i=0; i<list.size(); i++) {
            RopeType ropeType = list.get(i);
            if(ropeType.getId() == id){
                return ropeType;
            }
        }
        return null;
    }
    private Position getPositionById(int id){
        MigrationDbHelper dbHelper = MigrationDbHelper.getInstance(this);
        List<Position> list = dbHelper.getAllPositions();
        for(int i=0; i<list.size(); i++) {
            Position position = list.get(i);
            if(position.getId() == id){
                return position;
            }
        }
        return null;
    }
    private void createUpdateRope(){
        mRope.setSerialNr(txtSerial.getText().toString());
        mRope.setDate(txtCertificateDate.getText().toString());
        mRope.setWorkHours(Integer.parseInt(txtWorkHours.getText().toString()));
        mRope.setSync(false);
        mRope.setHasRetire(false);
        mRope.setTag(nfcText.getText().toString());
        if(findTypeFromName(sType.getSelectedItem().toString()) >= 0)
            mRope.setType_id(findTypeFromName(sType.getSelectedItem().toString()));
        if(findPositionIdByName(sPosition.getSelectedItem().toString()) >= 0)
            mRope.setPosition_id(findPositionIdByName(sPosition.getSelectedItem().toString()));
    }
    private String purgeSerialNumber(String serialNumber){
        return serialNumber.replace("/","@");
    }
}