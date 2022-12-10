package com.safeline.safelinecranes.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.safeline.safelinecranes.models.Position;

import java.util.ArrayList;
import java.util.List;

public final class PositionDBApi {

    public static void addPositionDB(SQLiteDatabase db, Position position) {
        ContentValues cv = new ContentValues();
        cv.put(Contract.PositionTable.NAME, position.getName());
        cv.put(Contract.PositionTable.X, position.getX());
        cv.put(Contract.PositionTable.Y, position.getY());
        cv.put(Contract.PositionTable.SYNC, false);
        cv.put(Contract.PositionTable.STORAGE, position.isStorage());
        cv.put(Contract.PositionTable.TYPE, position.getType_of_work());
        db.insert(Contract.PositionTable.TABLE_NAME, null, cv);
    }
    public static boolean addPositionDBWithID(SQLiteDatabase db, Position position) {
        ContentValues cv = new ContentValues();
        cv.put(Contract.PositionTable._ID, position.getId());
        cv.put(Contract.PositionTable.NAME, position.getName());
        cv.put(Contract.PositionTable.X, position.getX());
        cv.put(Contract.PositionTable.Y, position.getY());
        cv.put(Contract.PositionTable.SYNC, false);
        cv.put(Contract.PositionTable.STORAGE, position.isStorage());
        cv.put(Contract.PositionTable.TYPE, position.getType_of_work());
        try {
            db.insert(Contract.PositionTable.TABLE_NAME, null, cv);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }
    public static void updatePositionDB(SQLiteDatabase db, Position position) {
        ContentValues cv = new ContentValues();
        cv.put(Contract.PositionTable.NAME, position.getName());
        cv.put(Contract.PositionTable.X, position.getX());
        cv.put(Contract.PositionTable.Y, position.getY());
        cv.put(Contract.PositionTable.SYNC, false);
        cv.put(Contract.PositionTable.TYPE, position.getType_of_work());
        cv.put(Contract.PositionTable.STORAGE, position.isStorage());
        db.update(Contract.PositionTable.TABLE_NAME, cv, "_ID = ?", new String[] {String.valueOf(position.getId())});
    }
    public static void deletePositionDB(SQLiteDatabase db, Position position) {
        db.delete(Contract.PositionTable.TABLE_NAME, "name=?", new String[] {String.valueOf(position.getName())});
    }
    @SuppressLint("Range")
    public static List<Position> getAllPositionsDB(SQLiteDatabase db) {
        List<Position> positionList = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM " + Contract.PositionTable.TABLE_NAME + " WHERE " + Contract.PositionTable.STORAGE + " = 0" , null);
        if(c.moveToFirst()) {
            do {
                Position position = new Position();
                position.setId(c.getInt(c.getColumnIndex(Contract.PositionTable._ID)));
                position.setName(c.getString(c.getColumnIndex(Contract.PositionTable.NAME)));
                position.setType_of_work(c.getString(c.getColumnIndex(Contract.PositionTable.TYPE)));
                position.setX(c.getInt(c.getColumnIndex(Contract.PositionTable.X)));
                position.setY(c.getInt(c.getColumnIndex(Contract.PositionTable.Y)));
                position.setStorage(c.getInt(c.getColumnIndex(Contract.PositionTable.STORAGE)) > 0 ? true : false);
                positionList.add(position);
            } while(c.moveToNext());
        }
        c.close();
        return  positionList;
    }
    public static void updatePositionMakeSyncDB(SQLiteDatabase db, Position position) {
        ContentValues cv = new ContentValues();
        cv.put(Contract.PositionTable.NAME, position.getName());
        cv.put(Contract.PositionTable.X, position.getX());
        cv.put(Contract.PositionTable.Y, position.getY());
        cv.put(Contract.PositionTable.SYNC, true);
        cv.put(Contract.PositionTable.STORAGE, position.isStorage());
        cv.put(Contract.PositionTable.TYPE, position.getType_of_work());
        db.update(Contract.PositionTable.TABLE_NAME, cv, "_ID = ?", new String[] {String.valueOf(position.getId())});
    }
    @SuppressLint("Range")
    public static List<Position> getAllPositionsWithStorageDB(SQLiteDatabase db) {
        List<Position> positionList = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM " + Contract.PositionTable.TABLE_NAME, null);
        if(c.moveToFirst()) {
            do {
                Position position = new Position();
                position.setId(c.getInt(c.getColumnIndex(Contract.PositionTable._ID)));
                position.setName(c.getString(c.getColumnIndex(Contract.PositionTable.NAME)));
                position.setType_of_work(c.getString(c.getColumnIndex(Contract.PositionTable.TYPE)));
                position.setX(c.getInt(c.getColumnIndex(Contract.PositionTable.X)));
                position.setY(c.getInt(c.getColumnIndex(Contract.PositionTable.Y)));
                position.setStorage(c.getInt(c.getColumnIndex(Contract.PositionTable.STORAGE)) > 0 ? true : false);
                positionList.add(position);
            } while(c.moveToNext());
        }
        c.close();
        return  positionList;
    }
    @SuppressLint("Range")
    public static Position getPositionByNameDB(SQLiteDatabase db, String name) {
        Position position = new Position();
        Cursor c = db.rawQuery("SELECT * FROM " + Contract.PositionTable.TABLE_NAME + " WHERE " + Contract.PositionTable.NAME + "=?", new String[] {name});
        if(c.moveToFirst()) {
            do {
                position.setId(c.getInt(c.getColumnIndex(Contract.PositionTable._ID)));
                position.setName(c.getString(c.getColumnIndex(Contract.PositionTable.NAME)));
                position.setType_of_work(c.getString(c.getColumnIndex(Contract.PositionTable.TYPE)));
                position.setX(c.getInt(c.getColumnIndex(Contract.PositionTable.X)));
                position.setY(c.getInt(c.getColumnIndex(Contract.PositionTable.Y)));
                position.setStorage(c.getInt(c.getColumnIndex(Contract.PositionTable.STORAGE)) > 0 ? true : false);
            } while(c.moveToNext());
        }
        c.close();
        return  position;
    }
    @SuppressLint("Range")
    public static Position getPositionByIdDB(SQLiteDatabase db, int id) {
        Position position = new Position();
        Cursor c = db.rawQuery("SELECT * FROM " + Contract.PositionTable.TABLE_NAME + " WHERE " + Contract.PositionTable._ID + "=?", new String[] {String.valueOf(id)});
        if(c.moveToFirst()) {
            do {
                position.setId(c.getInt(c.getColumnIndex(Contract.PositionTable._ID)));
                position.setName(c.getString(c.getColumnIndex(Contract.PositionTable.NAME)));
                position.setType_of_work(c.getString(c.getColumnIndex(Contract.PositionTable.TYPE)));
                position.setX(c.getInt(c.getColumnIndex(Contract.PositionTable.X)));
                position.setY(c.getInt(c.getColumnIndex(Contract.PositionTable.Y)));
                position.setStorage(c.getInt(c.getColumnIndex(Contract.PositionTable.STORAGE)) > 0 ? true : false);
            } while(c.moveToNext());
        }
        c.close();
        return  position;
    }
    @SuppressLint("Range")
    public static List<Position> getAllNonSyncPositionsDB(SQLiteDatabase db) {
        List<Position> positionList = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM " + Contract.PositionTable.TABLE_NAME + " WHERE " + Contract.PositionTable.SYNC +
                " = 0",null);
        if(c.moveToFirst()) {
            do {
                Position position = new Position();
                position.setId(c.getInt(c.getColumnIndex(Contract.PositionTable._ID)));
                position.setName(c.getString(c.getColumnIndex(Contract.PositionTable.NAME)));
                position.setType_of_work(c.getString(c.getColumnIndex(Contract.PositionTable.TYPE)));
                position.setX(c.getInt(c.getColumnIndex(Contract.PositionTable.X)));
                position.setY(c.getInt(c.getColumnIndex(Contract.PositionTable.Y)));
                position.setStorage(c.getInt(c.getColumnIndex(Contract.PositionTable.STORAGE)) > 0 ? true : false);
                positionList.add(position);
            } while(c.moveToNext());
        }
        c.close();
        return  positionList;
    }
    public static void updatePositionDBSynced(SQLiteDatabase db, Position position) {
        ContentValues cv = new ContentValues();
        cv.put(Contract.PositionTable.NAME, position.getName());
        cv.put(Contract.PositionTable.TYPE, position.getType_of_work());
        cv.put(Contract.PositionTable.X, position.getX());
        cv.put(Contract.PositionTable.Y, position.getY());
        cv.put(Contract.PositionTable.SYNC, true);
        cv.put(Contract.PositionTable.STORAGE, position.isStorage());
        db.update(Contract.PositionTable.TABLE_NAME, cv, "_ID = ?", new String[] {String.valueOf(position.getId())});
    }
}
