package com.safeline.safelinecranes.ui.results;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.safeline.safelinecranes.R;
import com.safeline.safelinecranes.db.MigrationDbHelper;
import com.safeline.safelinecranes.models.Answer;
import com.safeline.safelinecranes.models.FinalResult;
import com.safeline.safelinecranes.models.FinishedResults;
import com.safeline.safelinecranes.models.Inspection;
import com.safeline.safelinecranes.models.Position;
import com.safeline.safelinecranes.models.QnA;
import com.safeline.safelinecranes.models.Result;
import com.safeline.safelinecranes.models.Rope;
import com.safeline.safelinecranes.models.Severity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SingleResultActivity extends AppCompatActivity {

    Intent mIntent;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private SingleResultAdapter mAdapter;
    private ArrayList<QnA> questionList;

    private Inspection mInspection;
    private int inspection_id;
    private int position_id;
    private int mCurrentPosition;

    TextView nextBtn;
    TextView previousBtn;
    ImageButton btnPrevArrow;
    ImageButton btnNxtArrow;
    TextView HeaderTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_result);
        mIntent = getIntent();
        mInspection = mIntent.getParcelableExtra("inspection");
        inspection_id = mInspection.getId();
        position_id = mInspection.getPosition();
        mCurrentPosition = mIntent.getIntExtra("currentPosition", 0);


        HeaderTextView = findViewById(R.id.quiz_list_header);
        MigrationDbHelper dbHelper = MigrationDbHelper.getInstance(this);
        Position pos = dbHelper.getPositionById(position_id);
        Rope rope = dbHelper.getRopeByPosition(pos);
        HeaderTextView.setText("Results for " + rope.getSerialNr());

        loadList();
        buildRecyclerView();
        initView();
    }

    private void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.single_result_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new SingleResultAdapter(questionList, this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void loadList() {
        questionList = new ArrayList<>();
        MigrationDbHelper dbHelper = MigrationDbHelper.getInstance(this);
        List<Result> resultList = dbHelper.getAllResultsOfInspection(inspection_id);
        for (int i = 0; i < resultList.size(); i++) {
            String questionName = dbHelper.getQuestionById(resultList.get(i).getQuestionId()).getHeader();
            String answerName = dbHelper.getAllAnswerById(resultList.get(i).getAnswerId()).getHeader();
            int answerValue = dbHelper.getAllAnswerById(resultList.get(i).getAnswerId()).getValue();
            QnA qna = new QnA(questionName, answerName, answerValue);
            questionList.add(qna);
        }
    }
    private void initView(){
        btnPrevArrow = findViewById(R.id.btn_previous_test);
        btnPrevArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent resultIntent = new Intent();
//                resultIntent.putExtra("current_question", mCurrentCoutner-1);
//                setResult(RESULT_OK, resultIntent);
//                finish();
                //TODO: restart the quiz or go back one question
            }
        });
        btnNxtArrow = findViewById(R.id.btn_next_test);
        btnNxtArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextBtnListener();
            }
        });
        nextBtn = findViewById(R.id.btn_quiz_continue);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextBtnListener();
            }
        });
        previousBtn = findViewById(R.id.btn_quiz_return);
        previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: tha doume....
            }
        });
    }

    private void nextBtnListener() {
        createQuizResults(mInspection.getId());
        Intent resultIntent = new Intent();
        resultIntent.putExtra("currentPosition", mCurrentPosition );
        resultIntent.putExtra("inspection", mInspection );
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void createQuizResults(int inspectionID){
        FinalResult final_result = new FinalResult();
        MigrationDbHelper dbHelper = MigrationDbHelper.getInstance(this);
        final_result.setInspection(mInspection);
        List<Rope> ropes = dbHelper.getRopesFromResultsInPosition(mInspection.getId(), mInspection.getPosition());
        Position position = dbHelper.getPositionById(mInspection.getPosition());
        final_result.setRope(ropes.get(0));
        int severity_lvl = 0;
        List<FinalResult.Action> action_results = new ArrayList<>();
        List<Answer> answers = dbHelper.getAnswersOfRopeFromResults(inspectionID, position, ropes.get(0));
        FinalResult.Action action = final_result.new Action();
        for(int k=0; k<answers.size(); k++) {
            Answer answer = answers.get(k);
            if (answer.getRecommendations() != null) {
                Severity severity = dbHelper.getSeverityByCode(answer.getRecommendations());
                if(severity.getType().equals("SHOW")) {
                    FinalResult.Action show_action = final_result.new Action();
                    show_action.setSeverity_level(severity.getSeverity_level());
                    show_action.setAction(severity.getFriendly_text());
                    action_results.add(show_action);
                } else {
                    if (severity.getSeverity_level() > severity_lvl){
                        severity_lvl = severity.getSeverity_level();
                        action.setSeverity_level(severity.getSeverity_level());
                        action.setAction(severity.getFriendly_text());
                    }
                }
            }
        }
        action_results.add(action);
        final_result.setActions(action_results);
        createFinishedInpection(inspectionID, final_result);
    }

    private void createFinishedInpection(int inspectionID, FinalResult final_result) {
        MigrationDbHelper dbHelper = MigrationDbHelper.getInstance(this);
        Inspection inspection = dbHelper.getInspectionById(inspectionID);
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        inspection.setDate_finished(date);
        inspection.setFinished(true);
        dbHelper.updateInspection(inspection);
        Gson gson = new Gson();
        String inspection2Json = gson.toJson(final_result);
        FinishedResults finishedResult = new FinishedResults();
        finishedResult.setResults(inspection2Json);
        finishedResult.setDateOfInspection(date);
        finishedResult.setSync(false);
        finishedResult.setInspection_id(inspection.getId());
        dbHelper.addFinishedResult(finishedResult);
    }
}