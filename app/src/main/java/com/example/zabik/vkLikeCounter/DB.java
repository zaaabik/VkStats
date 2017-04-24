package com.example.zabik.vkLikeCounter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by zabik on 19.03.17.
 */

public class DB extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "VkUser";
    public static final String TABLE_NAME = "VKlist";
    public static final String KEY_COUNT_LIKE = "likes";
    public static final String KEY_ID = "_id";
    public static final String KEY_USER_ID = "userId";
    public DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(" + KEY_ID + " integer primary key," + KEY_COUNT_LIKE + " integer, " + KEY_USER_ID + " integer" +")");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists" + TABLE_NAME);
        onCreate(db);
    }

}
