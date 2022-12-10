package com.safeline.safelinecranes.ui.retire;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.safeline.safelinecranes.R;
import com.safeline.safelinecranes.db.MigrationDbHelper;
import com.safeline.safelinecranes.models.Rope;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RopeRetireDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RopeRetireDetailsFragment extends Fragment {

    private static final String ROPE = "Rope";
    private static final String POSITION = "Position";

    private int mPosition;
    private Rope mRope;
    private Context mContext;
    private Button btnSave;
    private Button btnCancel;
    private TextView txtRetireDate;
    private CheckBox hasRetire;

    final Calendar retireCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener retireDateSelected;

    public RopeRetireDetailsFragment() { }

    public static RopeRetireDetailsFragment newInstance(String param1, String param2) {
        RopeRetireDetailsFragment fragment = new RopeRetireDetailsFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
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
            mPosition = getArguments().getInt(POSITION);
            mRope = getArguments().getParcelable(ROPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rope_retire_details, container, false);

        initView(view);
        setButtons(view);
        return view;
    }

    private void initView (View view) {
        hasRetire = view.findViewById(R.id.checkBox);
        hasRetire.setChecked(mRope.isHasRetire());
        hasRetire.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                txtRetireDate.setEnabled(isChecked);
            }
        });

        txtRetireDate = view.findViewById(R.id.retire_date_picker);
        txtRetireDate.setText(mRope.getRetireDate());
        txtRetireDate.setEnabled(mRope.isHasRetire());

        retireDateSelected = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                retireCalendar.set(Calendar.YEAR, year);
                retireCalendar.set(Calendar.MONTH, monthOfYear);
                retireCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateRetireLabel();
            }
        };
        txtRetireDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog retireDatePicker = new DatePickerDialog(mContext, retireDateSelected, retireCalendar
                        .get(Calendar.YEAR), retireCalendar.get(Calendar.MONTH),
                        retireCalendar.get(Calendar.DAY_OF_MONTH));
                retireDatePicker.show();
            }
        });
    }

    private void setButtons (View view) {
        btnCancel = view.findViewById(R.id.button_return);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_nav_retirement_details_to_nav_retirement);
            }
        });

        btnSave = view.findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hasRetire.isChecked()) {
                    // the rope has been retired
                    if(txtRetireDate.getText().toString().trim().equals("")){
                        // retired rope no date of retirement
                        new AlertDialog.Builder(mContext)
                                .setTitle("Wire retirement")
                                .setMessage("You must give a retirement date if you want to retire the wire.")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton(android.R.string.ok, null).show();
                        return;
                    } else {
                        // retired rope with date of retirement
                        updateRope(mRope);
                    }
                } else {
                    // non retired rope - remove date of retirement
                    txtRetireDate.setText("");
                    updateRope(mRope);
                }
                Bundle args = new Bundle();
                args.putInt(POSITION, mPosition);
                args.putParcelable(ROPE, mRope);
                Navigation.findNavController(view).navigate(R.id.action_nav_retirement_details_to_nav_retirement, args);
            }
        });
    }

    private void updateRetireLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        txtRetireDate.setText(sdf.format(retireCalendar.getTime()));
    }

    private void updateRope(Rope rope) {
        rope.setHasRetire(hasRetire.isChecked());
        rope.setRetireDate(txtRetireDate.getText().toString());
        rope.setSync(false);
        MigrationDbHelper dbHelper = MigrationDbHelper.getInstance(mContext);
        dbHelper.updateRope(mRope);
    }
}