package com.safeline.safelinecranes.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.safeline.safelinecranes.models.RopeType;

import java.util.ArrayList;
import java.util.List;

public final class RopeTypeDBApi {

    public static void addRopeTypeDB(SQLiteDatabase db, RopeType ropeType) {
        ContentValues cv = new ContentValues();
        cv.put(Contract.RopeTypeTable.TYPE, ropeType.getType());
        cv.put(Contract.RopeTypeTable.MANUFACTURER, ropeType.getManufacturer());
        cv.put(Contract.RopeTypeTable.MODEL, ropeType.getModel());
        cv.put(Contract.RopeTypeTable.DIAMETER, ropeType.getDiameter());
        cv.put(Contract.RopeTypeTable.LENGTH, ropeType.getLength());
        cv.put(Contract.RopeTypeTable.MATERIAL, ropeType.getMaterial());
        cv.put(Contract.RopeTypeTable.BREAKING_FORCE, ropeType.getBreakingForce());
        cv.put(Contract.RopeTypeTable.SYNC, false);
        db.insert(Contract.RopeTypeTable.TABLE_NAME, null, cv);
    }
    public static boolean addRopeTypeDBWithID(SQLiteDatabase db, RopeType ropeType) {
        ContentValues cv = new ContentValues();
        cv.put(Contract.RopeTypeTable._ID, ropeType.getId());
        cv.put(Contract.RopeTypeTable.TYPE, ropeType.getType());
        cv.put(Contract.RopeTypeTable.MANUFACTURER, ropeType.getManufacturer());
        cv.put(Contract.RopeTypeTable.MODEL, ropeType.getModel());
        cv.put(Contract.RopeTypeTable.DIAMETER, ropeType.getDiameter());
        cv.put(Contract.RopeTypeTable.LENGTH, ropeType.getLength());
        cv.put(Contract.RopeTypeTable.MATERIAL, ropeType.getMaterial());
        cv.put(Contract.RopeTypeTable.BREAKING_FORCE, ropeType.getBreakingForce());
        cv.put(Contract.RopeTypeTable.SYNC, false);
        try {
            db.insert(Contract.RopeTypeTable.TABLE_NAME, null, cv);
            return  true;
        } catch (Exception ex) {
            return false;
        }
    }
    public static void updateRopeTypeDB(SQLiteDatabase db, RopeType ropeType) {
        ContentValues cv = new ContentValues();
        cv.put(Contract.RopeTypeTable.TYPE, ropeType.getType());
        cv.put(Contract.RopeTypeTable.MANUFACTURER, ropeType.getManufacturer());
        cv.put(Contract.RopeTypeTable.MODEL, ropeType.getModel());
        cv.put(Contract.RopeTypeTable.DIAMETER, ropeType.getDiameter());
        cv.put(Contract.RopeTypeTable.LENGTH, ropeType.getLength());
        cv.put(Contract.RopeTypeTable.MATERIAL, ropeType.getMaterial());
        cv.put(Contract.RopeTypeTable.BREAKING_FORCE, ropeType.getBreakingForce());
        cv.put(Contract.RopeTypeTable.SYNC, false);
        db.update(Contract.RopeTypeTable.TABLE_NAME, cv, "_ID = ?", new String[] {String.valueOf(ropeType.getId())});
    }
    @SuppressLint("Range")
    public static List<RopeType> getAllRopeTypesDB(SQLiteDatabase db) {
        List<RopeType> ropeTypesList = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM " + Contract.RopeTypeTable.TABLE_NAME, null);

        if(c.moveToFirst()) {
            do {
                RopeType ropetype = new RopeType();
                ropetype.setId(c.getInt(c.getColumnIndex(Contract.RopeTypeTable._ID)));
                ropetype.setType(c.getString(c.getColumnIndex(Contract.RopeTypeTable.TYPE)));
                ropetype.setManufacturer(c.getString(c.getColumnIndex(Contract.RopeTypeTable.MANUFACTURER)));
                ropetype.setModel(c.getString(c.getColumnIndex(Contract.RopeTypeTable.MODEL)));
                ropetype.setDiameter(c.getInt(c.getColumnIndex(Contract.RopeTypeTable.DIAMETER)));
                ropetype.setLength(c.getFloat(c.getColumnIndex(Contract.RopeTypeTable.LENGTH)));
                ropetype.setMaterial(c.getString(c.getColumnIndex(Contract.RopeTypeTable.MATERIAL)));
                ropetype.setBreakingForce(c.getFloat(c.getColumnIndex(Contract.RopeTypeTable.BREAKING_FORCE)));
                ropetype.setSync(c.getInt(c.getColumnIndex(Contract.RopeTypeTable.SYNC)) > 0 ? true : false);
                ropeTypesList.add(ropetype);
            } while(c.moveToNext());
        }
        c.close();
        return  ropeTypesList;
    }
    @SuppressLint("Range")
    public static RopeType getRopeTypeByIdDB(SQLiteDatabase db, int id) {
        RopeType type = new RopeType();
        Cursor c = db.rawQuery("SELECT * FROM " + Contract.RopeTypeTable.TABLE_NAME + " WHERE " + Contract.RopeTypeTable._ID +" = ?", new String[] {String.valueOf(id)});

        if(c.moveToFirst()) {
            do {
                type.setId(c.getInt(c.getColumnIndex(Contract.RopeTypeTable._ID)));
                type.setType(c.getString(c.getColumnIndex(Contract.RopeTypeTable.TYPE)));
                type.setManufacturer(c.getString(c.getColumnIndex(Contract.RopeTypeTable.MANUFACTURER)));
                type.setModel(c.getString(c.getColumnIndex(Contract.RopeTypeTable.MODEL)));
                type.setDiameter(c.getInt(c.getColumnIndex(Contract.RopeTypeTable.DIAMETER)));
                type.setLength(c.getFloat(c.getColumnIndex(Contract.RopeTypeTable.LENGTH)));
                type.setMaterial(c.getString(c.getColumnIndex(Contract.RopeTypeTable.MATERIAL)));
                type.setBreakingForce(c.getFloat(c.getColumnIndex(Contract.RopeTypeTable.BREAKING_FORCE)));
                type.setSync(c.getInt(c.getColumnIndex(Contract.RopeTypeTable.SYNC)) > 0 ? true : false);
            } while(c.moveToNext());
        }
        c.close();
        return  type;
    }
    public static void deleteRopeTypeDB(SQLiteDatabase db, int ropeType_id) {
        db.delete(Contract.RopeTypeTable.TABLE_NAME, "_ID=?", new String[] {String.valueOf(ropeType_id)});
    }

    /* SYNC TABLE ROUTINES */
    public static void updateSyncRopeTypeDB(SQLiteDatabase db, RopeType ropeType) {
        ContentValues cv = new ContentValues();
        cv.put(Contract.RopeTypeTable.TYPE, ropeType.getType());
        cv.put(Contract.RopeTypeTable.MANUFACTURER, ropeType.getManufacturer());
        cv.put(Contract.RopeTypeTable.MODEL, ropeType.getModel());
        cv.put(Contract.RopeTypeTable.DIAMETER, ropeType.getDiameter());
        cv.put(Contract.RopeTypeTable.LENGTH, ropeType.getLength());
        cv.put(Contract.RopeTypeTable.MATERIAL, ropeType.getMaterial());
        cv.put(Contract.RopeTypeTable.BREAKING_FORCE, ropeType.getBreakingForce());
        cv.put(Contract.RopeTypeTable.SYNC, false);
        db.update(Contract.RopeTypeTable.TABLE_NAME, cv, "_ID = ?", new String[] {String.valueOf(ropeType.getId())});
    }
    public static void updateRopeTypeMakeSyncDB(SQLiteDatabase db, RopeType ropeType) {
        ContentValues cv = new ContentValues();
        cv.put(Contract.RopeTypeTable.TYPE, ropeType.getType());
        cv.put(Contract.RopeTypeTable.MANUFACTURER, ropeType.getManufacturer());
        cv.put(Contract.RopeTypeTable.MODEL, ropeType.getModel());
        cv.put(Contract.RopeTypeTable.DIAMETER, ropeType.getDiameter());
        cv.put(Contract.RopeTypeTable.LENGTH, ropeType.getLength());
        cv.put(Contract.RopeTypeTable.MATERIAL, ropeType.getMaterial());
        cv.put(Contract.RopeTypeTable.BREAKING_FORCE, ropeType.getBreakingForce());
        cv.put(Contract.RopeTypeTable.SYNC, true);
        db.update(Contract.RopeTypeTable.TABLE_NAME, cv, "_ID = ?", new String[] {String.valueOf(ropeType.getId())});
    }
    @SuppressLint("Range")
    public static List<RopeType> getAllNonSyncRopeTypesDB(SQLiteDatabase db) {
        List<RopeType> ropeTypesList = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM " + Contract.RopeTypeTable.TABLE_NAME +
                " WHERE " + Contract.RopeTypeTable.SYNC + " = 0",null);

        if(c.moveToFirst()) {
            do {
                RopeType ropetype = new RopeType();
                ropetype.setId(c.getInt(c.getColumnIndex(Contract.RopeTypeTable._ID)));
                ropetype.setType(c.getString(c.getColumnIndex(Contract.RopeTypeTable.TYPE)));
                ropetype.setManufacturer(c.getString(c.getColumnIndex(Contract.RopeTypeTable.MANUFACTURER)));
                ropetype.setModel(c.getString(c.getColumnIndex(Contract.RopeTypeTable.MODEL)));
                ropetype.setDiameter(c.getInt(c.getColumnIndex(Contract.RopeTypeTable.DIAMETER)));
                ropetype.setLength(c.getFloat(c.getColumnIndex(Contract.RopeTypeTable.LENGTH)));
                ropetype.setMaterial(c.getString(c.getColumnIndex(Contract.RopeTypeTable.MATERIAL)));
                ropetype.setBreakingForce(c.getFloat(c.getColumnIndex(Contract.RopeTypeTable.BREAKING_FORCE)));
                ropetype.setSync(c.getInt(c.getColumnIndex(Contract.RopeTypeTable.SYNC)) > 0 ? true : false);
                ropeTypesList.add(ropetype);
            } while(c.moveToNext());
        }
        c.close();
        return  ropeTypesList;
    }
}
