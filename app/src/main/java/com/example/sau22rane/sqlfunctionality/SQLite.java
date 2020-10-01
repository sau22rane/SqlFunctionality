package com.example.sau22rane.sqlfunctionality;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Date;

public class SQLite extends SQLiteOpenHelper {
    private SQLiteDatabase db;
        public SQLite(@Nullable Context context, @Nullable String name, String table, int version) {
        super(context, name, null, version);
            Log.d("TAG","works");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("TAG","OnCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("TAG","OnUpgrade");
    }
    void create_budget_table(){
        db = this.getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS BUDGET(ID INTEGER PRIMARY KEY AUTOINCREMENT, title VARCHAR, amount INTEGER, remaining INTEGER, date DATE);");
        Log.d("TAG","Table created by name: BUDGET");
    }
    boolean add_budget(String title, Integer amount, String date){
        db = this.getWritableDatabase();
        ContentValues mContentValues = new ContentValues();
        mContentValues.put("title",title);
        mContentValues.put("amount",amount);
        mContentValues.put("remaining",amount);
        mContentValues.put("date",date);
        long result = db.insert("BUDGET", null,mContentValues);
        if(result == -1)
            return false;
        else
            return true;
    }
    void create_expenditure_table(String table){
        db = this.getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS "+table+"(ID INTEGER PRIMARY KEY AUTOINCREMENT,title VARCHAR,amount INTEGER, date DATE);");
        Log.d("TAG","Table created by name: ");
    }
    boolean add_expenditure(String table, String title, Integer amount, String date){
        db = this.getWritableDatabase();
        ContentValues mContentValues = new ContentValues();
        mContentValues.put("title",title);
        mContentValues.put("amount",amount);
        mContentValues.put("date",date);
        long result = db.insert(table, null,mContentValues);
        if(result == -1)
            return false;
        else
            return true;
    }
    Cursor get_data(String table){
        db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+table,null);
        return res;
    }
    void delete_row(String table, String title){
        db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+table+" WHERE title LIKE('"+title+"');");
    }
    void delete_table(String table){
        db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS "+table+";");
    }
    void update(String table, String title, String new_title, Integer amount, String date){
        db = this.getWritableDatabase();
        db.execSQL("UPDATE "+table+" SET title = '"+new_title+"', amount = "+amount+", date = '"+date+"' WHERE title like('"+title+"');");
    }
    void rename_table(String table, String new_table){
        db = this.getWritableDatabase();
        db.execSQL("ALTER TABLE "+table+" RENAME TO "+new_table+";");
    }
    void update_remaining(String table, String title, Integer remaining){
        db = this.getWritableDatabase();
        db.execSQL("UPDATE "+table+" SET remaining = "+remaining+" WHERE title like('"+title+"');");
    }
    int get_amount(String title){
        db = this.getWritableDatabase();
        Log.d("TAG","Called");
        Cursor res = db.rawQuery("SELECT * FROM BUDGET WHERE title LIKE('"+title+"')",null);
        if(res.getCount()!=0){
            res.moveToNext();
            int amount = Integer.parseInt(res.getString(2));
            return amount;
        }
        else
            return -1;
    }
    int get_remaining(String title){
        db = this.getWritableDatabase();
        Log.d("TAG","Called");
        Cursor res = db.rawQuery("SELECT * FROM BUDGET WHERE title LIKE('"+title+"')",null);
        if(res.getCount()!=0){
            res.moveToNext();
            int remaining = Integer.parseInt(res.getString(3));
            return remaining;
        }
        else
            return -1;
    }
}
