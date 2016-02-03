package com.janedoe.mywalkingapp.Handler;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by janedoe on 1/26/2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "walkingapp.db";
    public static final int DATABASE_VERSION = 1;

    private static DatabaseHandler dbHandler;

    public static DatabaseHandler getInstance(Context context){
        if(dbHandler == null)
            dbHandler = new DatabaseHandler(context);
        return dbHandler;
    }

    private DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
