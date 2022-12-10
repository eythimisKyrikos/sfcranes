package com.safeline.safelinecranes.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.safeline.safelinecranes.models.Inspection;
import com.safeline.safelinecranes.models.Result;

import java.util.ArrayList;
import java.util.List;

public final class ResultsDBApi {

    public static void addResultDB(SQLiteDatabase db, Result result) {
        ContentValues cv = new ContentValues();
        cv.put(Contract.ResultsTable.INSPECTION, result.getInspectionId());
        cv.put(Contract.ResultsTable.POSITION, result.getPositionId());
        cv.put(Contract.ResultsTable.QUESTION, result.getQuestionId());
        cv.put(Contract.ResultsTable.ANSWER, result.getAnswerId());
        cv.put(Contract.ResultsTable.ROPE, result.getRopeId());
        cv.put(Contract.ResultsTable.IS_GLOBAL, result.isGlobal());
        db.insert(Contract.ResultsTable.TABLE_NAME, null, cv);
    }
    public static boolean addResultDBWithID(SQLiteDatabase db, Result result) {
        ContentValues cv = new ContentValues();
        cv.put(Contract.ResultsTable._ID, result.getId());
        cv.put(Contract.ResultsTable.INSPECTION, result.getInspectionId());
        cv.put(Contract.ResultsTable.POSITION, result.getPositionId());
        cv.put(Contract.ResultsTable.QUESTION, result.getQuestionId());
        cv.put(Contract.ResultsTable.ANSWER, result.getAnswerId());
        cv.put(Contract.ResultsTable.ROPE, result.getRopeId());
        cv.put(Contract.ResultsTable.IS_GLOBAL, result.isGlobal());
        try {
            db.insert(Contract.ResultsTable.TABLE_NAME, null, cv);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
    public static void updateResultDB(SQLiteDatabase db, Result result) {
        ContentValues cv = new ContentValues();
        cv.put(Contract.ResultsTable.INSPECTION, result.getInspectionId());
        cv.put(Contract.ResultsTable.POSITION, result.getPositionId());
        cv.put(Contract.ResultsTable.QUESTION, result.getQuestionId());
        cv.put(Contract.ResultsTable.ANSWER, result.getAnswerId());
        cv.put(Contract.ResultsTable.ROPE, result.getRopeId());
        cv.put(Contract.ResultsTable.IS_GLOBAL, result.isGlobal());
        db.update(Contract.ResultsTable.TABLE_NAME, cv, "_ID = ?", new String[] {String.valueOf(result.getId())});
    }
    @SuppressLint("Range")
    public static List<Result> getAllResultsOfInspectionDB(SQLiteDatabase db, int inspectionID) {
        List<Result> resultList = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM " + Contract.ResultsTable.TABLE_NAME + " WHERE " +
                        Contract.ResultsTable.INSPECTION + " = ? ORDER BY " + Contract.ResultsTable.POSITION +
                        " ASC, " + Contract.ResultsTable.ROPE + " ASC",
                new String[] {String.valueOf(inspectionID)});

        if(c.moveToFirst()) {
            do {
                Result result = new Result();
                result.setId(c.getInt(c.getColumnIndex(Contract.ResultsTable._ID)));
                result.setInspectionId(c.getInt(c.getColumnIndex(Contract.ResultsTable.INSPECTION)));
                result.setPositionId(c.getInt(c.getColumnIndex(Contract.ResultsTable.POSITION)));
                result.setQuestionId(c.getInt(c.getColumnIndex(Contract.ResultsTable.QUESTION)));
                result.setAnswerId(c.getInt(c.getColumnIndex(Contract.ResultsTable.ANSWER)));
                result.setRopeId(c.getInt(c.getColumnIndex(Contract.ResultsTable.ROPE)));
                result.setGlobal(c.getInt(c.getColumnIndex(Contract.ResultsTable.IS_GLOBAL)) > 0 ? true : false);
                resultList.add(result);
            } while(c.moveToNext());
        }
        c.close();
        return  resultList;
    }
    @SuppressLint("Range")
    public static List<Result> getAllResultsOfNonSyncedInspectionsDB(SQLiteDatabase db, List<Inspection> inspections) {
        List<Result> results = new ArrayList<>();
        String sqlQuery = "SELECT * FROM " + Contract.ResultsTable.TABLE_NAME + " WHERE " +
                Contract.ResultsTable.INSPECTION + " IN (";
        for(int i=0; i<inspections.size(); i++) {
            sqlQuery = sqlQuery + inspections.get(i).getId();
            sqlQuery = sqlQuery + ",";
        }
        sqlQuery = (sqlQuery.substring(0, sqlQuery.length() - 1));
        sqlQuery = sqlQuery + ")";
        Cursor c = db.rawQuery(sqlQuery, null);
        if(c.moveToFirst()) {
            do {
                Result result = new Result();
                result.setId(c.getInt(c.getColumnIndex(Contract.ResultsTable._ID)));
                result.setInspectionId(c.getInt(c.getColumnIndex(Contract.ResultsTable.INSPECTION)));
                result.setPositionId(c.getInt(c.getColumnIndex(Contract.ResultsTable.POSITION)));
                result.setQuestionId(c.getInt(c.getColumnIndex(Contract.ResultsTable.QUESTION)));
                result.setAnswerId(c.getInt(c.getColumnIndex(Contract.ResultsTable.ANSWER)));
                result.setRopeId(c.getInt(c.getColumnIndex(Contract.ResultsTable.ROPE)));
                result.setGlobal(c.getInt(c.getColumnIndex(Contract.ResultsTable.IS_GLOBAL)) > 0 ? true : false);
                results.add(result);
            } while(c.moveToNext());
        }
        c.close();
        return results;
    }
    @SuppressLint("Range")
    public static Result getResultDB(SQLiteDatabase db, int inspectionID, int positionID, int questionID) {
        Result result = new Result();
        String query = "SELECT * FROM " + Contract.ResultsTable.TABLE_NAME + " WHERE " + Contract.ResultsTable.INSPECTION + " = ? AND " +
                Contract.ResultsTable.POSITION + " = ? AND " + Contract.ResultsTable.QUESTION + " = ?";
        Cursor c = db.rawQuery(query, new String[] {String.valueOf(inspectionID), String.valueOf(positionID), String.valueOf(questionID)});

        if(c.moveToFirst()) {
            do {
                result.setId(c.getInt(c.getColumnIndex(Contract.ResultsTable._ID)));
                result.setInspectionId(c.getInt(c.getColumnIndex(Contract.ResultsTable.INSPECTION)));
                result.setPositionId(c.getInt(c.getColumnIndex(Contract.ResultsTable.POSITION)));
                result.setQuestionId(c.getInt(c.getColumnIndex(Contract.ResultsTable.QUESTION)));
                result.setAnswerId(c.getInt(c.getColumnIndex(Contract.ResultsTable.ANSWER)));
                result.setRopeId(c.getInt(c.getColumnIndex(Contract.ResultsTable.ROPE)));
                result.setGlobal(c.getInt(c.getColumnIndex(Contract.ResultsTable.IS_GLOBAL)) > 0 ? true : false);
            } while(c.moveToNext());
        }
        c.close();
        return  result;
    }
    @SuppressLint("Range")
    public static List<Result> getAllResultsOfRopeDB(SQLiteDatabase db, int inspection_id, int position_id, int rope_id) {
        List<Result> results = new ArrayList<>();
        String sqlQuery = "SELECT * FROM " + Contract.ResultsTable.TABLE_NAME + " r INNER JOIN " + Contract.QuestionsTable.TABLE_NAME +
                " q ON q." + Contract.QuestionsTable._ID + " = r." + Contract.ResultsTable.QUESTION + " WHERE q." + Contract.QuestionsTable.TEST +
                " IN (\"ROPE\" , \"WIRE\", \"TAIL\") AND r." + Contract.ResultsTable.INSPECTION + " = ? AND " +
                Contract.ResultsTable.POSITION + " = ? AND " + Contract.ResultsTable.ROPE + " = ?";

        Cursor c = db.rawQuery(sqlQuery, new String[] {String.valueOf(inspection_id), String.valueOf(position_id), String.valueOf(rope_id)});
        if(c.moveToFirst()) {
            do {
                Result result = new Result();
                result.setId(c.getInt(c.getColumnIndex(Contract.ResultsTable._ID)));
                result.setInspectionId(c.getInt(c.getColumnIndex(Contract.ResultsTable.INSPECTION)));
                result.setPositionId(c.getInt(c.getColumnIndex(Contract.ResultsTable.POSITION)));
                result.setQuestionId(c.getInt(c.getColumnIndex(Contract.ResultsTable.QUESTION)));
                result.setAnswerId(c.getInt(c.getColumnIndex(Contract.ResultsTable.ANSWER)));
                result.setRopeId(c.getInt(c.getColumnIndex(Contract.ResultsTable.ROPE)));
                result.setGlobal(c.getInt(c.getColumnIndex(Contract.ResultsTable.IS_GLOBAL)) > 0 ? true : false);
                results.add(result);
            } while(c.moveToNext());
        }
        c.close();
        return results;
    }
}
