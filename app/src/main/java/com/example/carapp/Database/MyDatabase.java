package com.example.carapp.Database;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class MyDatabase extends SQLiteAssetHelper {

    public static final String DATABASE_NAME = "car.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "cars";
    public static final String COL_ID = "id";
    public static final String COL_IMAGE = "image";
    public static final String COL_MODEL = "model";
    public static final String COL_COLOR = "color";
    public static final String COL_DESCRIPTION = "description";
    public static final String COL_DISTANCE_PER_LETTER = "distancePerLetter";


    public MyDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
