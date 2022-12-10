package com.safeline.safelinecranes.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.safeline.safelinecranes.models.Question;
import com.safeline.safelinecranes.models.Rope;

import java.util.ArrayList;
import java.util.List;

public final class QuestionDBApi {

    public static void addQuestionDB(SQLiteDatabase db, Question question) {
        ContentValues cv = new ContentValues();
        cv.put(Contract.QuestionsTable.HEADER, question.getHeader());
        cv.put(Contract.QuestionsTable.DETAILS, question.getDetails());
        cv.put(Contract.QuestionsTable.TEST, question.getTest());
        cv.put(Contract.QuestionsTable.IS_ROUTINE, question.isRoutine());
        cv.put(Contract.QuestionsTable.MATERIAL, question.getMaterialType());
        cv.put(Contract.QuestionsTable.OUTCOME, question.getOutcome());
        cv.put(Contract.QuestionsTable.ORDER, question.getOrder());
        cv.put(Contract.QuestionsTable.OPERATION, question.getOperation());
        cv.put(Contract.QuestionsTable.CONDITION, question.getConditionReply());
        cv.put(Contract.QuestionsTable.RISK, question.getRisk());
        db.insert(Contract.QuestionsTable.TABLE_NAME, null, cv);
    }
    @SuppressLint("Range")
    public static List<Question> getAllQuestionsDB(SQLiteDatabase db) {
        List<Question> questionList = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM " + Contract.QuestionsTable.TABLE_NAME, null);
        if(c.moveToFirst()) {
            do {
                Question question = new Question();
                question.setId(c.getInt(c.getColumnIndex(Contract.QuestionsTable._ID)));
                question.setHeader(c.getString(c.getColumnIndex(Contract.QuestionsTable.HEADER)));
                question.setDetails(c.getString(c.getColumnIndex(Contract.QuestionsTable.DETAILS)));
                questionList.add(question);
            } while(c.moveToNext());
        }
        c.close();
        return  questionList;
    }                                                   //TODO: FIX FUNCTION
    @SuppressLint("Range")
    public static List<Question> getAllConditionQuestionsDB(SQLiteDatabase db, boolean hasData, boolean hasNylon) {
        List<Question> questionList = new ArrayList<>();
        String query = "SELECT * FROM " + Contract.QuestionsTable.TABLE_NAME + " WHERE " + Contract.QuestionsTable.TEST + " = \"CONDITIONS\" AND " +
                Contract.QuestionsTable.CONDITION + " IS NULL UNION ALL SELECT * FROM " + Contract.QuestionsTable.TABLE_NAME + " WHERE " +
                Contract.QuestionsTable.TEST + "= \"CONDITIONS\" AND " + Contract.QuestionsTable.CONDITION + " = \"hasAppliedForcesData\" AND " +
                Contract.QuestionsTable.OPERATION + " = ? ";

        if(hasNylon) {
            query = query + "UNION ALL SELECT * FROM " + Contract.QuestionsTable.TABLE_NAME + " WHERE " + Contract.QuestionsTable.TEST +
                    "= \"CONDITIONS\" AND " + Contract.QuestionsTable.CONDITION + " = \"isNylon\"";
        }

        String[] selectionArgs = new String[] {String.valueOf(hasData)};
        Cursor c = db.rawQuery(query, selectionArgs);

        if(c.moveToFirst()) {
            do {
                Question question = new Question();
                question.setId(c.getInt(c.getColumnIndex(Contract.QuestionsTable._ID)));
                question.setHeader(c.getString(c.getColumnIndex(Contract.QuestionsTable.HEADER)));
                question.setDetails(c.getString(c.getColumnIndex(Contract.QuestionsTable.DETAILS)));
                question.setTest(c.getString(c.getColumnIndex(Contract.QuestionsTable.TEST)));
                question.setRoutine(c.getInt(c.getColumnIndex(Contract.QuestionsTable.IS_ROUTINE)) > 0 ? true : false);
                question.setMaterialType(c.getString(c.getColumnIndex(Contract.QuestionsTable.MATERIAL)));
                question.setOutcome(c.getString(c.getColumnIndex(Contract.QuestionsTable.OUTCOME)));
                question.setOrder(c.getInt(c.getColumnIndex(Contract.QuestionsTable.ORDER)));
                question.setOperation(c.getString(c.getColumnIndex(Contract.QuestionsTable.OPERATION)));
                question.setConditionReply(c.getString(c.getColumnIndex(Contract.QuestionsTable.CONDITION)));
                question.setRisk(c.getString(c.getColumnIndex(Contract.QuestionsTable.RISK)));
                questionList.add(question);
            } while(c.moveToNext());
        }
        c.close();
        return  questionList;
    }
    @SuppressLint("Range")
    public static List<Question> getAllConditionQuestionsForDetailedDB(SQLiteDatabase db, boolean hasData) {
        List<Question> questionList = new ArrayList<>();
        String query = "SELECT * FROM " + Contract.QuestionsTable.TABLE_NAME + " WHERE " + Contract.QuestionsTable.TEST + " = \"CONDITIONS\" AND " +
                Contract.QuestionsTable.CONDITION + " IS NULL AND " + Contract.QuestionsTable.IS_ROUTINE + " = false " +
                " UNION ALL SELECT * FROM " + Contract.QuestionsTable.TABLE_NAME + " WHERE " +
                Contract.QuestionsTable.TEST + "= \"CONDITIONS\" AND " + Contract.QuestionsTable.CONDITION + " = \"isNylon\" AND " +
                Contract.QuestionsTable.OPERATION + " = ? AND " + Contract.QuestionsTable.IS_ROUTINE + " = false " +
                " UNION ALL SELECT * FROM " + Contract.QuestionsTable.TABLE_NAME + " WHERE " +
                Contract.QuestionsTable.TEST + "= \"CONDITIONS\" AND " + Contract.QuestionsTable.CONDITION + " = \"hasAppliedForcesData\" AND " +
                Contract.QuestionsTable.OPERATION + " = ? AND " + Contract.QuestionsTable.IS_ROUTINE + " = false ";

        String[] selectionArgs = new String[] {String.valueOf(true), String.valueOf(hasData)};
        Cursor c = db.rawQuery(query, selectionArgs);

        if(c.moveToFirst()) {
            do {
                Question question = new Question();
                question.setId(c.getInt(c.getColumnIndex(Contract.QuestionsTable._ID)));
                question.setHeader(c.getString(c.getColumnIndex(Contract.QuestionsTable.HEADER)));
                question.setDetails(c.getString(c.getColumnIndex(Contract.QuestionsTable.DETAILS)));
                question.setTest(c.getString(c.getColumnIndex(Contract.QuestionsTable.TEST)));
                question.setRoutine(c.getInt(c.getColumnIndex(Contract.QuestionsTable.IS_ROUTINE)) > 0 ? true : false);
                question.setMaterialType(c.getString(c.getColumnIndex(Contract.QuestionsTable.MATERIAL)));
                question.setOutcome(c.getString(c.getColumnIndex(Contract.QuestionsTable.OUTCOME)));
                question.setOrder(c.getInt(c.getColumnIndex(Contract.QuestionsTable.ORDER)));
                question.setOperation(c.getString(c.getColumnIndex(Contract.QuestionsTable.OPERATION)));
                question.setConditionReply(c.getString(c.getColumnIndex(Contract.QuestionsTable.CONDITION)));
                question.setRisk(c.getString(c.getColumnIndex(Contract.QuestionsTable.RISK)));
                questionList.add(question);
            } while(c.moveToNext());
        }
        c.close();
        return  questionList;
    }
    @SuppressLint("Range")
    public static List<Question> getAllHardwareQuestionsForWireDB(SQLiteDatabase db) {
        List<Question> questionList = new ArrayList<>();
        String query = "SELECT * FROM " + Contract.QuestionsTable.TABLE_NAME + " WHERE " + Contract.QuestionsTable.TEST + " = \"HARDWARE\" AND " +
                Contract.QuestionsTable.MATERIAL + " = \"WIRE\"";

        String[] selectionArgs = new String[] {};
        Cursor c = db.rawQuery(query, selectionArgs);

        if(c.moveToFirst()) {
            do {
                Question question = new Question();
                question.setId(c.getInt(c.getColumnIndex(Contract.QuestionsTable._ID)));
                question.setHeader(c.getString(c.getColumnIndex(Contract.QuestionsTable.HEADER)));
                question.setDetails(c.getString(c.getColumnIndex(Contract.QuestionsTable.DETAILS)));
                question.setTest(c.getString(c.getColumnIndex(Contract.QuestionsTable.TEST)));
                question.setRoutine(c.getInt(c.getColumnIndex(Contract.QuestionsTable.IS_ROUTINE)) > 0 ? true : false);
                question.setMaterialType(c.getString(c.getColumnIndex(Contract.QuestionsTable.MATERIAL)));
                question.setOutcome(c.getString(c.getColumnIndex(Contract.QuestionsTable.OUTCOME)));
                question.setOrder(c.getInt(c.getColumnIndex(Contract.QuestionsTable.ORDER)));
                question.setOperation(c.getString(c.getColumnIndex(Contract.QuestionsTable.OPERATION)));
                question.setConditionReply(c.getString(c.getColumnIndex(Contract.QuestionsTable.CONDITION)));
                question.setRisk(c.getString(c.getColumnIndex(Contract.QuestionsTable.RISK)));
                questionList.add(question);
            } while(c.moveToNext());
        }
        c.close();
        return  questionList;
    }
    @SuppressLint("Range")
    public static List<Question> getAllHardwareQuestionsForRopeDB(SQLiteDatabase db) {
        List<Question> questionList = new ArrayList<>();
        String query = "SELECT * FROM " + Contract.QuestionsTable.TABLE_NAME + " WHERE " + Contract.QuestionsTable.TEST + " = \"HARDWARE\" AND " +
                Contract.QuestionsTable.MATERIAL + " = \"ROPE\"";

        String[] selectionArgs = new String[] {};
        Cursor c = db.rawQuery(query, selectionArgs);

        if(c.moveToFirst()) {
            do {
                Question question = new Question();
                question.setId(c.getInt(c.getColumnIndex(Contract.QuestionsTable._ID)));
                question.setHeader(c.getString(c.getColumnIndex(Contract.QuestionsTable.HEADER)));
                question.setDetails(c.getString(c.getColumnIndex(Contract.QuestionsTable.DETAILS)));
                question.setTest(c.getString(c.getColumnIndex(Contract.QuestionsTable.TEST)));
                question.setRoutine(c.getInt(c.getColumnIndex(Contract.QuestionsTable.IS_ROUTINE)) > 0 ? true : false);
                question.setMaterialType(c.getString(c.getColumnIndex(Contract.QuestionsTable.MATERIAL)));
                question.setOutcome(c.getString(c.getColumnIndex(Contract.QuestionsTable.OUTCOME)));
                question.setOrder(c.getInt(c.getColumnIndex(Contract.QuestionsTable.ORDER)));
                question.setOperation(c.getString(c.getColumnIndex(Contract.QuestionsTable.OPERATION)));
                question.setConditionReply(c.getString(c.getColumnIndex(Contract.QuestionsTable.CONDITION)));
                question.setRisk(c.getString(c.getColumnIndex(Contract.QuestionsTable.RISK)));
                questionList.add(question);
            } while(c.moveToNext());
        }
        c.close();
        return  questionList;
    }
    @SuppressLint("Range")
    public static List<Question> getAllStorageQuestionsDB(SQLiteDatabase db) {
        List<Question> questionList = new ArrayList<>();
        String query = "SELECT * FROM " + Contract.QuestionsTable.TABLE_NAME + " WHERE " + Contract.QuestionsTable.TEST + " = \"STORAGE\" ";
        String[] selectionArgs = new String[] {};
        Cursor c = db.rawQuery(query, selectionArgs);
        if(c.moveToFirst()) {
            do {
                Question question = new Question();
                question.setId(c.getInt(c.getColumnIndex(Contract.QuestionsTable._ID)));
                question.setHeader(c.getString(c.getColumnIndex(Contract.QuestionsTable.HEADER)));
                question.setDetails(c.getString(c.getColumnIndex(Contract.QuestionsTable.DETAILS)));
                question.setTest(c.getString(c.getColumnIndex(Contract.QuestionsTable.TEST)));
                question.setRoutine(c.getInt(c.getColumnIndex(Contract.QuestionsTable.IS_ROUTINE)) > 0 ? true : false);
                question.setMaterialType(c.getString(c.getColumnIndex(Contract.QuestionsTable.MATERIAL)));
                question.setOutcome(c.getString(c.getColumnIndex(Contract.QuestionsTable.OUTCOME)));
                question.setOrder(c.getInt(c.getColumnIndex(Contract.QuestionsTable.ORDER)));
                question.setOperation(c.getString(c.getColumnIndex(Contract.QuestionsTable.OPERATION)));
                question.setConditionReply(c.getString(c.getColumnIndex(Contract.QuestionsTable.CONDITION)));
                question.setRisk(c.getString(c.getColumnIndex(Contract.QuestionsTable.RISK)));
                questionList.add(question);
            } while(c.moveToNext());
        }
        c.close();
        return  questionList;
    }
    @SuppressLint("Range")
    public static List<Question> getAllTailQuestionsDB(SQLiteDatabase db) {
        List<Question> questionList = new ArrayList<>();
        String query = "SELECT * FROM " + Contract.QuestionsTable.TABLE_NAME + " WHERE " + Contract.QuestionsTable.TEST + " = \"TAIL\" ";
        String[] selectionArgs = new String[] {};
        Cursor c = db.rawQuery(query, selectionArgs);
        if(c.moveToFirst()) {
            do {
                Question question = new Question();
                question.setId(c.getInt(c.getColumnIndex(Contract.QuestionsTable._ID)));
                question.setHeader(c.getString(c.getColumnIndex(Contract.QuestionsTable.HEADER)));
                question.setDetails(c.getString(c.getColumnIndex(Contract.QuestionsTable.DETAILS)));
                question.setTest(c.getString(c.getColumnIndex(Contract.QuestionsTable.TEST)));
                question.setRoutine(c.getInt(c.getColumnIndex(Contract.QuestionsTable.IS_ROUTINE)) > 0 ? true : false);
                question.setMaterialType(c.getString(c.getColumnIndex(Contract.QuestionsTable.MATERIAL)));
                question.setOutcome(c.getString(c.getColumnIndex(Contract.QuestionsTable.OUTCOME)));
                question.setOrder(c.getInt(c.getColumnIndex(Contract.QuestionsTable.ORDER)));
                question.setOperation(c.getString(c.getColumnIndex(Contract.QuestionsTable.OPERATION)));
                question.setConditionReply(c.getString(c.getColumnIndex(Contract.QuestionsTable.CONDITION)));
                question.setRisk(c.getString(c.getColumnIndex(Contract.QuestionsTable.RISK)));
                questionList.add(question);
            } while(c.moveToNext());
        }
        c.close();
        return  questionList;
    }
    @SuppressLint("Range")
    public static List<Question> getAllTailQuestionsForRoutineDB(SQLiteDatabase db) {
        List<Question> questionList = new ArrayList<>();
        String query = "SELECT * FROM " + Contract.QuestionsTable.TABLE_NAME + " WHERE " + Contract.QuestionsTable.TEST + " = \"TAIL\" AND " +
                Contract.QuestionsTable.IS_ROUTINE + " = 1";
        String[] selectionArgs = new String[] {};
        Cursor c = db.rawQuery(query, selectionArgs);
        if(c.moveToFirst()) {
            do {
                Question question = new Question();
                question.setId(c.getInt(c.getColumnIndex(Contract.QuestionsTable._ID)));
                question.setHeader(c.getString(c.getColumnIndex(Contract.QuestionsTable.HEADER)));
                question.setDetails(c.getString(c.getColumnIndex(Contract.QuestionsTable.DETAILS)));
                question.setTest(c.getString(c.getColumnIndex(Contract.QuestionsTable.TEST)));
                question.setRoutine(c.getInt(c.getColumnIndex(Contract.QuestionsTable.IS_ROUTINE)) > 0 ? true : false);
                question.setMaterialType(c.getString(c.getColumnIndex(Contract.QuestionsTable.MATERIAL)));
                question.setOutcome(c.getString(c.getColumnIndex(Contract.QuestionsTable.OUTCOME)));
                question.setOrder(c.getInt(c.getColumnIndex(Contract.QuestionsTable.ORDER)));
                question.setOperation(c.getString(c.getColumnIndex(Contract.QuestionsTable.OPERATION)));
                question.setConditionReply(c.getString(c.getColumnIndex(Contract.QuestionsTable.CONDITION)));
                question.setRisk(c.getString(c.getColumnIndex(Contract.QuestionsTable.RISK)));
                questionList.add(question);
            } while(c.moveToNext());
        }
        c.close();
        return  questionList;
    }
    @SuppressLint("Range")
    public static List<Question> getAllRopeQuestionsDB(SQLiteDatabase db, boolean hasJacket, boolean hasTail) {
        List<Question> questionList = new ArrayList<>();
        String query = "SELECT * FROM " + Contract.QuestionsTable.TABLE_NAME + " WHERE " + Contract.QuestionsTable.TEST + " = \"ROPE\" AND " +
                Contract.QuestionsTable.CONDITION + " IS NULL UNION ALL SELECT * FROM " + Contract.QuestionsTable.TABLE_NAME + " WHERE " +
                Contract.QuestionsTable.TEST + "= \"ROPE\" AND " + Contract.QuestionsTable.CONDITION + " = ? ";
        if(hasTail) {
            query = query + "UNION ALL SELECT * FROM " + Contract.QuestionsTable.TABLE_NAME + " WHERE " +
                    Contract.QuestionsTable.TEST + "= \"ROPE\" AND " + Contract.QuestionsTable.CONDITION + " = \"hasTail\"";
        }
        String[] selectionArgs = new String[] {};
        if(hasJacket) {
            selectionArgs = new String[] {"DB"};
        } else {
            selectionArgs = new String[] {"SB"};
        }
        Cursor c = db.rawQuery(query, selectionArgs);
        if(c.moveToFirst()) {
            do {
                Question question = new Question();
                question.setId(c.getInt(c.getColumnIndex(Contract.QuestionsTable._ID)));
                question.setHeader(c.getString(c.getColumnIndex(Contract.QuestionsTable.HEADER)));
                question.setDetails(c.getString(c.getColumnIndex(Contract.QuestionsTable.DETAILS)));
                question.setTest(c.getString(c.getColumnIndex(Contract.QuestionsTable.TEST)));
                question.setRoutine(c.getInt(c.getColumnIndex(Contract.QuestionsTable.IS_ROUTINE)) > 0 ? true : false);
                question.setMaterialType(c.getString(c.getColumnIndex(Contract.QuestionsTable.MATERIAL)));
                question.setOutcome(c.getString(c.getColumnIndex(Contract.QuestionsTable.OUTCOME)));
                question.setOrder(c.getInt(c.getColumnIndex(Contract.QuestionsTable.ORDER)));
                question.setOperation(c.getString(c.getColumnIndex(Contract.QuestionsTable.OPERATION)));
                question.setConditionReply(c.getString(c.getColumnIndex(Contract.QuestionsTable.CONDITION)));
                question.setRisk(c.getString(c.getColumnIndex(Contract.QuestionsTable.RISK)));
                questionList.add(question);
            } while(c.moveToNext());
        }
        c.close();
        return  questionList;
    }
    @SuppressLint("Range")
    public static List<Question> getAllRopeQuestionsForRoutineDB(SQLiteDatabase db, boolean hasJacket, boolean hasTail) {
        List<Question> questionList = new ArrayList<>();
        String query = "SELECT * FROM " + Contract.QuestionsTable.TABLE_NAME + " WHERE " + Contract.QuestionsTable.TEST + " = \"ROPE\" AND " +
                Contract.QuestionsTable.CONDITION + " IS NULL AND " + Contract.QuestionsTable.IS_ROUTINE + " = 1 " +
                "UNION ALL SELECT * FROM " + Contract.QuestionsTable.TABLE_NAME + " WHERE " +
                Contract.QuestionsTable.TEST + "= \"ROPE\" AND " + Contract.QuestionsTable.IS_ROUTINE + " = 1 AND " +
                Contract.QuestionsTable.CONDITION + " = ? ";
        if(hasTail) {
            query = query + "UNION ALL SELECT * FROM " + Contract.QuestionsTable.TABLE_NAME + " WHERE " +
                    Contract.QuestionsTable.TEST + "= \"ROPE\" AND " + Contract.QuestionsTable.CONDITION + " = \"hasTail\"";
        }
        String[] selectionArgs = new String[] {};
        if(hasJacket) {
            selectionArgs = new String[] {"DB"};
        } else {
            selectionArgs = new String[] {"SB"};
        }
        Cursor c = db.rawQuery(query, selectionArgs);
        if(c.moveToFirst()) {
            do {
                Question question = new Question();
                question.setId(c.getInt(c.getColumnIndex(Contract.QuestionsTable._ID)));
                question.setHeader(c.getString(c.getColumnIndex(Contract.QuestionsTable.HEADER)));
                question.setDetails(c.getString(c.getColumnIndex(Contract.QuestionsTable.DETAILS)));
                question.setTest(c.getString(c.getColumnIndex(Contract.QuestionsTable.TEST)));
                question.setRoutine(c.getInt(c.getColumnIndex(Contract.QuestionsTable.IS_ROUTINE)) > 0 ? true : false);
                question.setMaterialType(c.getString(c.getColumnIndex(Contract.QuestionsTable.MATERIAL)));
                question.setOutcome(c.getString(c.getColumnIndex(Contract.QuestionsTable.OUTCOME)));
                question.setOrder(c.getInt(c.getColumnIndex(Contract.QuestionsTable.ORDER)));
                question.setOperation(c.getString(c.getColumnIndex(Contract.QuestionsTable.OPERATION)));
                question.setConditionReply(c.getString(c.getColumnIndex(Contract.QuestionsTable.CONDITION)));
                question.setRisk(c.getString(c.getColumnIndex(Contract.QuestionsTable.RISK)));
                questionList.add(question);
            } while(c.moveToNext());
        }
        c.close();
        return  questionList;
    }
    @SuppressLint("Range")
    public static List<Question> getAllWireQuestionsDB(SQLiteDatabase db) {
        List<Question> questionList = new ArrayList<>();
        String query = "SELECT * FROM " + Contract.QuestionsTable.TABLE_NAME + " WHERE " + Contract.QuestionsTable.TEST + " = \"WIRE\" ";
        String[] selectionArgs = new String[] {};
        Cursor c = db.rawQuery(query, selectionArgs);
        if(c.moveToFirst()) {
            do {
                Question question = new Question();
                question.setId(c.getInt(c.getColumnIndex(Contract.QuestionsTable._ID)));
                question.setHeader(c.getString(c.getColumnIndex(Contract.QuestionsTable.HEADER)));
                question.setDetails(c.getString(c.getColumnIndex(Contract.QuestionsTable.DETAILS)));
                question.setTest(c.getString(c.getColumnIndex(Contract.QuestionsTable.TEST)));
                question.setRoutine(c.getInt(c.getColumnIndex(Contract.QuestionsTable.IS_ROUTINE)) > 0 ? true : false);
                question.setMaterialType(c.getString(c.getColumnIndex(Contract.QuestionsTable.MATERIAL)));
                question.setOutcome(c.getString(c.getColumnIndex(Contract.QuestionsTable.OUTCOME)));
                question.setOrder(c.getInt(c.getColumnIndex(Contract.QuestionsTable.ORDER)));
                question.setOperation(c.getString(c.getColumnIndex(Contract.QuestionsTable.OPERATION)));
                question.setConditionReply(c.getString(c.getColumnIndex(Contract.QuestionsTable.CONDITION)));
                question.setRisk(c.getString(c.getColumnIndex(Contract.QuestionsTable.RISK)));
                questionList.add(question);
            } while(c.moveToNext());
        }
        c.close();
        return  questionList;
    }
    @SuppressLint("Range")
    public static List<Question> getAllWireQuestionsForRoutineDB(SQLiteDatabase db) {
        List<Question> questionList = new ArrayList<>();
        String query = "SELECT * FROM " + Contract.QuestionsTable.TABLE_NAME + " WHERE " + Contract.QuestionsTable.TEST + " = \"WIRE\" AND " +
                Contract.QuestionsTable.IS_ROUTINE + " = 1";
        String[] selectionArgs = new String[] {};
        Cursor c = db.rawQuery(query, selectionArgs);
        if(c.moveToFirst()) {
            do {
                Question question = new Question();
                question.setId(c.getInt(c.getColumnIndex(Contract.QuestionsTable._ID)));
                question.setHeader(c.getString(c.getColumnIndex(Contract.QuestionsTable.HEADER)));
                question.setDetails(c.getString(c.getColumnIndex(Contract.QuestionsTable.DETAILS)));
                question.setTest(c.getString(c.getColumnIndex(Contract.QuestionsTable.TEST)));
                question.setRoutine(c.getInt(c.getColumnIndex(Contract.QuestionsTable.IS_ROUTINE)) > 0 ? true : false);
                question.setMaterialType(c.getString(c.getColumnIndex(Contract.QuestionsTable.MATERIAL)));
                question.setOutcome(c.getString(c.getColumnIndex(Contract.QuestionsTable.OUTCOME)));
                question.setOrder(c.getInt(c.getColumnIndex(Contract.QuestionsTable.ORDER)));
                question.setOperation(c.getString(c.getColumnIndex(Contract.QuestionsTable.OPERATION)));
                question.setConditionReply(c.getString(c.getColumnIndex(Contract.QuestionsTable.CONDITION)));
                question.setRisk(c.getString(c.getColumnIndex(Contract.QuestionsTable.RISK)));
                questionList.add(question);
            } while(c.moveToNext());
        }
        c.close();
        return  questionList;
    }
    @SuppressLint("Range")
    public static List<Question> getAllQuestionsOfRopeDB(SQLiteDatabase db, Rope rope, String inspectionType, boolean hasJacket, boolean hasTail) {
        List<Question> questionList = new ArrayList<>();
        String query_hw = "SELECT * FROM " + Contract.QuestionsTable.TABLE_NAME + " WHERE " + Contract.QuestionsTable.TEST + " = \"HARDWARE\"";
        String query_tail = "SELECT * FROM " + Contract.QuestionsTable.TABLE_NAME + " WHERE " + Contract.QuestionsTable.TEST + " = \"TAIL\"";
        String query_storage = "SELECT * FROM " + Contract.QuestionsTable.TABLE_NAME + " WHERE " + Contract.QuestionsTable.TEST + " = \"STORAGE\"";
        String query_rope = "SELECT * FROM " + Contract.QuestionsTable.TABLE_NAME + " WHERE " + Contract.QuestionsTable.TEST + " = \"ROPE\" AND " +
                Contract.QuestionsTable.CONDITION + " IS NULL";
        String query_rope_jacket = "SELECT * FROM " + Contract.QuestionsTable.TABLE_NAME + " WHERE " + Contract.QuestionsTable.TEST + " = \"ROPE\" AND " +
                Contract.QuestionsTable.CONDITION + " = \"DB\"";
        String query_rope_no_jacket = "SELECT * FROM " + Contract.QuestionsTable.TABLE_NAME + " WHERE " + Contract.QuestionsTable.TEST + " = \"ROPE\" AND " +
                Contract.QuestionsTable.CONDITION + " = \"SB\"";

        String query = query_rope + " UNION ALL " + query_hw;

        if(inspectionType.equals("detail")){
            query = query + " UNION ALL " + query_storage;
        }

        if(hasTail) {
            query = query + " UNION ALL " + query_tail;
        }

        if(hasJacket) {
            query = query + " UNION ALL " + query_rope_jacket;
        } else {
            query = query + " UNION ALL " + query_rope_no_jacket;
        }

        Cursor c = db.rawQuery(query, null);

        if(c.moveToFirst()) {
            do {
                Question question = new Question();
                question.setId(c.getInt(c.getColumnIndex(Contract.QuestionsTable._ID)));
                question.setHeader(c.getString(c.getColumnIndex(Contract.QuestionsTable.HEADER)));
                question.setDetails(c.getString(c.getColumnIndex(Contract.QuestionsTable.DETAILS)));
                question.setTest(c.getString(c.getColumnIndex(Contract.QuestionsTable.TEST)));
                question.setRoutine(c.getInt(c.getColumnIndex(Contract.QuestionsTable.IS_ROUTINE)) > 0 ? true : false);
                question.setMaterialType(c.getString(c.getColumnIndex(Contract.QuestionsTable.MATERIAL)));
                question.setOutcome(c.getString(c.getColumnIndex(Contract.QuestionsTable.OUTCOME)));
                question.setOrder(c.getInt(c.getColumnIndex(Contract.QuestionsTable.ORDER)));
                question.setOperation(c.getString(c.getColumnIndex(Contract.QuestionsTable.OPERATION)));
                question.setConditionReply(c.getString(c.getColumnIndex(Contract.QuestionsTable.CONDITION)));
                question.setRisk(c.getString(c.getColumnIndex(Contract.QuestionsTable.RISK)));
                questionList.add(question);
            } while(c.moveToNext());
        }
        c.close();
        return  questionList;
    }
//    @SuppressLint("Range")
//    public static Question getQuestionFromResultDB(SQLiteDatabase db, Result result) {
//        Question question = new Question();
//        String query = "SELECT * FROM " + Contract.QuestionsTable.TABLE_NAME + "q INNER JOIN " + Contract.ResultsTable.TABLE_NAME +
//                "r ON q." + Contract.QuestionsTable._ID + " = r." + Contract.ResultsTable.QUESTION + " WHERE r." +
//                Contract.ResultsTable.QUESTION + " = ?";
//        Cursor c = db.rawQuery(query, new String[] {String.valueOf(result.getQuestionId())});
//
//        if(c.moveToFirst()) {
//            do {
//                question.setId(c.getInt(c.getColumnIndex(Contract.QuestionsTable._ID)));
//                question.setHeader(c.getString(c.getColumnIndex(Contract.QuestionsTable.HEADER)));
//                question.setDetails(c.getString(c.getColumnIndex(Contract.QuestionsTable.DETAILS)));
//                question.setTest(c.getString(c.getColumnIndex(Contract.QuestionsTable.TEST)));
//                question.setRoutine(c.getInt(c.getColumnIndex(Contract.QuestionsTable.IS_ROUTINE)) > 0 ? true : false);
//                question.setMaterialType(c.getString(c.getColumnIndex(Contract.QuestionsTable.MATERIAL)));
//                question.setOutcome(c.getString(c.getColumnIndex(Contract.QuestionsTable.OUTCOME)));
//                question.setOrder(c.getInt(c.getColumnIndex(Contract.QuestionsTable.ORDER)));
//                question.setOperation(c.getString(c.getColumnIndex(Contract.QuestionsTable.OPERATION)));
//                question.setConditionReply(c.getString(c.getColumnIndex(Contract.QuestionsTable.CONDITION)));
//                question.setRisk(c.getString(c.getColumnIndex(Contract.QuestionsTable.RISK)));
//            } while(c.moveToNext());
//        }
//        c.close();
//        return  question;
//    }
    @SuppressLint("Range")
    public static Question getQuestionByIdDB(SQLiteDatabase db, int id) {
        Question question = new Question();
        String query = "SELECT * FROM " + Contract.QuestionsTable.TABLE_NAME + " WHERE " +
                Contract.QuestionsTable._ID + " = ?";
        Cursor c = db.rawQuery(query, new String[] {String.valueOf(id)});

        if(c.moveToFirst()) {
            do {
                question.setId(c.getInt(c.getColumnIndex(Contract.QuestionsTable._ID)));
                question.setHeader(c.getString(c.getColumnIndex(Contract.QuestionsTable.HEADER)));
                question.setDetails(c.getString(c.getColumnIndex(Contract.QuestionsTable.DETAILS)));
                question.setTest(c.getString(c.getColumnIndex(Contract.QuestionsTable.TEST)));
                question.setRoutine(c.getInt(c.getColumnIndex(Contract.QuestionsTable.IS_ROUTINE)) > 0 ? true : false);
                question.setMaterialType(c.getString(c.getColumnIndex(Contract.QuestionsTable.MATERIAL)));
                question.setOutcome(c.getString(c.getColumnIndex(Contract.QuestionsTable.OUTCOME)));
                question.setOrder(c.getInt(c.getColumnIndex(Contract.QuestionsTable.ORDER)));
                question.setOperation(c.getString(c.getColumnIndex(Contract.QuestionsTable.OPERATION)));
                question.setConditionReply(c.getString(c.getColumnIndex(Contract.QuestionsTable.CONDITION)));
                question.setRisk(c.getString(c.getColumnIndex(Contract.QuestionsTable.RISK)));
            } while(c.moveToNext());
        }
        c.close();
        return  question;
    }

    @SuppressLint("Range")
    public static List<Question> getAllQuestionsOfRopeInStorageDB(SQLiteDatabase db, String material) {
        List<Question> questionList = new ArrayList<>();
        String query_storage_ropes = "SELECT * FROM " + Contract.QuestionsTable.TABLE_NAME + " WHERE " + Contract.QuestionsTable.TEST + " = \"STORAGE\"" + " AND " +
                Contract.QuestionsTable.MATERIAL + " = ?";

        Cursor c = db.rawQuery(query_storage_ropes, new String[] {material});

        if(c.moveToFirst()) {
            do {
                Question question = new Question();
                question.setId(c.getInt(c.getColumnIndex(Contract.QuestionsTable._ID)));
                question.setHeader(c.getString(c.getColumnIndex(Contract.QuestionsTable.HEADER)));
                question.setDetails(c.getString(c.getColumnIndex(Contract.QuestionsTable.DETAILS)));
                question.setTest(c.getString(c.getColumnIndex(Contract.QuestionsTable.TEST)));
                question.setRoutine(c.getInt(c.getColumnIndex(Contract.QuestionsTable.IS_ROUTINE)) > 0 ? true : false);
                question.setMaterialType(c.getString(c.getColumnIndex(Contract.QuestionsTable.MATERIAL)));
                question.setOutcome(c.getString(c.getColumnIndex(Contract.QuestionsTable.OUTCOME)));
                question.setOrder(c.getInt(c.getColumnIndex(Contract.QuestionsTable.ORDER)));
                question.setOperation(c.getString(c.getColumnIndex(Contract.QuestionsTable.OPERATION)));
                question.setConditionReply(c.getString(c.getColumnIndex(Contract.QuestionsTable.CONDITION)));
                question.setRisk(c.getString(c.getColumnIndex(Contract.QuestionsTable.RISK)));
                questionList.add(question);
            } while(c.moveToNext());
        }
        c.close();
        return  questionList;
    }
}

