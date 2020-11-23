package com.example.imapet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class CalculatorDatabase extends SQLiteOpenHelper {
    public static final String NAME_OF_DATABASE = "PetYears.db";
    public static final String NAME_OF_TABLE = "pet_table";

    public static final String COL_1 = "HumanYears";
    public static final String COL_2 = "Weight";
    public static final String COL_3 = "Pet";
    public static final String COL_4 = "PetYear";

    public CalculatorDatabase(Context context) {
        super(context, NAME_OF_DATABASE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + NAME_OF_TABLE + "(HumanYears INTEGER, Weight INTEGER, Pet TEXT, PetYear INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + NAME_OF_TABLE);
        onCreate(sqLiteDatabase);
    }

    public boolean insertCalculationData (int humanYears, int weight, String pet, int petYear) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_1, humanYears);
        contentValues.put(COL_2, weight);
        contentValues.put(COL_3, pet);
        contentValues.put(COL_4, petYear);

        long result = sqLiteDatabase.insert(NAME_OF_TABLE, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getAllData() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor retrieval = sqLiteDatabase.rawQuery("SELECT * FROM " + NAME_OF_TABLE, null);
        return retrieval;
    }

    public Cursor deleteAllData() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor deletion = sqLiteDatabase.rawQuery("DELETE FROM " + NAME_OF_TABLE, null);
        return deletion;
    }
}
