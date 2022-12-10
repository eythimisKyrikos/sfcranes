package com.safeline.safelinecranes.ui.inspection;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.safeline.safelinecranes.R;
import com.safeline.safelinecranes.db.MigrationDbHelper;
import com.safeline.safelinecranes.models.Inspection;
import com.safeline.safelinecranes.models.Position;
import com.safeline.safelinecranes.models.Question;
import com.safeline.safelinecranes.models.Result;
import com.safeline.safelinecranes.models.Rope;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class InitializeInspectionFragment extends Fragment {

    private static final String POSITIONS = "selectedPositions";
    private static final String CURRENT_POSITION = "currentPosition";

    private Context mContext;
    private static ArrayList<Position> mSelectedPositions;
    private int mCurrentPosition;
    private boolean mNfc;

    private Button continueBtn;
    private Button cancelBtn;
    private TextView dateTxt;
    private TextView whTxt;
    private TextView positionName;
    private TextView wireSerial;

    final Calendar inspectionCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener inspectionDateSelected;

    ActivityResultLauncher<Intent> quizActivityLanuncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        mCurrentPosition = result.getData().getIntExtra("currentPosition", 0);
                        Inspection inspection =  result.getData().getParcelableExtra("inspection");
                        int ropeID = result.getData().getIntExtra("rope_id", 0);
                        inspection.setFinished(true);
                        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                        inspection.setDate_finished(df.format(new Date()));
                        MigrationDbHelper dbHelper = MigrationDbHelper.getInstance(mContext);
                        dbHelper.updateInspection(inspection);
                        Rope updatedRope = dbHelper.getRopeById(ropeID);
                        updatedRope.setWorkHours(updatedRope.getWorkHours() + inspection.getWorkHours());
                        dbHelper.updateRope(updatedRope);
                        if(!mNfc) mCurrentPosition++;
                        if(mCurrentPosition < mSelectedPositions.size() && !mNfc) {
                            updateView(getView());
                        } else {
                            Bundle args = new Bundle();
                            args.putParcelable("inspection", inspection);
                            args.putParcelableArrayList("selectedPositions", mSelectedPositions);
                            args.putBoolean("nfc", mNfc);
                            Navigation.findNavController(getView()).navigate(R.id.action_nav_new_inspection_parameters_to_nav_results, args);
                        }
                    } else {
                        Toast.makeText(mContext, "Failed", Toast.LENGTH_LONG);
                    }
                }
            });

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public InitializeInspectionFragment() { }

    public static InitializeInspectionFragment newInstance(ArrayList<Position> mSelectedPositions) {
        InitializeInspectionFragment fragment = new InitializeInspectionFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(POSITIONS, mSelectedPositions);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSelectedPositions = getArguments().getParcelableArrayList(POSITIONS);
            mCurrentPosition = getArguments().getInt(CURRENT_POSITION);
            mNfc = getArguments().getBoolean("nfc", false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_initialize_inspection, container, false);

        initButtons(view);
        initView(view);

        return view;
    }

    private void updateView(View view) {
        positionName.setText(mSelectedPositions.get(mCurrentPosition).getName());
        MigrationDbHelper dbHelper = MigrationDbHelper.getInstance(mContext);
        Rope rope = dbHelper.getRopeByPosition(mSelectedPositions.get(mCurrentPosition));
        if(rope.getId() > 0) {
            wireSerial.setText(rope.getSerialNr());
            continueBtn.setEnabled(true);
            whTxt.setEnabled(true);
            dateTxt.setEnabled(true);
        } else {
            new AlertDialog.Builder(mContext)
                    .setTitle("Start an inspection")
                    .setMessage("The position you selected has no wires. Please choose another position.")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.ok, null).show();
            continueBtn.setEnabled(false);
            whTxt.setEnabled(false);
            dateTxt.setEnabled(false);
        }
    }

    private void initView(View view) {
//        if(!mNfc){
            positionName = view.findViewById(R.id.value_position);
            positionName.setText(mSelectedPositions.get(mCurrentPosition).getName());
            wireSerial = view.findViewById(R.id.value_wire_serial);
            whTxt = view.findViewById(R.id.editText_inspection_work_hours);
            dateTxt = view.findViewById(R.id.editText_inspection_date);
            MigrationDbHelper dbHelper = MigrationDbHelper.getInstance(mContext);
            Rope rope = dbHelper.getRopeByPosition(mSelectedPositions.get(mCurrentPosition));
            if(rope.getId() > 0) {
                wireSerial.setText(rope.getSerialNr());
                continueBtn.setEnabled(true);
                whTxt.setEnabled(true);
                dateTxt.setEnabled(true);
            } else {
                new AlertDialog.Builder(mContext)
                        .setTitle("Start an inspection")
                        .setMessage("The position you selected has no wires. Please choose another position.")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.ok, null).show();
                continueBtn.setEnabled(false);
                whTxt.setEnabled(false);
                dateTxt.setEnabled(false);
            }
//        } else {
//            navController.navigate(R.id.nav_inspection);
//        }


        inspectionDateSelected = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                inspectionCalendar.set(Calendar.YEAR, year);
                inspectionCalendar.set(Calendar.MONTH, monthOfYear);
                inspectionCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        dateTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(mContext,inspectionDateSelected, inspectionCalendar
                        .get(Calendar.YEAR), inspectionCalendar.get(Calendar.MONTH),
                        inspectionCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void initButtons(View view) {
        cancelBtn = view.findViewById(R.id.button_inspection_cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_nav_new_inspection_parameters_to_nav_inspection);
            }
        });
        continueBtn = view.findViewById(R.id.button_inspection_save);
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(whTxt.getText().toString().trim().equals("") || dateTxt.getText().toString().trim().equals("")) {
                    new AlertDialog.Builder(mContext)
                            .setTitle("Start an inspection")
                            .setMessage("You must fill all information in the form in order to start an inspection.")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.ok, null).show();
                } else {
                    initializeInspection();
                }
            }
        });
    }
    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateTxt.setText(sdf.format(inspectionCalendar.getTime()));
    }

    private void initializeQuiz(Inspection inspection, Position position) {
        MigrationDbHelper dbHelper = MigrationDbHelper.getInstance(mContext);
        List<Rope> ropes = dbHelper.getRopesByPositionForRoutine(position);
        for (int j = 0; j < ropes.size(); j++) {
            Rope rope = ropes.get(j);
            List<Question> questions = dbHelper.getAllWireQuestionsForRoutine();
            for (int k = 0; k < questions.size(); k++) {
                Question question = questions.get(k);
                Result result = new Result();
                result.setInspectionId(inspection.getId());
                result.setPositionId(position.getId());
                result.setQuestionId(question.getId());
                result.setRopeId(rope.getId());
                result.setGlobal(false);
                dbHelper.addResult(result);
            }
        }
    }
    private void initializeInspection() {
        Inspection inspection = new Inspection();
        inspection.setStep(0);
        inspection.setDate_created(dateTxt.getText().toString());
        inspection.setFinished(false);
        inspection.setWorkHours(Integer.parseInt(whTxt.getText().toString()));
        inspection.setPosition(mSelectedPositions.get(mCurrentPosition).getId());
        MigrationDbHelper dbHelper = MigrationDbHelper.getInstance(mContext);
        dbHelper.addInspection(inspection);
        inspection = dbHelper.getLastInsertedInspectionByPosition(mSelectedPositions.get(mCurrentPosition).getId());
        initializeQuiz(inspection, mSelectedPositions.get(mCurrentPosition));
        Intent resultIntent = new Intent(mContext, QuizActivity.class);
        resultIntent.putExtra("inspection", inspection);
        resultIntent.putExtra("currentPosition", mCurrentPosition);
        resultIntent.putExtra("nfc", mNfc);
        quizActivityLanuncher.launch(resultIntent);
    }
}