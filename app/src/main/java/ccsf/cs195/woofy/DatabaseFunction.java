package ccsf.cs195.woofy;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/* 11/9/2021 - Modified getDatabase to getAllDatabase, sql query to pull all rows
 */

public class DatabaseFunction {

    private static String dbpath = "/data/data/ccsf.cs195.woofy/databases/woofy.db";
    private SQLiteDatabase db;

    public void openDatabase() {
        db = SQLiteDatabase.openOrCreateDatabase(dbpath, null);
    }

    public void closeDatabase() {
        db.close();
    }

    public static void initDatabase(Context context) {
        File folder = new File(context.getFilesDir().getParent(), "databases");
        File databaseFile = new File(folder, "woofy.db");
        if (!folder.exists()) {
            folder.mkdirs();
        }

        if (databaseFile.exists()) {
            databaseFile.delete();
        }

        AssetManager assets = context.getAssets();
        try
        {
            InputStream open = assets.open("woofy.db");
            FileOutputStream fileOutStream = new FileOutputStream(databaseFile);
            byte[] byteReadWrite = new byte[1024];
            int len;
            while ((len = open.read(byteReadWrite)) != -1)
            {
                fileOutStream.write(byteReadWrite, 0, len);
            }
            fileOutStream.flush();
            fileOutStream.close();
            open.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    public ArrayList<String> getDatabaseCount(String tableName) {
        openDatabase();
        ArrayList<String> tempString = new ArrayList<>();
        Cursor cursor;

        cursor = db.rawQuery("select count(*) from "+ tableName, null);
        if (cursor.getCount() != 0) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                tempString.add(cursor.getString(0));
            }
        }
        closeDatabase();
        cursor.close();
        return tempString;
    }

    // Modified to pull all data from table to store in 2D ArrayList
    public ArrayList<ArrayList<String>> getAllDatabase(String tableName) {
        openDatabase();
        ArrayList<ArrayList<String>> tempString = new ArrayList<>();
        Cursor cursor;
        cursor = db.rawQuery("select * from "+ tableName, null);


        if (cursor.getCount() != 0) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                ArrayList<String> column = new ArrayList<>();
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    column.add(cursor.getString(i));
                }
                tempString.add(column);
            }
        }
        closeDatabase();
        cursor.close();
        return tempString;
    }

    public ArrayList<String> getDatabase(String tableName,String searchKey, Object number) {
        openDatabase();
        ArrayList<String> tempString = new ArrayList<>();
        Cursor cursor;

        cursor = db.rawQuery("select * from "+ tableName +
                " where " + searchKey +" =? ", new String[]{String.valueOf(number)});


        if (cursor.getCount() != 0) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    tempString.add(cursor.getString(i));
                }

            }
        }
        closeDatabase();
        cursor.close();
        return tempString;
    }

}
