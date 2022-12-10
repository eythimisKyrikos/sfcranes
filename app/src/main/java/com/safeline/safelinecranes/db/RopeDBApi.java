package com.safeline.safelinecranes.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.safeline.safelinecranes.models.Position;
import com.safeline.safelinecranes.models.Rope;
import com.safeline.safelinecranes.models.RopeType;

import java.util.ArrayList;
import java.util.List;

public final class RopeDBApi {

    public static void addRopeDB(SQLiteDatabase db, Rope rope) {
        ContentValues cv = new ContentValues();
        cv.put(Contract.RopeTable.TYPE, rope.getType_id());
        cv.put(Contract.RopeTable.POSITION, rope.getPosition_id());
        cv.put(Contract.RopeTable.CERTIFICATE_DATE, rope.getDate());
        cv.put(Contract.RopeTable.SERIAL_NUMBER, rope.getSerialNr());
        cv.put(Contract.RopeTable.WORK_HOURS, rope.getWorkHours());
        cv.put(Contract.RopeTable.CERTIFICATE, rope.getByteImage());
        cv.put(Contract.RopeTable.SYNC, false);
        cv.put(Contract.RopeTable.HAS_RETIRE, rope.isHasRetire());
        cv.put(Contract.RopeTable.RETIRE_DATE, rope.getRetireDate());
        cv.put(Contract.RopeTable.TAG, rope.getTag());
        db.insert(Contract.RopeTable.TABLE_NAME, null, cv);
    }
    public static boolean addRopeDBWithID(SQLiteDatabase db, Rope rope) {
        ContentValues cv = new ContentValues();
        cv.put(Contract.RopeTable._ID, rope.getId());
        cv.put(Contract.RopeTable.TYPE, rope.getType_id());
        cv.put(Contract.RopeTable.POSITION, rope.getPosition_id());
        cv.put(Contract.RopeTable.CERTIFICATE_DATE, rope.getDate());
        cv.put(Contract.RopeTable.SERIAL_NUMBER, rope.getSerialNr());
        cv.put(Contract.RopeTable.WORK_HOURS, rope.getWorkHours());
        cv.put(Contract.RopeTable.CERTIFICATE, rope.getByteImage());
        cv.put(Contract.RopeTable.SYNC, false);
        cv.put(Contract.RopeTable.HAS_RETIRE, rope.isHasRetire());
        cv.put(Contract.RopeTable.RETIRE_DATE, rope.getRetireDate());
        cv.put(Contract.RopeTable.TAG, rope.getTag());
        try {
            db.insert(Contract.RopeTable.TABLE_NAME, null, cv);
            return true;
        } catch (Exception ex){
            return false;
        }
    }
    public static void updateRopeDB(SQLiteDatabase db, Rope rope) {
        ContentValues cv = new ContentValues();
        cv.put(Contract.RopeTable.TYPE, rope.getType_id());
        cv.put(Contract.RopeTable.POSITION, rope.getPosition_id());
        cv.put(Contract.RopeTable.CERTIFICATE_DATE, rope.getDate());
        cv.put(Contract.RopeTable.SERIAL_NUMBER, rope.getSerialNr());
        cv.put(Contract.RopeTable.WORK_HOURS, rope.getWorkHours());
        cv.put(Contract.RopeTable.CERTIFICATE, rope.getByteImage());
        cv.put(Contract.RopeTable.SYNC, false);
        cv.put(Contract.RopeTable.HAS_RETIRE, rope.isHasRetire());
        cv.put(Contract.RopeTable.RETIRE_DATE, rope.getRetireDate());
        cv.put(Contract.RopeTable.TAG, rope.getTag());
        db.update(Contract.RopeTable.TABLE_NAME, cv, "_ID = ?", new String[] {String.valueOf(rope.getId())});
    }
    public static void updateRopeMakeSyncDB(SQLiteDatabase db, Rope rope) {
        ContentValues cv = new ContentValues();
        cv.put(Contract.RopeTable.TYPE, rope.getType_id());
        cv.put(Contract.RopeTable.POSITION, rope.getPosition_id());
        cv.put(Contract.RopeTable.CERTIFICATE_DATE, rope.getDate());
        cv.put(Contract.RopeTable.SERIAL_NUMBER, rope.getSerialNr());
        cv.put(Contract.RopeTable.WORK_HOURS, rope.getWorkHours());
        cv.put(Contract.RopeTable.CERTIFICATE, rope.getByteImage());
        cv.put(Contract.RopeTable.SYNC, true);
        cv.put(Contract.RopeTable.HAS_RETIRE, rope.isHasRetire());
        cv.put(Contract.RopeTable.RETIRE_DATE, rope.getRetireDate());
        cv.put(Contract.RopeTable.TAG, rope.getTag());
        db.update(Contract.RopeTable.TABLE_NAME, cv, "_ID = ?", new String[] {String.valueOf(rope.getId())});
    }
    public static void updateRopeWorkHoursDB(SQLiteDatabase db, int rope_id, int work_hours) {
        ContentValues cv = new ContentValues();
        cv.put(Contract.RopeTable.WORK_HOURS, work_hours);
        cv.put(Contract.RopeTable.SYNC, false);
        db.update(Contract.RopeTable.TABLE_NAME, cv, "_ID = ?", new String[] {String.valueOf(rope_id)});
    }
    @SuppressLint("Range")
    public static List<Rope> getAllRopesDB(SQLiteDatabase db) {
        List<Rope> ropeList = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM " + Contract.RopeTable.TABLE_NAME, null);

        if(c.moveToFirst()) {
            do {
                Rope rope = new Rope();
                rope.setId(c.getInt(c.getColumnIndex(Contract.RopeTable._ID)));
                rope.setSerialNr(c.getString(c.getColumnIndex(Contract.RopeTable.SERIAL_NUMBER)));
                rope.setDate(c.getString(c.getColumnIndex(Contract.RopeTable.CERTIFICATE_DATE)));
                rope.setWorkHours(c.getInt(c.getColumnIndex(Contract.RopeTable.WORK_HOURS)));
                rope.setPosition_id(c.getInt(c.getColumnIndex(Contract.RopeTable.POSITION)));
                rope.setType_id(c.getInt(c.getColumnIndex(Contract.RopeTable.TYPE)));
                rope.setByteImage(c.getBlob(c.getColumnIndex(Contract.RopeTable.CERTIFICATE)));
                rope.setSync(c.getInt(c.getColumnIndex(Contract.RopeTable.SYNC)) > 0 ? true : false);
                rope.setHasRetire(c.getInt(c.getColumnIndex(Contract.RopeTable.HAS_RETIRE)) > 0 ? true : false);
                rope.setRetireDate(c.getString(c.getColumnIndex(Contract.RopeTable.RETIRE_DATE)));
                rope.setTag(c.getString(c.getColumnIndex(Contract.RopeTable.TAG)));
                ropeList.add(rope);
            } while(c.moveToNext());
        }
        c.close();
        return  ropeList;
    }
    public static void deleteRopeDB(SQLiteDatabase db, Rope rope) {
        db.delete(Contract.RopeTable.TABLE_NAME, "_ID=?", new String[] {String.valueOf(rope.getId())});
    }
    @SuppressLint("Range")
    public static List<Rope> getAllRopesAndTailsDB(SQLiteDatabase db) {
        List<Rope> ropeList = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM " + Contract.RopeTable.TABLE_NAME, null);

        if(c.moveToFirst()) {
            do {
                Rope rope = new Rope();
                rope.setId(c.getInt(c.getColumnIndex(Contract.RopeTable._ID)));
                rope.setSerialNr(c.getString(c.getColumnIndex(Contract.RopeTable.SERIAL_NUMBER)));
                rope.setDate(c.getString(c.getColumnIndex(Contract.RopeTable.CERTIFICATE_DATE)));
                rope.setWorkHours(c.getInt(c.getColumnIndex(Contract.RopeTable.WORK_HOURS)));
                rope.setPosition_id(c.getInt(c.getColumnIndex(Contract.RopeTable.POSITION)));
                rope.setType_id(c.getInt(c.getColumnIndex(Contract.RopeTable.TYPE)));
                rope.setByteImage(c.getBlob(c.getColumnIndex(Contract.RopeTable.CERTIFICATE)));
                rope.setSync(c.getInt(c.getColumnIndex(Contract.RopeTable.SYNC)) > 0 ? true : false);
                rope.setHasRetire(c.getInt(c.getColumnIndex(Contract.RopeTable.HAS_RETIRE)) > 0 ? true : false);
                rope.setRetireDate(c.getString(c.getColumnIndex(Contract.RopeTable.RETIRE_DATE)));
                rope.setTag(c.getString(c.getColumnIndex(Contract.RopeTable.TAG)));
                ropeList.add(rope);
            } while(c.moveToNext());
        }
        c.close();
        return  ropeList;
    }
    @SuppressLint("Range")
    public static List<Rope> getAllTailsDB(SQLiteDatabase db) {
        List<Rope> ropeList = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM " + Contract.RopeTable.TABLE_NAME + " r INNER JOIN " + Contract.RopeTypeTable.TABLE_NAME + " rt ON rt." + Contract.RopeTypeTable._ID +
                " = r." + Contract.RopeTable.TYPE + " WHERE rt." + Contract.RopeTypeTable.TYPE + " = \"Tail\"", null);

        if(c.moveToFirst()) {
            do {
                Rope rope = new Rope();
                rope.setId(c.getInt(c.getColumnIndex(Contract.RopeTable._ID)));
                rope.setSerialNr(c.getString(c.getColumnIndex(Contract.RopeTable.SERIAL_NUMBER)));
                rope.setDate(c.getString(c.getColumnIndex(Contract.RopeTable.CERTIFICATE_DATE)));
                rope.setWorkHours(c.getInt(c.getColumnIndex(Contract.RopeTable.WORK_HOURS)));
                rope.setPosition_id(c.getInt(c.getColumnIndex(Contract.RopeTable.POSITION)));
                rope.setType_id(c.getInt(c.getColumnIndex(Contract.RopeTable.TYPE)));
                rope.setByteImage(c.getBlob(c.getColumnIndex(Contract.RopeTable.CERTIFICATE)));
                rope.setSync(c.getInt(c.getColumnIndex(Contract.RopeTable.SYNC)) > 0 ? true : false);
                rope.setHasRetire(c.getInt(c.getColumnIndex(Contract.RopeTable.HAS_RETIRE)) > 0 ? true : false);
                rope.setRetireDate(c.getString(c.getColumnIndex(Contract.RopeTable.RETIRE_DATE)));
                rope.setTag(c.getString(c.getColumnIndex(Contract.RopeTable.TAG)));
                ropeList.add(rope);
            } while(c.moveToNext());
        }
        c.close();
        return  ropeList;
    }
    @SuppressLint("Range")
    public static Rope getRopeByIdDB(SQLiteDatabase db, int id) {
        Rope rp = new Rope();
        Cursor c = db.rawQuery("SELECT * FROM " + Contract.RopeTable.TABLE_NAME + " WHERE " + Contract.RopeTable._ID + "=?", new String[] {String.valueOf(id)});

        if(c.moveToFirst()) {
            do {
                rp.setId(c.getInt(c.getColumnIndex(Contract.RopeTable._ID)));
                rp.setSerialNr(c.getString(c.getColumnIndex(Contract.RopeTable.SERIAL_NUMBER)));
                rp.setDate(c.getString(c.getColumnIndex(Contract.RopeTable.CERTIFICATE_DATE)));
                rp.setWorkHours(c.getInt(c.getColumnIndex(Contract.RopeTable.WORK_HOURS)));
                rp.setPosition_id(c.getInt(c.getColumnIndex(Contract.RopeTable.POSITION)));
                rp.setType_id(c.getInt(c.getColumnIndex(Contract.RopeTable.TYPE)));
                rp.setByteImage(c.getBlob(c.getColumnIndex(Contract.RopeTable.CERTIFICATE)));
                rp.setSync(c.getInt(c.getColumnIndex(Contract.RopeTable.SYNC)) > 0 ? true : false);
                rp.setHasRetire(c.getInt(c.getColumnIndex(Contract.RopeTable.HAS_RETIRE)) > 0 ? true : false);
                rp.setRetireDate(c.getString(c.getColumnIndex(Contract.RopeTable.RETIRE_DATE)));
                rp.setTag(c.getString(c.getColumnIndex(Contract.RopeTable.TAG)));
            } while(c.moveToNext());
        }
        c.close();
        return  rp;
    }
    @SuppressLint("Range")
    public static Rope getRopeBySerialNumberDB(SQLiteDatabase db, String serialNr) {
        Rope rp = new Rope();
        Cursor c = db.rawQuery("SELECT * FROM " + Contract.RopeTable.TABLE_NAME + " WHERE " + Contract.RopeTable.SERIAL_NUMBER + "=?", new String[] {serialNr});

        if(c.moveToFirst()) {
            do {
                rp.setId(c.getInt(c.getColumnIndex(Contract.RopeTable._ID)));
                rp.setSerialNr(c.getString(c.getColumnIndex(Contract.RopeTable.SERIAL_NUMBER)));
                rp.setDate(c.getString(c.getColumnIndex(Contract.RopeTable.CERTIFICATE_DATE)));
                rp.setWorkHours(c.getInt(c.getColumnIndex(Contract.RopeTable.WORK_HOURS)));
                rp.setPosition_id(new Integer(c.getInt(c.getColumnIndex(Contract.RopeTable.POSITION))));
                rp.setType_id(c.getInt(c.getColumnIndex(Contract.RopeTable.TYPE)));
                rp.setByteImage(c.getBlob(c.getColumnIndex(Contract.RopeTable.CERTIFICATE)));
                rp.setSync(c.getInt(c.getColumnIndex(Contract.RopeTable.SYNC)) > 0 ? true : false);

                rp.setHasRetire(c.getInt(c.getColumnIndex(Contract.RopeTable.HAS_RETIRE)) > 0 ? true : false);
                rp.setRetireDate(c.getString(c.getColumnIndex(Contract.RopeTable.RETIRE_DATE)));
                rp.setTag(c.getString(c.getColumnIndex(Contract.RopeTable.TAG)));
            } while(c.moveToNext());
        }
        c.close();
        return  rp;
    }
    @SuppressLint("Range")
    public static Rope getRopeByTagIdDB(SQLiteDatabase db, String tag) {
        Rope rp = new Rope();
        Cursor c = db.rawQuery("SELECT * FROM " + Contract.RopeTable.TABLE_NAME + " WHERE " + Contract.RopeTable.TAG + "=?", new String[] {tag});

        if(c.moveToFirst()) {
            do {
                rp.setId(c.getInt(c.getColumnIndex(Contract.RopeTable._ID)));
                rp.setSerialNr(c.getString(c.getColumnIndex(Contract.RopeTable.SERIAL_NUMBER)));
                rp.setDate(c.getString(c.getColumnIndex(Contract.RopeTable.CERTIFICATE_DATE)));
                rp.setWorkHours(c.getInt(c.getColumnIndex(Contract.RopeTable.WORK_HOURS)));
                rp.setPosition_id(new Integer(c.getInt(c.getColumnIndex(Contract.RopeTable.POSITION))));
                rp.setType_id(c.getInt(c.getColumnIndex(Contract.RopeTable.TYPE)));
                rp.setByteImage(c.getBlob(c.getColumnIndex(Contract.RopeTable.CERTIFICATE)));
                rp.setSync(c.getInt(c.getColumnIndex(Contract.RopeTable.SYNC)) > 0 ? true : false);

                rp.setHasRetire(c.getInt(c.getColumnIndex(Contract.RopeTable.HAS_RETIRE)) > 0 ? true : false);
                rp.setRetireDate(c.getString(c.getColumnIndex(Contract.RopeTable.RETIRE_DATE)));
                rp.setTag(c.getString(c.getColumnIndex(Contract.RopeTable.TAG)));
            } while(c.moveToNext());
        }
        c.close();
        return  rp;
    }
    @SuppressLint("Range")
    public static Rope getRopeByNameDB(SQLiteDatabase db, Rope rope) {
        Rope rp = new Rope();
        Cursor c = db.rawQuery("SELECT * FROM " + Contract.RopeTable.TABLE_NAME + " WHERE " + Contract.RopeTable.SERIAL_NUMBER + "=?", new String[] {rope.getSerialNr()});

        if(c.moveToFirst()) {
            do {
                rp.setId(c.getInt(c.getColumnIndex(Contract.RopeTable._ID)));
                rp.setSerialNr(c.getString(c.getColumnIndex(Contract.RopeTable.SERIAL_NUMBER)));
                rp.setDate(c.getString(c.getColumnIndex(Contract.RopeTable.CERTIFICATE_DATE)));
                rp.setWorkHours(c.getInt(c.getColumnIndex(Contract.RopeTable.WORK_HOURS)));
                rp.setPosition_id(new Integer(c.getInt(c.getColumnIndex(Contract.RopeTable.POSITION))));
                rp.setType_id(c.getInt(c.getColumnIndex(Contract.RopeTable.TYPE)));
                rp.setByteImage(c.getBlob(c.getColumnIndex(Contract.RopeTable.CERTIFICATE)));
                rp.setSync(c.getInt(c.getColumnIndex(Contract.RopeTable.SYNC)) > 0 ? true : false);
                rp.setHasRetire(c.getInt(c.getColumnIndex(Contract.RopeTable.HAS_RETIRE)) > 0 ? true : false);
                rp.setRetireDate(c.getString(c.getColumnIndex(Contract.RopeTable.RETIRE_DATE)));
                rp.setTag(c.getString(c.getColumnIndex(Contract.RopeTable.TAG)));
            } while(c.moveToNext());
        }
        c.close();
        return  rp;
    }
    @SuppressLint("Range")
    public static Rope getRopeByPositionDB(SQLiteDatabase db, Position position) {
        Rope rope = new Rope();
        Cursor c = db.rawQuery("SELECT * FROM " + Contract.RopeTable.TABLE_NAME + " WHERE " + Contract.RopeTable.POSITION + "=?", new String[] {String.valueOf(position.getId())});

        if(c.moveToFirst()) {
            do {
                rope.setId(c.getInt(c.getColumnIndex(Contract.RopeTable._ID)));
                rope.setSerialNr(c.getString(c.getColumnIndex(Contract.RopeTable.SERIAL_NUMBER)));
                rope.setDate(c.getString(c.getColumnIndex(Contract.RopeTable.CERTIFICATE_DATE)));
                rope.setWorkHours(c.getInt(c.getColumnIndex(Contract.RopeTable.WORK_HOURS)));
                rope.setPosition_id(c.getInt(c.getColumnIndex(Contract.RopeTable.POSITION)));
                rope.setType_id(c.getInt(c.getColumnIndex(Contract.RopeTable.TYPE)));
                rope.setByteImage(c.getBlob(c.getColumnIndex(Contract.RopeTable.CERTIFICATE)));
                rope.setSync(c.getInt(c.getColumnIndex(Contract.RopeTable.SYNC)) > 0 ? true : false);
                rope.setHasRetire(c.getInt(c.getColumnIndex(Contract.RopeTable.HAS_RETIRE)) > 0 ? true : false);
                rope.setRetireDate(c.getString(c.getColumnIndex(Contract.RopeTable.RETIRE_DATE)));
                rope.setTag(c.getString(c.getColumnIndex(Contract.RopeTable.TAG)));
            } while(c.moveToNext());
        }
        c.close();
        return  rope;
    }
    @SuppressLint("Range")
    public static List<Rope> getRopesByPositionForDetailedDB(SQLiteDatabase db, Position position) {
        List<Rope> ropeList = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM " + Contract.RopeTable.TABLE_NAME + " WHERE " + Contract.RopeTable.POSITION +
                " =? AND " + Contract.RopeTable.HAS_RETIRE + " = 0", new String[] {String.valueOf(position.getId())});

        if(c.moveToFirst()) {
            do {
                Rope rope = new Rope();
                rope.setId(c.getInt(c.getColumnIndex(Contract.RopeTable._ID)));
                rope.setSerialNr(c.getString(c.getColumnIndex(Contract.RopeTable.SERIAL_NUMBER)));
                rope.setDate(c.getString(c.getColumnIndex(Contract.RopeTable.CERTIFICATE_DATE)));
                rope.setWorkHours(c.getInt(c.getColumnIndex(Contract.RopeTable.WORK_HOURS)));
                rope.setPosition_id(c.getInt(c.getColumnIndex(Contract.RopeTable.POSITION)));
                rope.setType_id(c.getInt(c.getColumnIndex(Contract.RopeTable.TYPE)));
                rope.setByteImage(c.getBlob(c.getColumnIndex(Contract.RopeTable.CERTIFICATE)));
                rope.setSync(c.getInt(c.getColumnIndex(Contract.RopeTable.SYNC)) > 0 ? true : false);
                rope.setHasRetire(c.getInt(c.getColumnIndex(Contract.RopeTable.HAS_RETIRE)) > 0 ? true : false);
                rope.setRetireDate(c.getString(c.getColumnIndex(Contract.RopeTable.RETIRE_DATE)));
                rope.setTag(c.getString(c.getColumnIndex(Contract.RopeTable.TAG)));
                ropeList.add(rope);
            } while(c.moveToNext());
        }
        c.close();
        return  ropeList;
    }
    @SuppressLint("Range")
    public static List<Rope> getRopesByPositionForRoutineDB(SQLiteDatabase db, Position position) {
        List<Rope> ropeList = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT r.* FROM " + Contract.RopeTable.TABLE_NAME + " r INNER JOIN " + Contract.PositionTable.TABLE_NAME + " p ON p." +
                Contract.PositionTable._ID + " = r." + Contract.RopeTable.POSITION + " WHERE r." + Contract.RopeTable.POSITION + "=? AND p." +
                Contract.PositionTable.STORAGE + " = 0 AND r." + Contract.RopeTable.HAS_RETIRE + " = 0", new String[] {String.valueOf(position.getId())});

        if(c.moveToFirst()) {
            do {
                Rope rope = new Rope();
                rope.setId(c.getInt(c.getColumnIndex(Contract.RopeTable._ID)));
                rope.setSerialNr(c.getString(c.getColumnIndex(Contract.RopeTable.SERIAL_NUMBER)));
                rope.setDate(c.getString(c.getColumnIndex(Contract.RopeTable.CERTIFICATE_DATE)));
                rope.setWorkHours(c.getInt(c.getColumnIndex(Contract.RopeTable.WORK_HOURS)));
                rope.setPosition_id(c.getInt(c.getColumnIndex(Contract.RopeTable.POSITION)));
                rope.setType_id(c.getInt(c.getColumnIndex(Contract.RopeTable.TYPE)));
                rope.setByteImage(c.getBlob(c.getColumnIndex(Contract.RopeTable.CERTIFICATE)));
                rope.setSync(c.getInt(c.getColumnIndex(Contract.RopeTable.SYNC)) > 0 ? true : false);
                rope.setHasRetire(c.getInt(c.getColumnIndex(Contract.RopeTable.HAS_RETIRE)) > 0 ? true : false);
                rope.setRetireDate(c.getString(c.getColumnIndex(Contract.RopeTable.RETIRE_DATE)));
                rope.setTag(c.getString(c.getColumnIndex(Contract.RopeTable.TAG)));
                ropeList.add(rope);
            } while(c.moveToNext());
        }
        c.close();
        return  ropeList;
    }
    @SuppressLint("Range")
    public static String getTypeofRopeDB(SQLiteDatabase db, int type_id) {
        String type = null;
        Cursor c = db.rawQuery("SELECT rt." + Contract.RopeTypeTable.TYPE + " FROM " + Contract.RopeTable.TABLE_NAME +
                " r INNER JOIN " + Contract.RopeTypeTable.TABLE_NAME + " rt ON rt." + Contract.RopeTypeTable._ID + " = " +
                "r." + Contract.RopeTable.TYPE + " WHERE rt." + Contract.RopeTypeTable._ID + "=?", new String[] {String.valueOf(type_id)});

        if(c.moveToFirst()) {
            do {
                type = c.getString(c.getColumnIndex(Contract.RopeTypeTable.TYPE));
            } while(c.moveToNext());
        }
        c.close();
        return  type;
    }
    @SuppressLint("Range")
    public static List<Rope> getRopesByRopeTypeDB(SQLiteDatabase db, RopeType type) {
        List<Rope> ropeList = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM " + Contract.RopeTable.TABLE_NAME + " WHERE " + Contract.RopeTable.TYPE + "=?", new String[] {String.valueOf(type.getId())});

        if(c.moveToFirst()) {
            do {
                Rope rope = new Rope();
                rope.setId(c.getInt(c.getColumnIndex(Contract.RopeTable._ID)));
                rope.setSerialNr(c.getString(c.getColumnIndex(Contract.RopeTable.SERIAL_NUMBER)));
                rope.setDate(c.getString(c.getColumnIndex(Contract.RopeTable.CERTIFICATE_DATE)));
                rope.setWorkHours(c.getInt(c.getColumnIndex(Contract.RopeTable.WORK_HOURS)));
                rope.setPosition_id(c.getInt(c.getColumnIndex(Contract.RopeTable.POSITION)));
                rope.setType_id(c.getInt(c.getColumnIndex(Contract.RopeTable.TYPE)));
                rope.setByteImage(c.getBlob(c.getColumnIndex(Contract.RopeTable.CERTIFICATE)));
                rope.setSync(c.getInt(c.getColumnIndex(Contract.RopeTable.SYNC)) > 0 ? true : false);
                rope.setHasRetire(c.getInt(c.getColumnIndex(Contract.RopeTable.HAS_RETIRE)) > 0 ? true : false);
                rope.setRetireDate(c.getString(c.getColumnIndex(Contract.RopeTable.RETIRE_DATE)));
                rope.setTag(c.getString(c.getColumnIndex(Contract.RopeTable.TAG)));
                ropeList.add(rope);
            } while(c.moveToNext());
        }
        c.close();
        return  ropeList;
    }
    @SuppressLint("Range")
    public static List<Rope> getAllRopesForRoutineDB(SQLiteDatabase db, List<Position> positions) {
        List<Integer> ids = new ArrayList<>();
        for(int i=0; i<positions.size(); i++) {
            ids.add(positions.get(i).getId());
        }
        List<Rope> ropeList = new ArrayList<>();
        String query = "SELECT r.* FROM " + Contract.RopeTable.TABLE_NAME + " r INNER JOIN " + Contract.PositionTable.TABLE_NAME +
                " p ON p." + Contract.PositionTable._ID + " = r." + Contract.RopeTable.POSITION + "  WHERE " + Contract.PositionTable.STORAGE +
                " = 0 " + "AND r." + Contract.RopeTable.POSITION + " IN (" + makePlaceholders(ids.size()) + ")";
        Cursor c = db.rawQuery(query, null);

        if(c.moveToFirst()) {
            do {
                Rope rope = new Rope();
                rope.setId(c.getInt(c.getColumnIndex(Contract.RopeTable._ID)));
                rope.setSerialNr(c.getString(c.getColumnIndex(Contract.RopeTable.SERIAL_NUMBER)));
                rope.setDate(c.getString(c.getColumnIndex(Contract.RopeTable.CERTIFICATE_DATE)));
                rope.setWorkHours(c.getInt(c.getColumnIndex(Contract.RopeTable.WORK_HOURS)));
                rope.setPosition_id(c.getInt(c.getColumnIndex(Contract.RopeTable.POSITION)));
                rope.setType_id(c.getInt(c.getColumnIndex(Contract.RopeTable.TYPE)));
                rope.setByteImage(c.getBlob(c.getColumnIndex(Contract.RopeTable.CERTIFICATE)));
                rope.setSync(c.getInt(c.getColumnIndex(Contract.RopeTable.SYNC)) > 0 ? true : false);
                rope.setHasRetire(c.getInt(c.getColumnIndex(Contract.RopeTable.HAS_RETIRE)) > 0 ? true : false);
                rope.setRetireDate(c.getString(c.getColumnIndex(Contract.RopeTable.RETIRE_DATE)));
                rope.setTag(c.getString(c.getColumnIndex(Contract.RopeTable.TAG)));
                ropeList.add(rope);
            } while(c.moveToNext());
        }
        c.close();
        return  ropeList;
    }
    @SuppressLint("Range")
    public static List<Rope> getAllRopesInUseDB(SQLiteDatabase db) {
        List<Rope> ropeList = new ArrayList<>();
        String query = "SELECT p.* FROM " + Contract.RopeTable.TABLE_NAME + " WHERE " + Contract.RopeTable.HAS_RETIRE + " = 0";
        Cursor c = db.rawQuery(query, null);

        if(c.moveToFirst()) {
            do {
                Rope rope = new Rope();
                rope.setId(c.getInt(c.getColumnIndex(Contract.RopeTable._ID)));
                rope.setSerialNr(c.getString(c.getColumnIndex(Contract.RopeTable.SERIAL_NUMBER)));
                rope.setDate(c.getString(c.getColumnIndex(Contract.RopeTable.CERTIFICATE_DATE)));
                rope.setWorkHours(c.getInt(c.getColumnIndex(Contract.RopeTable.WORK_HOURS)));
                rope.setPosition_id(c.getInt(c.getColumnIndex(Contract.RopeTable.POSITION)));
                rope.setType_id(c.getInt(c.getColumnIndex(Contract.RopeTable.TYPE)));
                rope.setByteImage(c.getBlob(c.getColumnIndex(Contract.RopeTable.CERTIFICATE)));
                rope.setSync(c.getInt(c.getColumnIndex(Contract.RopeTable.SYNC)) > 0 ? true : false);
                rope.setHasRetire(c.getInt(c.getColumnIndex(Contract.RopeTable.HAS_RETIRE)) > 0 ? true : false);
                rope.setRetireDate(c.getString(c.getColumnIndex(Contract.RopeTable.RETIRE_DATE)));
                rope.setTag(c.getString(c.getColumnIndex(Contract.RopeTable.TAG)));
                ropeList.add(rope);
            } while(c.moveToNext());
        }
        c.close();
        return  ropeList;
    }
    @SuppressLint("Range")
    public static List<Rope> getAllStoredRopesDB(SQLiteDatabase db) {
        List<Rope> ropeList = new ArrayList<>();

        String query = "SELECT * FROM " + Contract.RopeTable.TABLE_NAME + " r INNER JOIN " + Contract.PositionTable.TABLE_NAME +
                " p ON p." + Contract.PositionTable._ID + " = r." + Contract.RopeTable.POSITION + "  WHERE " + Contract.PositionTable.STORAGE +
                " = 1 ";

        Cursor c = db.rawQuery(query, null);

        if(c.moveToFirst()) {
            do {
                Rope rope = new Rope();
                rope.setId(c.getInt(c.getColumnIndex(Contract.RopeTable._ID)));
                rope.setSerialNr(c.getString(c.getColumnIndex(Contract.RopeTable.SERIAL_NUMBER)));
                rope.setDate(c.getString(c.getColumnIndex(Contract.RopeTable.CERTIFICATE_DATE)));
                rope.setWorkHours(c.getInt(c.getColumnIndex(Contract.RopeTable.WORK_HOURS)));
                rope.setPosition_id(c.getInt(c.getColumnIndex(Contract.RopeTable.POSITION)));
                rope.setType_id(c.getInt(c.getColumnIndex(Contract.RopeTable.TYPE)));
                rope.setByteImage(c.getBlob(c.getColumnIndex(Contract.RopeTable.CERTIFICATE)));
                rope.setSync(c.getInt(c.getColumnIndex(Contract.RopeTable.SYNC)) > 0 ? true : false);
                rope.setHasRetire(c.getInt(c.getColumnIndex(Contract.RopeTable.HAS_RETIRE)) > 0 ? true : false);
                rope.setRetireDate(c.getString(c.getColumnIndex(Contract.RopeTable.RETIRE_DATE)));
                rope.setTag(c.getString(c.getColumnIndex(Contract.RopeTable.TAG)));
                ropeList.add(rope);
            } while(c.moveToNext());
        }
        c.close();
        return  ropeList;
    }
    @SuppressLint("Range")
    public static List<Rope> getAllNonStoredRopesDB(SQLiteDatabase db) {
        List<Rope> ropeList = new ArrayList<>();

        String query = "SELECT * FROM " + Contract.RopeTable.TABLE_NAME + " r INNER JOIN " + Contract.PositionTable.TABLE_NAME +
                " p ON p." + Contract.PositionTable._ID + " = r." + Contract.RopeTable.POSITION + "  WHERE " + Contract.PositionTable.STORAGE +
                " = 0 ";

        Cursor c = db.rawQuery(query, null);

        if(c.moveToFirst()) {
            do {
                Rope rope = new Rope();
                rope.setId(c.getInt(c.getColumnIndex(Contract.RopeTable._ID)));
                rope.setSerialNr(c.getString(c.getColumnIndex(Contract.RopeTable.SERIAL_NUMBER)));
                rope.setDate(c.getString(c.getColumnIndex(Contract.RopeTable.CERTIFICATE_DATE)));
                rope.setWorkHours(c.getInt(c.getColumnIndex(Contract.RopeTable.WORK_HOURS)));
                rope.setPosition_id(c.getInt(c.getColumnIndex(Contract.RopeTable.POSITION)));
                rope.setType_id(c.getInt(c.getColumnIndex(Contract.RopeTable.TYPE)));
                rope.setByteImage(c.getBlob(c.getColumnIndex(Contract.RopeTable.CERTIFICATE)));
                rope.setSync(c.getInt(c.getColumnIndex(Contract.RopeTable.SYNC)) > 0 ? true : false);
                rope.setHasRetire(c.getInt(c.getColumnIndex(Contract.RopeTable.HAS_RETIRE)) > 0 ? true : false);
                rope.setRetireDate(c.getString(c.getColumnIndex(Contract.RopeTable.RETIRE_DATE)));
                rope.setTag(c.getString(c.getColumnIndex(Contract.RopeTable.TAG)));
                ropeList.add(rope);
            } while(c.moveToNext());
        }
        c.close();
        return  ropeList;
    }
    @SuppressLint("Range")
    public static Rope findTailByPositionDB(SQLiteDatabase db, String positionName) {
        Rope rope = new Rope();
        String query = "SELECT * FROM " + Contract.RopeTable.TABLE_NAME + " r INNER JOIN " + Contract.PositionTable.TABLE_NAME +
                " p ON p." + Contract.PositionTable._ID + " = r." + Contract.RopeTable.POSITION + " INNER JOIN " + Contract.RopeTypeTable.TABLE_NAME +
                " rt ON rt." + Contract.RopeTypeTable._ID + " = r." + Contract.RopeTable.TYPE + " WHERE p." + Contract.PositionTable.NAME +
                " = ? AND rt." + Contract.RopeTypeTable.TYPE + " = \"Tail\"";

        Cursor c = db.rawQuery(query, new String[] {positionName});

        if(c.moveToFirst()) {
            do {
                rope.setId(c.getInt(c.getColumnIndex(Contract.RopeTable._ID)));
                rope.setSerialNr(c.getString(c.getColumnIndex(Contract.RopeTable.SERIAL_NUMBER)));
                rope.setDate(c.getString(c.getColumnIndex(Contract.RopeTable.CERTIFICATE_DATE)));
                rope.setWorkHours(c.getInt(c.getColumnIndex(Contract.RopeTable.WORK_HOURS)));
                rope.setPosition_id(c.getInt(c.getColumnIndex(Contract.RopeTable.POSITION)));
                rope.setType_id(c.getInt(c.getColumnIndex(Contract.RopeTable.TYPE)));
                rope.setByteImage(c.getBlob(c.getColumnIndex(Contract.RopeTable.CERTIFICATE)));
                rope.setSync(c.getInt(c.getColumnIndex(Contract.RopeTable.SYNC)) > 0 ? true : false);
                rope.setHasRetire(c.getInt(c.getColumnIndex(Contract.RopeTable.HAS_RETIRE)) > 0 ? true : false);
                rope.setRetireDate(c.getString(c.getColumnIndex(Contract.RopeTable.RETIRE_DATE)));
                rope.setTag(c.getString(c.getColumnIndex(Contract.RopeTable.TAG)));
            } while(c.moveToNext());
        }
        c.close();
        return  rope;
    }
    public static void updateSyncRopeDB(SQLiteDatabase db, Rope rope) {
        ContentValues cv = new ContentValues();
        cv.put(Contract.RopeTable.TYPE, rope.getType_id());
        cv.put(Contract.RopeTable.POSITION, rope.getPosition_id());
        cv.put(Contract.RopeTable.CERTIFICATE_DATE, rope.getDate());
        cv.put(Contract.RopeTable.SERIAL_NUMBER, rope.getSerialNr());
        cv.put(Contract.RopeTable.WORK_HOURS, rope.getWorkHours());
        cv.put(Contract.RopeTable.CERTIFICATE, rope.getByteImage());
        cv.put(Contract.RopeTable.SYNC, true);
        cv.put(Contract.RopeTable.HAS_RETIRE, rope.isHasRetire());
        cv.put(Contract.RopeTable.RETIRE_DATE, rope.getRetireDate());
        cv.put(Contract.RopeTable.TAG, rope.getTag());
        db.update(Contract.RopeTable.TABLE_NAME, cv, "_ID = ?", new String[] {String.valueOf(rope.getId())});
    }
    @SuppressLint("Range")
    public static List<Rope> getAllNonSyncRopesDB(SQLiteDatabase db) {
        List<Rope> ropeList = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM " + Contract.RopeTable.TABLE_NAME + " WHERE " +
                Contract.RopeTable.SYNC + " = 0",null);

        if(c.moveToFirst()) {
            do {
                Rope rope = new Rope();
                rope.setId(c.getInt(c.getColumnIndex(Contract.RopeTable._ID)));
                rope.setSerialNr(c.getString(c.getColumnIndex(Contract.RopeTable.SERIAL_NUMBER)));
                rope.setDate(c.getString(c.getColumnIndex(Contract.RopeTable.CERTIFICATE_DATE)));
                rope.setWorkHours(c.getInt(c.getColumnIndex(Contract.RopeTable.WORK_HOURS)));
                rope.setPosition_id(c.getInt(c.getColumnIndex(Contract.RopeTable.POSITION)));
                rope.setType_id(c.getInt(c.getColumnIndex(Contract.RopeTable.TYPE)));
                rope.setByteImage(c.getBlob(c.getColumnIndex(Contract.RopeTable.CERTIFICATE)));
                rope.setSync(c.getInt(c.getColumnIndex(Contract.RopeTable.SYNC)) > 0 ? true : false);
                rope.setHasRetire(c.getInt(c.getColumnIndex(Contract.RopeTable.HAS_RETIRE)) > 0 ? true : false);
                rope.setRetireDate(c.getString(c.getColumnIndex(Contract.RopeTable.RETIRE_DATE)));
                rope.setTag(c.getString(c.getColumnIndex(Contract.RopeTable.TAG)));
                ropeList.add(rope);
            } while(c.moveToNext());
        }
        c.close();
        return  ropeList;
    }
    static String makePlaceholders(int len) {
        if (len < 1) {
            // It will lead to an invalid query anyway ..
            throw new RuntimeException("No placeholders");
        } else {
            StringBuilder sb = new StringBuilder(len * 2 - 1);
            sb.append("?");
            for (int i = 1; i < len; i++) {
                sb.append(",?");
            }
            return sb.toString();
        }
    }
}
