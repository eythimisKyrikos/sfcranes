package com.safeline.safelinecranes.ui.results;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.safeline.safelinecranes.R;
import com.safeline.safelinecranes.models.QnA;

import java.util.ArrayList;

public class SingleResultAdapter extends RecyclerView.Adapter<SingleResultAdapter.SingleResultViewHolder> {

    private View v;
    private Context mContext;
    private ArrayList<QnA> mQuestionList;

    public static class SingleResultViewHolder extends RecyclerView.ViewHolder {
        public TextView question;
        public TextView answer;

        public SingleResultViewHolder(@NonNull View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.single_selection_quiz_question);
            answer = itemView.findViewById(R.id.single_selection_quiz_answer);
        }
    }

    public SingleResultAdapter(ArrayList<QnA> questioList, Context context) {
        mQuestionList = questioList;
        mContext = context;
    }

    @NonNull
    @Override
    public SingleResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.quiz_single_selection_item, parent, false);
        com.safeline.safelinecranes.ui.results.SingleResultAdapter.SingleResultViewHolder svh = new com.safeline.safelinecranes.ui.results.SingleResultAdapter.SingleResultViewHolder(v);
        return svh;
    }

    @Override
    public void onBindViewHolder(@NonNull SingleResultViewHolder holder, int position) {
        QnA mQnA = mQuestionList.get(position);
        holder.question.setText(mQnA.getQuestion());
        holder.answer.setText(mQnA.getAnswer());
        if(mQnA.getAnswerValue() == 5){
            v.setBackgroundColor(0xFF00CC08);
        } else if(mQnA.getAnswerValue() == 4){
            v.setBackgroundColor(0xFF8FCC00);
        } else if(mQnA.getAnswerValue() == 3){
            v.setBackgroundColor(0xFFECE801);
        } else if(mQnA.getAnswerValue() == 2){
            v.setBackgroundColor(0xFFEC8201);
        } else if(mQnA.getAnswerValue() == 1){
            v.setBackgroundColor(0xFFEC0707);
        }
    }

    @Override
    public int getItemCount() {
        return mQuestionList.size();
    }

}
