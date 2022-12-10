package com.safeline.safelinecranes.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.safeline.safelinecranes.models.User;

import java.util.ArrayList;
import java.util.List;

public final class UserDBApi {
    public static void addUserDB(SQLiteDatabase db, User user) {
        ContentValues cv = new ContentValues();
        cv.put(Contract.UserTable.USERNAME, user.getUsername());
        cv.put(Contract.UserTable.PASSWORD, user.getPassword());
        cv.put(Contract.UserTable.LICENSE, user.getLicense());
        cv.put(Contract.UserTable.DEVICE, user.getDevice());
        cv.put(Contract.UserTable.VESSEL, user.getVessel());
        db.insert(Contract.UserTable.TABLE_NAME, null, cv);
    }
    public static void updateUserDB(SQLiteDatabase db, User user) {
        ContentValues cv = new ContentValues();
        cv.put(Contract.UserTable.USERNAME, user.getUsername());
        cv.put(Contract.UserTable.LICENSE, user.getLicense());
        cv.put(Contract.UserTable.PASSWORD, user.getPassword());
        cv.put(Contract.UserTable.DEVICE, user.getDevice());
        cv.put(Contract.UserTable.VESSEL, user.getVessel());
        db.update(Contract.UserTable.TABLE_NAME, cv, "_ID = ?", new String[] {String.valueOf(user.getId())});
    }
    @SuppressLint("Range")
    public static User verifyUserDB(SQLiteDatabase db, String username, String deviceInfo) {
        User user = new User();
        Cursor c = db.rawQuery("SELECT * FROM " + Contract.UserTable.TABLE_NAME + " WHERE " +
                Contract.UserTable.USERNAME + " = " + username + " AND " + Contract.UserTable.DEVICE +
                " = " + deviceInfo, null);
        if(c.moveToFirst()) {
            do {
                user.setId(c.getInt(c.getColumnIndex(Contract.UserTable._ID)));
                user.setUsername(c.getString(c.getColumnIndex(Contract.UserTable.USERNAME)));
                user.setLicense(c.getString(c.getColumnIndex(Contract.UserTable.LICENSE)));
                user.setDevice(c.getString(c.getColumnIndex(Contract.UserTable.DEVICE)));
                user.setVessel(c.getString(c.getColumnIndex(Contract.UserTable.VESSEL)));
                user.setPassword(c.getString(c.getColumnIndex(Contract.UserTable.PASSWORD)));
            } while(c.moveToNext());
        }
        c.close();
        return  user;
    }
    @SuppressLint("Range")
    public static User getUserDB(SQLiteDatabase db) {
        User user = new User();
        Cursor c = db.rawQuery("SELECT * FROM " + Contract.UserTable.TABLE_NAME + " LIMIT 1", null);
        if(c.moveToFirst()) {
            do {
                user.setId(c.getInt(c.getColumnIndex(Contract.UserTable._ID)));
                user.setUsername(c.getString(c.getColumnIndex(Contract.UserTable.USERNAME)));
                user.setLicense(c.getString(c.getColumnIndex(Contract.UserTable.LICENSE)));
                user.setDevice(c.getString(c.getColumnIndex(Contract.UserTable.DEVICE)));
                user.setVessel(c.getString(c.getColumnIndex(Contract.UserTable.VESSEL)));
                user.setPassword(c.getString(c.getColumnIndex(Contract.UserTable.PASSWORD)));
            } while(c.moveToNext());
        }
        c.close();
        return  user;
    }
    public static void deleteUserDB(SQLiteDatabase db, String username) {
        db.delete(Contract.UserTable.TABLE_NAME, Contract.UserTable.USERNAME+ "=?", new String[] {username});
    }
    public static void deleteUsersDB(SQLiteDatabase db){
        List<User> users = getAllUsersDB(db);
        for(int i=0; i<users.size(); i++){
            deleteUserDB(db, users.get(i).getUsername());
        }
    }
    @SuppressLint("Range")
    public static List<User> getAllUsersDB(SQLiteDatabase db){
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM " + Contract.UserTable.TABLE_NAME;
        Cursor c = db.rawQuery(query, null);

        if(c.moveToFirst()) {
            do {
                User user = new User();
                user.setId(c.getInt(c.getColumnIndex(Contract.UserTable._ID)));
                user.setUsername(c.getString(c.getColumnIndex(Contract.UserTable.USERNAME)));
                user.setVessel(c.getString(c.getColumnIndex(Contract.UserTable.VESSEL)));
                user.setPassword(c.getString(c.getColumnIndex(Contract.UserTable.PASSWORD)));
                user.setDevice(c.getString(c.getColumnIndex(Contract.UserTable.DEVICE)));
                user.setLicense(c.getString(c.getColumnIndex(Contract.UserTable.LICENSE)));
                users.add(user);
            } while(c.moveToNext());
        }
        c.close();
        return  users;
    }
}

