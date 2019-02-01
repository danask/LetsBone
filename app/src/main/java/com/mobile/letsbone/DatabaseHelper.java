package com.mobile.letsbone;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper
{
    final static String DB_NAME = "LetsBone.db";
    final static int DB_VER = 1;
    final static String CHAT_LOG_TBL = "chatLogTable";

    // common
    final static String ID = "id";
    final static String PARTNER_A = "A_Id";
    final static String PARTNER_B = "B_Id";
    final static String PARTNER_A_MSG = "A_Message";
    final static String PARTNER_B_MSG = "B_Message";
    final static String ROOM = "room";
    final static String DATE = "date";
    final static String MONTH = "month";

    public DatabaseHelper(Context ctx){
        super(ctx, DB_NAME, null, DB_VER);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String queryUser = "CREATE TABLE " + CHAT_LOG_TBL + "("
                + ID + " INTEGER PRIMARY KEY,"
                + PARTNER_A + " TEXT,"
                + PARTNER_B + " TEXT,"
                + PARTNER_A_MSG + " TEXT,"
                + PARTNER_B_MSG + " TEXT,"
                + ROOM + " TEXT,"
                + DATE + " TEXT,"
                + MONTH + " TEXT)";

        try {
            db.execSQL(queryUser);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer)
    {
        db.execSQL("DROP table if exists " + CHAT_LOG_TBL);

        onCreate(db);
    }


    public boolean addChatLog(String parterAId, String parterBId, String parterAMessage, String parterBMessage,
                              String room, String date, String month)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(PARTNER_A, parterAId);
        contentValues.put(PARTNER_B, parterBId);
        contentValues.put(PARTNER_A_MSG, parterAMessage);
        contentValues.put(PARTNER_B_MSG, parterBMessage);
        contentValues.put(ROOM, room);
        contentValues.put(DATE, date);
        contentValues.put(MONTH, month);

        long r = db.insert(CHAT_LOG_TBL, null, contentValues);

        if(r == -1)
            return false;
        else
            return true;
    }

}
