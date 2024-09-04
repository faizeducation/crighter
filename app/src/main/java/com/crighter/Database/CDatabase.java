package com.crighter.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by gold on 10/2/2018.
 */

public class CDatabase extends SQLiteOpenHelper {
    Context context;
    
    public static final String DATABASE_NAME = "crighter.db";
    private static final int DatabaseVersion = 1;
    public static final String NAME_OF_TABLE = "crighter";
    public static final String Col_1 = "id";
    public static final String Col_2 = "status";//0 mean uploaded 1 mean not uploaded
    public static final String Col_3 = "userid";
    public static final String Col_4 = "audiopath";
    public static final String Col_5 = "image1";
    public static final String Col_6 = "image2";
    public static final String Col_7 = "image3";
    public static final String Col_8 = "image4";
    public static final String Col_9 = "image5";
    public static final String Col_10 = "lat";
    public static final String Col_11 = "lng";
    

    String CREATE_TABLE_CALL = "CREATE TABLE " + NAME_OF_TABLE
            + "("
            + Col_1 + " integer PRIMARY KEY AUTOINCREMENT,"
            + Col_2 + " TEXT, "
            + Col_3 + " TEXT, "
            + Col_4 + " TEXT, "
            + Col_5 + " TEXT, "
            + Col_6 + " TEXT, "
            + Col_7 + " TEXT, "
            + Col_8 + " TEXT, "
            + Col_9 + " TEXT, "
            + Col_10 + " TEXT, "
            + Col_11 + " TEXT "
            + ")";


    public CDatabase(Context context) {
        super(context, DATABASE_NAME, null, DatabaseVersion);
        this.context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_CALL);
        //db.execSQL(Create_Virtual_Table_Call);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NAME_OF_TABLE);
    }

    //inserting post in databse
    public long insertDatatoDb(DbHelper helper) {
        long result;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //values.put(Col_1, post.getId());
        values.put(Col_2, helper.getStatus());
        values.put(Col_3, helper.getUserid());
        values.put(Col_4, helper.getAudiopath());
        values.put(Col_5, helper.getImage1());
        values.put(Col_6, helper.getImage2());
        values.put(Col_7, helper.getImage3());
        values.put(Col_8, helper.getImage4());
        values.put(Col_9, helper.getImage5());
        values.put(Col_10, helper.getLat());
        values.put(Col_11, helper.getLng());

        //inserting valuse into table columns
        result = db.insert(NAME_OF_TABLE, null, values);
        db.close();
        return result;

    }

    /* fetching records from Database Table*/
    public ArrayList<DbHelper> gettingAllRecords() {
        String query = "SELECT * FROM " + NAME_OF_TABLE;
        ArrayList<DbHelper> addingToList = new ArrayList<DbHelper>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        if (c != null) {
            while (c.moveToNext()) {
                DbHelper myHelper = new DbHelper();
                String id = c.getString(c.getColumnIndex(Col_1));
                String status = c.getString(c.getColumnIndex(Col_2));
                String userId = c.getString(c.getColumnIndex(Col_3));
                String audioPath= c.getString(c.getColumnIndex(Col_4));
                String image1 = c.getString(c.getColumnIndex(Col_5));
                String image2 = c.getString(c.getColumnIndex(Col_6));
                String image3 = c.getString(c.getColumnIndex(Col_7));
                String image4 = c.getString(c.getColumnIndex(Col_8));
                String image5 = c.getString(c.getColumnIndex(Col_9));
                String lat = c.getString(c.getColumnIndex(Col_10));
                String lng = c.getString(c.getColumnIndex(Col_11));

                myHelper.setId(id);
                myHelper.setStatus(status);
                myHelper.setUserid(userId);
                myHelper.setAudiopath(audioPath);
                myHelper.setImage1(image1);
                myHelper.setImage2(image2);
                myHelper.setImage3(image3);
                myHelper.setImage4(image4);
                myHelper.setImage5(image5);
                myHelper.setLat(lat);
                myHelper.setLng(lng);

                //adding data to array list
                addingToList.add(myHelper);

            }
        }

        db.close();
        return addingToList;
    }


    //fetch single record
    public ArrayList<DbHelper> getOrderByStatus(String mStatus) {
        String query = "SELECT * FROM " + NAME_OF_TABLE +" WHERE status="+mStatus;
        Log.e("TAg", "the id is: " + query);
        ArrayList<DbHelper> addingToList = new ArrayList<DbHelper>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        if (c != null) {
            while (c.moveToNext()) {
                DbHelper myHelper = new DbHelper();
                String id = c.getString(c.getColumnIndex(Col_1));
                String status = c.getString(c.getColumnIndex(Col_2));
                String userId = c.getString(c.getColumnIndex(Col_3));
                String audioPath= c.getString(c.getColumnIndex(Col_4));
                String image1 = c.getString(c.getColumnIndex(Col_5));
                String image2 = c.getString(c.getColumnIndex(Col_6));
                String image3 = c.getString(c.getColumnIndex(Col_7));
                String image4 = c.getString(c.getColumnIndex(Col_8));
                String image5 = c.getString(c.getColumnIndex(Col_9));
                String lat = c.getString(c.getColumnIndex(Col_10));
                String lng = c.getString(c.getColumnIndex(Col_11));

                myHelper.setId(id);
                myHelper.setStatus(status);
                myHelper.setUserid(userId);
                myHelper.setAudiopath(audioPath);
                myHelper.setImage1(image1);
                myHelper.setImage2(image2);
                myHelper.setImage3(image3);
                myHelper.setImage4(image4);
                myHelper.setImage5(image5);
                myHelper.setLat(lat);
                myHelper.setLng(lng);

                //adding data to array list
                addingToList.add(myHelper);

            }
        }

        db.close();
        return addingToList;

    }

    //Updatating post
    public boolean updateTable(int id, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Col_2,status);
        db.update(NAME_OF_TABLE, contentValues, "id = ?", new String[]{Integer.toString(id)});
        db.close();
        return true;
    }

    //deleting post
    public boolean deleteFromTable(long rowId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(NAME_OF_TABLE, Col_1 + "=" + rowId, null);
        db.close();

        return true;

    }

    //

    public int getCount(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + NAME_OF_TABLE;
        return db.rawQuery(query, null).getCount();
    }

}