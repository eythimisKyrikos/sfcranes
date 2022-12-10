package com.safeline.safelinecranes.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.safeline.safelinecranes.models.FinishedResults;
import com.safeline.safelinecranes.models.Position;

import java.util.ArrayList;
import java.util.List;

public final class FinishedResultsDBApi {

    public static void addFinishedResultDB(SQLiteDatabase db, FinishedResults results) {
        ContentValues cv = new ContentValues();
        cv.put(Contract.FinishedInspection.INSPECTION_ID, results.getInspection_id());
        cv.put(Contract.FinishedInspection.DATE_FINISHED, results.getDateOfInspection());
        cv.put(Contract.FinishedInspection.RESULTS, results.getResults());
        cv.put(Contract.FinishedInspection.SYNC, false);
        db.insert(Contract.FinishedInspection.TABLE_NAME, null, cv);
    }
    public static boolean addFinishedResultDBWithID(SQLiteDatabase db, FinishedResults results) {
        ContentValues cv = new ContentValues();
        cv.put(Contract.FinishedInspection.INSPECTION_ID, results.getInspection_id());
        cv.put(Contract.FinishedInspection.DATE_FINISHED, results.getDateOfInspection());
        cv.put(Contract.FinishedInspection.RESULTS, results.getResults());
        cv.put(Contract.FinishedInspection.SYNC, false);
        try {
            db.insert(Contract.FinishedInspection.TABLE_NAME, null, cv);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
    public static void updateFinishedResultDB(SQLiteDatabase db, FinishedResults results) {
        ContentValues cv = new ContentValues();
        cv.put(Contract.FinishedInspection.INSPECTION_ID, results.getInspection_id());
        cv.put(Contract.FinishedInspection.DATE_FINISHED, results.getDateOfInspection());
        cv.put(Contract.FinishedInspection.RESULTS, results.getResults());
        cv.put(Contract.FinishedInspection.SYNC, true);
        db.update(Contract.FinishedInspection.TABLE_NAME, cv,
                Contract.FinishedInspection._ID + "=?", new String[] {String.valueOf(results.getId())});
    }
    @SuppressLint("Range")
    public static List<FinishedResults> getFinishedResultsDB(SQLiteDatabase db) {
        List<FinishedResults> finishedResults = new ArrayList<>();
        String query = "SELECT * FROM " + Contract.FinishedInspection.TABLE_NAME;
        Cursor c = db.rawQuery(query, new String[] {});
        if(c.moveToFirst()) {
            do {
                FinishedResults finishedResult = new FinishedResults();
                finishedResult.setId(c.getInt(c.getColumnIndex(Contract.FinishedInspection._ID)));
                finishedResult.setSync(c.getInt(c.getColumnIndex(Contract.FinishedInspection.SYNC)) > 0 ? true : false);
                finishedResult.setInspection_id(c.getInt(c.getColumnIndex(Contract.FinishedInspection.INSPECTION_ID)));
                finishedResult.setDateOfInspection(c.getString(c.getColumnIndex(Contract.FinishedInspection.DATE_FINISHED)));
                finishedResult.setResults(c.getString(c.getColumnIndex(Contract.FinishedInspection.RESULTS)));
                finishedResults.add(finishedResult);
            } while(c.moveToNext());
        }
        c.close();
        return finishedResults;
    }
    @SuppressLint("Range")
    public static List<FinishedResults> getUnSyncedFinishedResultsDB(SQLiteDatabase db) {
        List<FinishedResults> finishedResults = new ArrayList<>();
        String query = "SELECT * FROM " + Contract.FinishedInspection.TABLE_NAME + " WHERE " + Contract.FinishedInspection.SYNC + " = 0";
        Cursor c = db.rawQuery(query, new String[] {});
        if(c.moveToFirst()) {
            do {
                FinishedResults finishedResult = new FinishedResults();
                finishedResult.setId(c.getInt(c.getColumnIndex(Contract.FinishedInspection._ID)));
                finishedResult.setSync(c.getInt(c.getColumnIndex(Contract.FinishedInspection.SYNC)) > 0 ? true : false);
                finishedResult.setInspection_id(c.getInt(c.getColumnIndex(Contract.FinishedInspection.INSPECTION_ID)));
                finishedResult.setDateOfInspection(c.getString(c.getColumnIndex(Contract.FinishedInspection.DATE_FINISHED)));
                finishedResult.setResults(c.getString(c.getColumnIndex(Contract.FinishedInspection.RESULTS)));
                finishedResults.add(finishedResult);
            } while(c.moveToNext());
        }
        c.close();
        return finishedResults;
    }
    @SuppressLint("Range")
    public static FinishedResults getFinishedResultByInspectionDB(SQLiteDatabase db, int inspection_id) {
        FinishedResults finishedResult = new FinishedResults();
        String query = "SELECT * FROM " + Contract.FinishedInspection.TABLE_NAME + " WHERE " + Contract.FinishedInspection.INSPECTION_ID + " = ?";
        Cursor c = db.rawQuery(query, new String[] {String.valueOf(inspection_id)});

        if(c.moveToFirst()) {
            do {
                finishedResult.setId(c.getInt(c.getColumnIndex(Contract.FinishedInspection._ID)));
                finishedResult.setSync(c.getInt(c.getColumnIndex(Contract.FinishedInspection.SYNC)) > 0 ? true : false);
                finishedResult.setInspection_id(c.getInt(c.getColumnIndex(Contract.FinishedInspection.INSPECTION_ID)));
                finishedResult.setDateOfInspection(c.getString(c.getColumnIndex(Contract.FinishedInspection.DATE_FINISHED)));
                finishedResult.setResults(c.getString(c.getColumnIndex(Contract.FinishedInspection.RESULTS)));
            } while(c.moveToNext());
        }
        c.close();
        return finishedResult;
    }
    @SuppressLint("Range")
    public static FinishedResults getLastFinishedResultDB(SQLiteDatabase db) {
        FinishedResults finishedResult = new FinishedResults();
        String query = "SELECT * FROM " + Contract.FinishedInspection.TABLE_NAME + " ORDER BY " + Contract.FinishedInspection.DATE_FINISHED + ", " + Contract.FinishedInspection._ID + " DESC LIMIT 1";
        Cursor c = db.rawQuery(query, null);

        if(c.moveToFirst()) {
            do {
                finishedResult.setId(c.getInt(c.getColumnIndex(Contract.FinishedInspection._ID)));
                finishedResult.setSync(c.getInt(c.getColumnIndex(Contract.FinishedInspection.SYNC)) > 0 ? true : false);
                finishedResult.setInspection_id(c.getInt(c.getColumnIndex(Contract.FinishedInspection.INSPECTION_ID)));
                finishedResult.setDateOfInspection(c.getString(c.getColumnIndex(Contract.FinishedInspection.DATE_FINISHED)));
                finishedResult.setResults(c.getString(c.getColumnIndex(Contract.FinishedInspection.RESULTS)));
            } while(c.moveToNext());
        }
        c.close();
        return finishedResult;
    }
    @SuppressLint("Range")
    public static FinishedResults getLastFinishedResultForPositionDB(SQLiteDatabase db, Position position) {
        FinishedResults finishedResult = new FinishedResults();
        String query = "SELECT * FROM " + Contract.FinishedInspection.TABLE_NAME + " WHERE " + Contract.FinishedInspection.INSPECTION_ID +
                " IN (SELECT " + Contract.InspectionTable._ID + " FROM " + Contract.InspectionTable.TABLE_NAME + " WHERE " + Contract.InspectionTable.POSITION + " = ?) " +
                " ORDER BY " + Contract.FinishedInspection.DATE_FINISHED + ", " + Contract.FinishedInspection._ID + " DESC LIMIT 1";
        Cursor c = db.rawQuery(query, new String[] {String.valueOf(position.getId())});

        if(c.moveToFirst()) {
            do {
                finishedResult.setId(c.getInt(c.getColumnIndex(Contract.FinishedInspection._ID)));
                finishedResult.setSync(c.getInt(c.getColumnIndex(Contract.FinishedInspection.SYNC)) > 0 ? true : false);
                finishedResult.setInspection_id(c.getInt(c.getColumnIndex(Contract.FinishedInspection.INSPECTION_ID)));
                finishedResult.setDateOfInspection(c.getString(c.getColumnIndex(Contract.FinishedInspection.DATE_FINISHED)));
                finishedResult.setResults(c.getString(c.getColumnIndex(Contract.FinishedInspection.RESULTS)));
            } while(c.moveToNext());
        }
        c.close();
        return finishedResult;
    }
}
