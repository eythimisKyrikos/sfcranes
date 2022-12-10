package com.safeline.safelinecranes.ui.inspection;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.github.chrisbanes.photoview.PhotoView;
import com.juliomarcos.ImageViewPopUpHelper;
import com.safeline.safelinecranes.R;
import com.safeline.safelinecranes.db.MigrationDbHelper;
import com.safeline.safelinecranes.models.Answer;
import com.safeline.safelinecranes.models.Inspection;
import com.safeline.safelinecranes.models.Position;
import com.safeline.safelinecranes.models.Question;
import com.safeline.safelinecranes.models.Result;
import com.safeline.safelinecranes.models.Rope;
import com.safeline.safelinecranes.ui.results.SingleResultActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private Inspection mInspection;
    private int mCurrentPosition;
    private List<Result> emptyResults;
    MigrationDbHelper dbHelper;
    private List<Answer> answerList;
    public HashMap<Answer, Answer> listHash;
    public List<Answer> listDataHeader;
    private ExpandableListView startList;
    private boolean mNfc;

    public static InspectionExpandableListAdapter inspectionListAdapter;

    private int resultsCounter;
    private int resultsCounterTotal;
    private Question currentQuestion;
    private Result currentResult;
    private Position currentPosition;
    private Rope currentRope;
    private boolean notFinishedSubResult = true;

    private TextView headerTxt;
    private TextView detailsTxt;
    private TextView positionTxt;
    private TextView ropeTxt;
    private TextView typeOfQuestionTxt;
    private TextView inspectionTxt;

    private ImageButton btnPreviousQuestion;

    private ActivityResultLauncher<Intent> changeQuizActivityLanuncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = new Intent();
                        intent.putExtra("currentPosition", mCurrentPosition);
                        intent.putExtra("inspection", mInspection);
                        intent.putExtra("nfc", mNfc);
                        Position position  = dbHelper.getPositionById(mInspection.getPosition());
                        Rope rope = dbHelper.getRopeByPosition(position);
                        intent.putExtra("rope_id", rope.getId());
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        Toast.makeText(QuizActivity.this, "Canceled", Toast.LENGTH_LONG);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Intent intent = getIntent();
        mInspection = intent.getParcelableExtra("inspection");
        mCurrentPosition = intent.getIntExtra("currentPosition",0);
        mNfc = intent.getBooleanExtra("nfc",false);
        dbHelper = MigrationDbHelper.getInstance(this);
        emptyResults = dbHelper.getAllResultsOfInspection(mInspection.getId());
        resultsCounterTotal = emptyResults.size();
        if(mInspection.getStep() != null) {
            resultsCounter = mInspection.getStep().intValue();
        } else {
            resultsCounter = 0;
        }

        btnPreviousQuestion = findViewById(R.id.btn_prev);
        btnPreviousQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(resultsCounter > 0) resultsCounter--;
                showPreviousQuestion();
            }
        });
        showNextQuestion();
    }

    private void showNextQuestion(){
        if (resultsCounter < resultsCounterTotal) {
            currentResult = emptyResults.get(resultsCounter);
            if(currentResult.getPositionId() != null){
                currentPosition = dbHelper.getPositionById(currentResult.getPositionId());
            }
            if(currentResult.getRopeId() != null){
                currentRope = dbHelper.getRopeById(currentResult.getRopeId());
            }
            currentQuestion = dbHelper.getQuestionById(currentResult.getQuestionId());

            if(resultsCounter > 0 && notFinishedSubResult) checkFinishedQuiz();
            notFinishedSubResult = true;

            inspectionTxt = findViewById(R.id.inspecrtion_text_view);
            inspectionTxt.setText(mInspection.getDate_created());
            headerTxt = findViewById(R.id.question_header_textView);
            headerTxt.setText(currentQuestion.getHeader());
            detailsTxt = findViewById(R.id.question_details_textView);
            detailsTxt.setText(currentQuestion.getDetails());
            ropeTxt = findViewById(R.id.rope_text_view);
            positionTxt = findViewById(R.id.position_text_view);
            if(currentPosition != null) {
                positionTxt.setText(currentPosition.getName());
            }
            if(currentPosition != null && currentRope!= null) {
                ropeTxt.setText(currentRope.getSerialNr());
                positionTxt.setText(currentPosition.getName() + " position");
            }

            typeOfQuestionTxt = findViewById(R.id.question_test);
            typeOfQuestionTxt.setText(currentQuestion.getTest());

            answerList = dbHelper.getAllAnswersOfQuestion(currentQuestion);
            listDataHeader = new ArrayList<>(answerList.size());
            listDataHeader.addAll(answerList);
            listHash = new HashMap<>();

            inspectionListAdapter = new InspectionExpandableListAdapter(this, listDataHeader, listHash) {
                @Override
                public void OnTextClick(String id){
                    Result res = new Result();
                    res.setId(currentResult.getId());
                    res.setInspectionId(currentResult.getInspectionId());
                    res.setPositionId(currentResult.getPositionId());
                    res.setRopeId(currentResult.getRopeId());
                    res.setQuestionId(currentResult.getQuestionId());
                    res.setAnswerId(Integer.parseInt(id));
                    res.setGlobal(currentResult.isGlobal());
                    saveAnswer(res);
                    selectAnswer();
                }
                @Override
                public void zoomImageFromThumb(final View thumbView, int imageResId) {
                    ImageView image = (ImageView) thumbView;
                    image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder mBuilder = new AlertDialog.Builder(QuizActivity.this);
                            View mView = getLayoutInflater().inflate(R.layout.picture_zoom_dialog, null);
                            PhotoView photoView = mView.findViewById(R.id.photoView_zoom);
                            photoView.setImageResource(imageResId);
                            mBuilder.setView(mView);
                            AlertDialog mDialog = mBuilder.create();
                            mDialog.show();
                            mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        }
                    });
                }
            };
            startList = (ExpandableListView) findViewById(R.id.inspection_list);
            startList.setAdapter(inspectionListAdapter);
            startList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                @Override
                public void onGroupExpand(int groupPosition) {
                    if(listDataHeader.get(groupPosition).getImage1() == null){
                        startList.collapseGroup(groupPosition);
                    }
                }
            });
            resultsCounter++;
        } else {
            finishQuiz();
        }
    }
    private void showPreviousQuestion() {
        if (resultsCounter > 0) {
            resultsCounter--;
            currentResult = emptyResults.get(resultsCounter);
            if(currentResult.getPositionId() != null) {
                currentPosition = dbHelper.getPositionById(currentResult.getPositionId());
            } else {
                currentPosition = null;
            }
            if(currentResult.getRopeId() != null) {
                currentRope = dbHelper.getRopeById(currentResult.getRopeId());
            } else {
                currentRope = null;
            }
            currentQuestion = dbHelper.getQuestionById(currentResult.getQuestionId());
            typeOfQuestionTxt = findViewById(R.id.question_test);
            typeOfQuestionTxt.setText(currentQuestion.getTest());

            headerTxt = findViewById(R.id.question_header_textView);
            headerTxt.setText(currentQuestion.getHeader());
            detailsTxt = findViewById(R.id.question_details_textView);
            detailsTxt.setText(currentQuestion.getDetails());
            ropeTxt = findViewById(R.id.rope_text_view);
            positionTxt = findViewById(R.id.position_text_view);
            if(currentPosition != null && currentRope!= null) {
                ropeTxt.setText(currentRope.getSerialNr());
                positionTxt.setText(currentPosition.getName() + " position");
            } else if(currentPosition != null){
                ropeTxt.setText("");
                positionTxt.setText(currentPosition.getName());
            } else {
                positionTxt.setText("");
                ropeTxt.setText("");
            }

            answerList = dbHelper.getAllAnswersOfQuestion(currentQuestion);
            listDataHeader = new ArrayList<>(answerList.size());
            listDataHeader.addAll(answerList);
            listHash = new HashMap<>();

            inspectionListAdapter = new InspectionExpandableListAdapter(this, listDataHeader, listHash) {
                @Override
                public void OnTextClick(String id){
                    Result res = new Result();
                    res.setId(currentResult.getId());
                    res.setInspectionId(currentResult.getInspectionId());
                    res.setPositionId(currentResult.getPositionId());
                    res.setRopeId(currentResult.getRopeId());
                    res.setQuestionId(currentResult.getQuestionId());
                    res.setAnswerId(Integer.parseInt(id));
                    res.setGlobal(currentResult.isGlobal());
                    saveAnswer(res);
                    resultsCounter++;
                    selectAnswer();
                }
                @Override
                public void zoomImageFromThumb(final View thumbView, int imageResId) {
                    ImageView image = (ImageView) thumbView;
                    ImageViewPopUpHelper.enablePopUpOnClick(QuizActivity.this, image);
                }
            };
            startList = (ExpandableListView) findViewById(R.id.inspection_list);
            startList.setAdapter(inspectionListAdapter);
            startList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                @Override
                public void onGroupExpand(int groupPosition) {
                    if(listDataHeader.get(groupPosition).getImage1() == null){
                        startList.collapseGroup(groupPosition);
                    }
                }
            });
        }
    }

    private void selectAnswer() {
        showNextQuestion();
    }
    private void saveAnswer(Result result) {
        dbHelper.updateResult(result);
        mInspection.setStep(resultsCounter);
        dbHelper.updateInspection(mInspection);
    }
    private void checkFinishedQuiz(){
//        notFinishedSubResult = true;
//        Result prevResult = emptyResults.get(resultsCounter - 1);
//        Question prevQuestion = dbHelper.getQuestionById(prevResult.getQuestionId());
//        if ((prevResult.getPositionId() != null) && (currentResult.getPositionId() != null)) {
//            if(prevResult.getPositionId().intValue() != currentResult.getPositionId().intValue()) {
//                Intent resultIntent = new Intent(this, SingleResultActivity.class);
//                resultIntent.putExtra("inspection_id", mInspection.getId());
//                resultIntent.putExtra("position_id", mInspection.getPosition());
////                if(prevResult.getRopeId() != null){
////                    resultIntent.putExtra("rope_id", mInspection.get);
////                }
//                resultIntent.putExtra("test", prevQuestion.getTest());
//                resultIntent.putExtra("current_counter", resultsCounter);
//                changeQuizActivityLanuncher.launch(resultIntent);
//            }
//        }
    }
    private void finishQuiz() {
        Intent resultIntent = new Intent(this, SingleResultActivity.class);
//        resultIntent.putExtra("inspection_id", mInspection.getId());
//        resultIntent.putExtra("position_id", mInspection.getPosition());
        resultIntent.putExtra("inspection", mInspection);
        MigrationDbHelper dbHelper = MigrationDbHelper.getInstance(this);
        Position position = dbHelper.getPositionById(mInspection.getPosition());
        resultIntent.putExtra("currentPosition", mCurrentPosition);
        resultIntent.putExtra("nfc", mNfc);
        changeQuizActivityLanuncher.launch(resultIntent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        return super.onKeyDown(keyCode, event);
    }
}