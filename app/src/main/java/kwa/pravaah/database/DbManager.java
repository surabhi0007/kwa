package kwa.pravaah.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DbManager extends SQLiteOpenHelper {
    private static final String TAG = "KWATest: DBmanager ";

    public static final String DATABASE_NAME = "KWA.db";
    public static final String TABLE_SMS = "sms_table";
    public static final String ID="id";
    public static final String ID_REG="id_reg";
    public static final String TABLE_REG = "reg_table";
    public static final String SL_NO = "sl_no";
    public static final String MOBILE_NO = "phone";
    public static final String PHN_NO = "phone_reg";
    public static final String NAME_REG = "name_reg";
    public static final String NAME = "name";
    public static final String POWER = "power";
    public static final String PUMP = "pump";
    public static final String SHEET_ID = "sheet_id";
    public static final String PENDING_INTENT_ON="alarmid1";
    public static final String PENDING_INTENT_OFF="alarmid2";
    public static final String TIME_ON = "time_on";
    public static final String TIME_OFF = "time_off";

    public DbManager(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + TABLE_SMS + "(" + ID +" integer primary key autoincrement ,"
                + MOBILE_NO + " text,"
                + NAME + " text,"
                + POWER + " text,"
                + PUMP + " text,"
                + PENDING_INTENT_ON +" text,"
                + PENDING_INTENT_OFF +" text,"
                + TIME_ON +" text unique,"
                + TIME_OFF +" text unique )");

        db.execSQL("create table "+ TABLE_REG + "(" + ID_REG + " integer primary key autoincrement ,"
                + PHN_NO + " text unique  not null ,"
                + NAME_REG + " text ,"
                + SHEET_ID + " text ) " );
        Log.i(TAG,"db : created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }
    public int numOfRows1() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        int numOfRows = (int) DatabaseUtils.queryNumEntries(sqLiteDatabase, TABLE_REG);
        return numOfRows;
    }

    public int numOfRows() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        int numOfRows = (int) DatabaseUtils.queryNumEntries(sqLiteDatabase, TABLE_SMS);
        return numOfRows;
    }
    public boolean insertUserDetails(String no,String name,String power, String pump, String indent_to_on,
                                     String intent_to_off, String time_on, String time_off) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(MOBILE_NO, no);
        contentValues.put(NAME, name);
        contentValues.put(POWER, power);
        contentValues.put(PUMP, pump);
        contentValues.put(PENDING_INTENT_ON, indent_to_on);
        contentValues.put(PENDING_INTENT_OFF, intent_to_off);
        contentValues.put(TIME_ON, time_on);
        contentValues.put(TIME_OFF, time_off);

        db.insert(TABLE_SMS, null, contentValues);
        return true;
    }

    public boolean insertPumpDetails(String num,String name,String sheetid) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(PHN_NO, num);
        contentValues.put(NAME_REG, name);
        contentValues.put(SHEET_ID, sheetid);

        db.insert(TABLE_REG, null, contentValues);
        Log.i(TAG,"db : succesful");
        return true;
    }

    public boolean UpdateDetails(String no,String power, String pump)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(POWER, power);
        contentValues.put(PUMP, pump);
        db.update(TABLE_SMS,contentValues, MOBILE_NO + "=" + "'" +no+ "'",null);
        return true;

    }
    public String getTime_off(String no , String intent_to_on )
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select "+TIME_OFF+" from " + TABLE_SMS + "where" + MOBILE_NO + "=" + "'" + no + "'" + PENDING_INTENT_ON + "=" + "''" + intent_to_on+"''",null);
        return TIME_OFF;

    }
       public Boolean CheckNumber(String no) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select " + MOBILE_NO+ " from " + TABLE_SMS + " where " + MOBILE_NO + " = " + "'" + no + "'" , null);
        if (res.getCount() == 0) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getPowerStatus(String no) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select " + POWER + " from " + TABLE_SMS + " where " + MOBILE_NO + " = " + "'" + no + "'" , null);
        return res;
    }

    public Cursor getPumpStatus(String no) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select " + PUMP + " from " + TABLE_SMS + " where " + MOBILE_NO + " = " + "'" + no + "'" , null);
        return res;
    }

    public String getPendingON(String no) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select " + PENDING_INTENT_ON + " from " + TABLE_SMS + " where " + PENDING_INTENT_ON + " = " + "'"+ no +"'", null);
        return PENDING_INTENT_ON;
    }
    public String getPendingOFF(String no) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select " + PENDING_INTENT_OFF + " from " + TABLE_SMS + " where " + PENDING_INTENT_ON + " = " +"'"+ no+"'" , null);
        return PENDING_INTENT_OFF;
    }
    public Cursor getPendingIntent(String no) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select "+PENDING_INTENT_ON + " , " + PENDING_INTENT_OFF + " from " + TABLE_SMS + " where " + ID + " = " + no , null);
        return res;
    }
    public Cursor getnumber(String nm) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select " + PHN_NO+ " from " + TABLE_REG + " where " + NAME_REG + " = " + "'" + nm + "'", null);
         return  res;
    }



    public void updatePumpStatus(String no, String pump) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PUMP, pump);
        db.update(TABLE_SMS,contentValues,MOBILE_NO+"="+no,null);

    }


    public void addPendingIntent_ON(String no, String pending) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PENDING_INTENT_ON, pending);
        db.update(TABLE_SMS,contentValues,MOBILE_NO+"="+"'"+no+"'",null);

    }

    public void addPendingIntent_OFF(String pending_on,String pending) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PENDING_INTENT_OFF, pending);
        db.update(TABLE_SMS,contentValues, PENDING_INTENT_ON + "=" +"'"+ pending_on+"'",null);

    }

    public void addTime_ON(String no, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TIME_ON, time);
        db.update(TABLE_SMS,contentValues,MOBILE_NO + " = " + no,null);

    }

    public void addTime_OFF(String  pending_on, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TIME_OFF, time);
        db.update(TABLE_SMS,contentValues,PENDING_INTENT_ON + "=" +"'"+ pending_on+"'",null);
    }

    public void deleteRow(String data)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_SMS+ " WHERE "+ID+"='"+data+"'");
        db.close();
    }

    public Cursor getDataUsername( String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select " + NAME + " from " + TABLE_SMS + " where " + ID + " = " + "'" + id + "'", null);
        return res;
    }

    public Cursor viewData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select "+ID +"," +NAME +","  +POWER +"," +PUMP +"," +TIME_ON +"," +TIME_OFF + " from " + TABLE_SMS , null);
        return res;
    }


    public List<String> getDetails(){
        List<String> rows = new ArrayList<String>();


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT "+ID+" FROM " + TABLE_SMS, null);


        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                rows.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return rows;
    }
    public List<String> getPumpDetails(){
        List<String> rows1 = new ArrayList<String>();


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT "+ NAME_REG +" FROM " + TABLE_REG, null);


        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                rows1.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return rows1;
    }
}
