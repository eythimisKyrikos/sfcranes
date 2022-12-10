package com.safeline.safelinecranes.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.safeline.safelinecranes.models.Answer;
import com.safeline.safelinecranes.models.Question;
import java.util.ArrayList;
import java.util.List;

public final class AnswerDBApi {

    public static  void addAnswerDB(SQLiteDatabase db, Answer answer) {
        ContentValues cv = new ContentValues();
        cv.put(Contract.AnswersTable.HEADER, answer.getHeader());
        cv.put(Contract.AnswersTable.DETAILS, answer.getDetails());
        cv.put(Contract.AnswersTable.QUESTION_ID, answer.getQuestion_id());
        cv.put(Contract.AnswersTable.IMAGE1, answer.getImage1());
        cv.put(Contract.AnswersTable.RECOMMENDATIONS, answer.getRecommendations());
        cv.put(Contract.AnswersTable.VALUE, answer.getValue());
        db.insert(Contract.AnswersTable.TABLE_NAME, null, cv);
    }
    @SuppressLint("Range")
    public static List<Answer> getAllAnswersOfQuestionDB(SQLiteDatabase db, Question question) {
        List<Answer> answerList = new ArrayList<>();
        String query = "SELECT * FROM " + Contract.AnswersTable.TABLE_NAME + " WHERE " + Contract.AnswersTable.QUESTION_ID + " = ? ";
        String[] selectionArgs = new String[] {String.valueOf(question.getId())};
        Cursor c = db.rawQuery(query, selectionArgs);

        if(c.moveToFirst()) {
            do {
                Answer answer = new Answer();
                answer.setId(c.getInt(c.getColumnIndex(Contract.AnswersTable._ID)));
                answer.setHeader(c.getString(c.getColumnIndex(Contract.AnswersTable.HEADER)));
                answer.setDetails(c.getString(c.getColumnIndex(Contract.AnswersTable.DETAILS)));
                answer.setQuestion_id(c.getInt(c.getColumnIndex(Contract.AnswersTable.QUESTION_ID)));
                answer.setImage1(c.getString(c.getColumnIndex(Contract.AnswersTable.IMAGE1)));
                answer.setRecommendations(c.getString(c.getColumnIndex(Contract.AnswersTable.RECOMMENDATIONS)));
                answer.setValue(c.getInt(c.getColumnIndex(Contract.AnswersTable.VALUE)));
                answerList.add(answer);
            } while(c.moveToNext());
        }
        c.close();
        return  answerList;
    }
    @SuppressLint("Range")
    public static Answer getAllAnswerByIdDB(SQLiteDatabase db, int id) {
        Answer answer = new Answer();
        String query = "SELECT * FROM " + Contract.AnswersTable.TABLE_NAME + " WHERE " + Contract.AnswersTable._ID + " = ? ";
        String[] selectionArgs = new String[] {String.valueOf(id)};
        Cursor c = db.rawQuery(query, selectionArgs);

        if(c.moveToFirst()) {
            do {
                answer.setId(c.getInt(c.getColumnIndex(Contract.AnswersTable._ID)));
                answer.setHeader(c.getString(c.getColumnIndex(Contract.AnswersTable.HEADER)));
                answer.setDetails(c.getString(c.getColumnIndex(Contract.AnswersTable.DETAILS)));
                answer.setQuestion_id(c.getInt(c.getColumnIndex(Contract.AnswersTable.QUESTION_ID)));
                answer.setImage1(c.getString(c.getColumnIndex(Contract.AnswersTable.IMAGE1)));
                answer.setRecommendations(c.getString(c.getColumnIndex(Contract.AnswersTable.RECOMMENDATIONS)));
                answer.setValue(c.getInt(c.getColumnIndex(Contract.AnswersTable.VALUE)));
            } while(c.moveToNext());
        }
        c.close();
        return  answer;
    }
}
