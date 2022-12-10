package com.safeline.safelinecranes.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.safeline.safelinecranes.models.DeletedObject;

import java.util.ArrayList;
import java.util.List;

public final class SyncDBApi {

    public static void addObjectToDeleteDB(SQLiteDatabase db, String type, int id) {
        ContentValues cv = new ContentValues();
        cv.put(Contract.SyncDeleteTable.TYPE, type);
        cv.put(Contract.SyncDeleteTable.KEY, id);
        db.insert(Contract.SyncDeleteTable.TABLE_NAME, null, cv);
    }

    public static void deleteObjectToDeleteDB(SQLiteDatabase db, int id) {
        db.delete(Contract.SyncDeleteTable.TABLE_NAME, Contract.SyncDeleteTable._ID + "= ?", new String[] {String.valueOf(id)});
    }

    @SuppressLint("Range")
    public static List<DeletedObject> getDeletedObjectsDB(SQLiteDatabase db) {
        List<DeletedObject> deletedObjects = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM " + Contract.SyncDeleteTable.TABLE_NAME, null);
        if(c.moveToFirst()) {
            do {
                DeletedObject deletedObject = new DeletedObject();
                deletedObject.setId(c.getInt(c.getColumnIndex(Contract.SyncDeleteTable._ID)));
                deletedObject.setType(c.getString(c.getColumnIndex(Contract.SyncDeleteTable.TYPE)));
                deletedObject.setKey(c.getInt(c.getColumnIndex(Contract.SyncDeleteTable.KEY)));
                deletedObjects.add(deletedObject);
            } while(c.moveToNext());
        }
        c.close();
        return  deletedObjects;
    }

    @SuppressLint("Range")
    public static boolean objectExist(SQLiteDatabase db, String type, int key) {
        List<DeletedObject> delObjects = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM " + Contract.SyncDeleteTable.TABLE_NAME + " WHERE " + Contract.SyncDeleteTable.TYPE + " = '" + type + "' AND " +
                Contract.SyncDeleteTable.KEY + "=" + key, null);
        if(c.moveToFirst()) {
            do {
                DeletedObject deletedObj = new DeletedObject();
                deletedObj.setId(c.getInt(c.getColumnIndex(Contract.SyncDeleteTable._ID)));
                deletedObj.setType(c.getString(c.getColumnIndex(Contract.SyncDeleteTable.TYPE)));
                deletedObj.setKey(c.getInt(c.getColumnIndex(Contract.SyncDeleteTable.KEY)));
                delObjects.add(deletedObj);
            } while(c.moveToNext());
        }
        c.close();
        if(delObjects.size()>0){
            return true;
        } else {
            return false;
        }
    }

    public static boolean objectSynced(SQLiteDatabase db, String type, int key) {
        List<DeletedObject> delObjects = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM " + type + " WHERE sync = 1 AND _id = " + key, null);
        if(c.getCount() > 0){
            c.close();
            return true;
        } else {
            c.close();
            return false;
        }
    }
}

