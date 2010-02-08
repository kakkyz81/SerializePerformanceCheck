package net.krks.android.spc;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class SPCOpenHelper extends SQLiteOpenHelper {
    static final int DB_VERSION = 1;

    static final String CREATE_TABLE_SCHOOL =
        "create table spc_school ( _id integer primary key autoincrement,"
        + "name string);";

    static final String CREATE_TABLE_STUDENT =
        "create table spc_student ( _id integer primary key autoincrement,"
        + "name text, id integer, school integer, text string);";

    static final String DROP_ALL = "drop table spc_school; drop table spc_student;";

    public SPCOpenHelper(Context context) {
//        super(context, "spc", null, DB_VERSION);
        super(context, "spc", null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SCHOOL);
        db.execSQL(CREATE_TABLE_STUDENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        drop(db);
        db.execSQL(CREATE_TABLE_SCHOOL);
        db.execSQL(CREATE_TABLE_STUDENT);
    }


    public void drop(SQLiteDatabase db) {
        try {
            db.execSQL(DROP_ALL);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
