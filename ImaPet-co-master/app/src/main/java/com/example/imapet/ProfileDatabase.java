package com.example.imapet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ProfileDatabase extends SQLiteOpenHelper {
    public static final String NAME_OF_DATABASE = "Profile.db";
    public static final String NAME_OF_TABLE = "profile_table";

    public static final String COL_1 = "Username";
    public static final String COL_2 = "ProfileName";
    public static final String COL_3 = "Status";
    public static final String COL_4 = "ALittleAboutMe";

    public ProfileDatabase(Context context) {
        super(context, NAME_OF_DATABASE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + NAME_OF_TABLE + "(Username TEXT PRIMARY KEY, ProfileName TEXT, Status TEXT, ALittleAboutMe TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + NAME_OF_TABLE);
        onCreate(sqLiteDatabase);
    }

    public boolean insertProfileData (String username, String profileName, String status, String aboutMe) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_1, username);
        contentValues.put(COL_2, profileName);
        contentValues.put(COL_3, status);
        contentValues.put(COL_4, aboutMe);

        long result = sqLiteDatabase.insert(NAME_OF_TABLE, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getAllProfiles() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor retrieval = sqLiteDatabase.rawQuery("SELECT * FROM " + NAME_OF_TABLE, null);
        return retrieval;
    }

    public boolean updateProfile (String username, String profileName, String status, String aboutMe) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_1, username);
        contentValues.put(COL_2, profileName);
        contentValues.put(COL_3, status);
        contentValues.put(COL_4, aboutMe);

        sqLiteDatabase.update(NAME_OF_TABLE, contentValues, "Username = ?", new String[] {username});
        return true;
    }

    public Integer deleteProfileData (String username) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.delete(NAME_OF_TABLE, "Username = ?", new String[] {username});
    }
}
