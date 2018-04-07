package com.example.tabish.taskask;

import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Debug;
import android.util.Log;

public class SQLiteDatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "TaskaskOffline";
    private static final String TABLE_NAME = "Tasks";
    private static final String KEY_DESC = "description";
    private static final String KEY_FEE = "fee";
    private static final String KEY_TAG = "tag";
    private static final String KEY_CLEVEL = "clevel";
    private static final String KEY_URGENCY = "ulevel";
    private static final String KEY_POSTEDBY = "pby";
    private static final String KEY_TIMELIMIT = "timelimit";


    private static final String[] COLUMNS = { KEY_DESC, KEY_FEE, KEY_TAG, KEY_URGENCY,KEY_CLEVEL,KEY_POSTEDBY,KEY_TIMELIMIT,};

    public SQLiteDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATION_TABLE = "CREATE TABLE Tasks ( "
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "description TEXT, "
                + "fee INTEGER, " + "tag TEXT," + "clevel TEXT," +"ulevel TEXT," +"pby TEXT," + "timelimit INTEGER)";

        db.execSQL(CREATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // you can implement here migration process
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(db);
    }

    public void deleteAll()
    {
        // Get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME);
        db.close();
    }



    public List<Tasks> allTasks() {

        List<Tasks> tasksList = new LinkedList<Tasks>();
        String query = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Tasks task = null;
        if (cursor.moveToFirst()) {
            do {
                task = new Tasks();
                task.setId_(cursor.getString(0));
                task.setDesc_(cursor.getString(1));
                task.setFee_(cursor.getString(2));
                task.setTag_(cursor.getString(3));
                task.setCritical_(cursor.getString(4));
                task.setUrgency_(cursor.getString(5));
                task.setUsername_(cursor.getString(6));
                task.setTime_(cursor.getString(7));
                tasksList.add(task);
            } while (cursor.moveToNext());
        }
        db.close();

        return tasksList;
    }

    public void addTask(Tasks task) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DESC, task.getDesc());
        values.put(KEY_FEE, task.getFee());
        values.put(KEY_TAG, task.getTag());
        values.put(KEY_CLEVEL, task.getCritical());
        values.put(KEY_URGENCY, task.getUrgency());
        values.put(KEY_POSTEDBY, task.getUsername());
        values.put(KEY_TIMELIMIT, task.getTime());


        // insert
        db.insert(TABLE_NAME,null, values);
        db.close();
    }









}