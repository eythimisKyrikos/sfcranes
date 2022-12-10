package com.safeline.safelinecranes.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.safeline.safelinecranes.models.FinishedResults;
import com.safeline.safelinecranes.models.Inspection;

import java.util.ArrayList;
import java.util.List;

public final class InspectionDBApi {

    @SuppressLint("Range")
    public static List<Inspection> getAllInspectionsDB(SQLiteDatabase db) {
        List<Inspection> inspectionList = new ArrayList<>();

        Cursor c = db.rawQuery("SELECT * FROM " + Contract.InspectionTable.TABLE_NAME, null);

        if(c.moveToFirst()) {
            do {
                Inspection inspection = new Inspection();
                inspection.setId(c.getInt(c.getColumnIndex(Contract.InspectionTable._ID)));
                inspection.setWorkHours(c.getInt(c.getColumnIndex(Contract.InspectionTable.WORK_HOURS)));
                inspection.setFinished(c.getInt(c.getColumnIndex(Contract.InspectionTable.IS_FINISHED)) > 0 ? true : false);
                inspection.setStep(c.getInt(c.getColumnIndex(Contract.InspectionTable.STEP)));
                inspection.setDate_created(c.getString(c.getColumnIndex(Contract.InspectionTable.DATE_CREATED)));
                inspection.setDate_finished(c.getString(c.getColumnIndex(Contract.InspectionTable.DATE_FINISHED)));
                inspection.setPosition(c.getInt(c.getColumnIndex(Contract.InspectionTable.WORK_HOURS)));
                inspection.setPosition(c.getInt(c.getColumnIndex(Contract.InspectionTable.POSITION)));
                inspectionList.add(inspection);
            } while(c.moveToNext());
        }
        c.close();
        return  inspectionList;
    }
    @SuppressLint("Range")
    public static Inspection getInspectionByIdDB(SQLiteDatabase db, int id) {
        Inspection inspection = new Inspection();
        Cursor c = db.rawQuery("SELECT * FROM " + Contract.InspectionTable.TABLE_NAME + " WHERE " + Contract.InspectionTable._ID + " = ?", new String[] {String.valueOf(id)});

        if(c.moveToFirst()) {
            do {
                inspection.setId(c.getInt(c.getColumnIndex(Contract.InspectionTable._ID)));
                inspection.setWorkHours(c.getInt(c.getColumnIndex(Contract.InspectionTable.WORK_HOURS)));
                inspection.setFinished(c.getInt(c.getColumnIndex(Contract.InspectionTable.IS_FINISHED)) > 0 ? true : false);
                inspection.setStep(c.getInt(c.getColumnIndex(Contract.InspectionTable.STEP)));
                inspection.setDate_created(c.getString(c.getColumnIndex(Contract.InspectionTable.DATE_CREATED)));
                inspection.setDate_finished(c.getString(c.getColumnIndex(Contract.InspectionTable.DATE_FINISHED)));
                inspection.setPosition(c.getInt(c.getColumnIndex(Contract.InspectionTable.POSITION)));
            } while(c.moveToNext());
        }
        c.close();
        return  inspection;
    }
    @SuppressLint("Range")
    public static Inspection getLastInsertedInspectionDB(SQLiteDatabase db){
        Inspection inspection = new Inspection();
        Cursor c = db.rawQuery("SELECT * FROM " + Contract.InspectionTable.TABLE_NAME + " WHERE " +
                Contract.InspectionTable._ID + " = (SELECT MAX("+  Contract.InspectionTable._ID +
                ") FROM " + Contract.InspectionTable.TABLE_NAME + ") AND " + Contract.InspectionTable.POSITION + " = ?", new String[] {});

        if(c.moveToFirst()) {
            do {
                inspection.setId(c.getInt(c.getColumnIndex(Contract.InspectionTable._ID)));
                inspection.setWorkHours(c.getInt(c.getColumnIndex(Contract.InspectionTable.WORK_HOURS)));
                inspection.setFinished(c.getInt(c.getColumnIndex(Contract.InspectionTable.IS_FINISHED)) > 0 ? true : false);
                inspection.setStep(c.getInt(c.getColumnIndex(Contract.InspectionTable.STEP)));
                inspection.setDate_created(c.getString(c.getColumnIndex(Contract.InspectionTable.DATE_CREATED)));
                inspection.setDate_finished(c.getString(c.getColumnIndex(Contract.InspectionTable.DATE_FINISHED)));
                inspection.setPosition(c.getInt(c.getColumnIndex(Contract.InspectionTable.POSITION)));
            } while(c.moveToNext());
        }
        c.close();
        return  inspection;
    }
    @SuppressLint("Range")
    public static Inspection getLastInsertedInspectionInPositionDB(SQLiteDatabase db, int positionID){
        Inspection inspection = new Inspection();
        Cursor c = db.rawQuery("SELECT * FROM " + Contract.InspectionTable.TABLE_NAME + " WHERE " +
                Contract.InspectionTable._ID + " = (SELECT MAX("+  Contract.InspectionTable._ID +
                ") FROM " + Contract.InspectionTable.TABLE_NAME + " WHERE " + Contract.InspectionTable.POSITION + " = ?)", new String[] {String.valueOf(positionID)});

        if(c.moveToFirst()) {
            do {
                inspection.setId(c.getInt(c.getColumnIndex(Contract.InspectionTable._ID)));
                inspection.setWorkHours(c.getInt(c.getColumnIndex(Contract.InspectionTable.WORK_HOURS)));
                inspection.setFinished(c.getInt(c.getColumnIndex(Contract.InspectionTable.IS_FINISHED)) > 0 ? true : false);
                inspection.setStep(c.getInt(c.getColumnIndex(Contract.InspectionTable.STEP)));
                inspection.setDate_created(c.getString(c.getColumnIndex(Contract.InspectionTable.DATE_CREATED)));
                inspection.setDate_finished(c.getString(c.getColumnIndex(Contract.InspectionTable.DATE_FINISHED)));
                inspection.setPosition(c.getInt(c.getColumnIndex(Contract.InspectionTable.POSITION)));
            } while(c.moveToNext());
        }
        c.close();
        return  inspection;
    }
    @SuppressLint("Range")
    public static List<Inspection> getAllInspectionsInPositionDB(SQLiteDatabase db, int positionID){
        List<Inspection> inspectionList = new ArrayList<>();

        Cursor c = db.rawQuery("SELECT * FROM " + Contract.InspectionTable.TABLE_NAME + " WHERE " +
                     Contract.InspectionTable.POSITION + " = ? ORDER BY " + Contract.InspectionTable.DATE_FINISHED + " DESC", new String[] {String.valueOf(positionID)});

        if(c.moveToFirst()) {
            do {
                Inspection inspection = new Inspection();
                inspection.setId(c.getInt(c.getColumnIndex(Contract.InspectionTable._ID)));
                inspection.setWorkHours(c.getInt(c.getColumnIndex(Contract.InspectionTable.WORK_HOURS)));
                inspection.setFinished(c.getInt(c.getColumnIndex(Contract.InspectionTable.IS_FINISHED)) > 0 ? true : false);
                inspection.setStep(c.getInt(c.getColumnIndex(Contract.InspectionTable.STEP)));
                inspection.setDate_created(c.getString(c.getColumnIndex(Contract.InspectionTable.DATE_CREATED)));
                inspection.setDate_finished(c.getString(c.getColumnIndex(Contract.InspectionTable.DATE_FINISHED)));
                inspection.setPosition(c.getInt(c.getColumnIndex(Contract.InspectionTable.POSITION)));
                inspectionList.add(inspection);
            } while(c.moveToNext());
        }
        c.close();
        return  inspectionList;
    }
    public static void addInspectionDB(SQLiteDatabase db, Inspection inspection) {
        ContentValues cv = new ContentValues();
        cv.put(Contract.InspectionTable.WORK_HOURS, inspection.getWorkHours());
        cv.put(Contract.InspectionTable.IS_FINISHED, inspection.isFinished());
        cv.put(Contract.InspectionTable.STEP, inspection.getStep());
        cv.put(Contract.InspectionTable.DATE_CREATED, inspection.getDate_created());
        cv.put(Contract.InspectionTable.POSITION, inspection.getPosition());
        db.insert(Contract.InspectionTable.TABLE_NAME, null, cv);
    }
    public static boolean addInspectionDBWithID(SQLiteDatabase db, Inspection inspection) {
        ContentValues cv = new ContentValues();
        cv.put(Contract.InspectionTable._ID, inspection.getId());
        cv.put(Contract.InspectionTable.WORK_HOURS, inspection.getWorkHours());
        cv.put(Contract.InspectionTable.IS_FINISHED, inspection.isFinished());
        cv.put(Contract.InspectionTable.STEP, inspection.getStep());
        cv.put(Contract.InspectionTable.DATE_CREATED, inspection.getDate_created());
        cv.put(Contract.InspectionTable.DATE_FINISHED, inspection.getDate_finished());
        cv.put(Contract.InspectionTable.POSITION, inspection.getPosition());
        try {
            db.insert(Contract.InspectionTable.TABLE_NAME, null, cv);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
    public static void updateInspectionDB(SQLiteDatabase db, Inspection inspection) {
        ContentValues cv = new ContentValues();
        cv.put(Contract.InspectionTable.WORK_HOURS, inspection.getWorkHours());
        cv.put(Contract.InspectionTable.IS_FINISHED, inspection.isFinished());
        cv.put(Contract.InspectionTable.STEP, inspection.getStep());
        cv.put(Contract.InspectionTable.DATE_CREATED, inspection.getDate_created());
        cv.put(Contract.InspectionTable.DATE_FINISHED, inspection.getDate_finished());
        cv.put(Contract.InspectionTable.POSITION, inspection.getPosition());
        db.update(Contract.InspectionTable.TABLE_NAME, cv, "_ID = ?", new String[] {String.valueOf(inspection.getId())});
    }

    @SuppressLint("Range")
    public static List<Inspection> getAllInspectionsOfUnsyncedDB(SQLiteDatabase db, List<FinishedResults> unsyncedResults){
        List<Inspection> inspections = new ArrayList<>();
        String sqlQuery = "SELECT * FROM " + Contract.InspectionTable.TABLE_NAME + " WHERE " + Contract.InspectionTable.IS_FINISHED +
                " = 1 AND " + Contract.InspectionTable._ID + " IN (";
        for(int i=0; i<unsyncedResults.size(); i++) {
            sqlQuery = sqlQuery + unsyncedResults.get(i).getInspection_id();
            sqlQuery = sqlQuery + ",";
        }
        sqlQuery = (sqlQuery.substring(0, sqlQuery.length() - 1));
        sqlQuery = sqlQuery + ")";
        Cursor c = db.rawQuery(sqlQuery, null);
        if(c.moveToFirst()) {
            do {
                Inspection inspection = new Inspection();
                inspection.setId(c.getInt(c.getColumnIndex(Contract.InspectionTable._ID)));
                inspection.setWorkHours(c.getInt(c.getColumnIndex(Contract.InspectionTable.WORK_HOURS)));
                inspection.setFinished(c.getInt(c.getColumnIndex(Contract.InspectionTable.IS_FINISHED)) > 0 ? true : false);
                inspection.setStep(c.getInt(c.getColumnIndex(Contract.InspectionTable.STEP)));
                inspection.setPosition(c.getInt(c.getColumnIndex(Contract.InspectionTable.POSITION)));
                inspection.setDate_created(c.getString(c.getColumnIndex(Contract.InspectionTable.DATE_CREATED)));
                inspection.setDate_finished(c.getString(c.getColumnIndex(Contract.InspectionTable.DATE_FINISHED)));
                inspections.add(inspection);
            } while(c.moveToNext());
        }
        c.close();
        return inspections;
    }
}
