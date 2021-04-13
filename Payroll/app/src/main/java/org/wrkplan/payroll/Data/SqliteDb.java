package org.wrkplan.payroll.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.wrkplan.payroll.Model.SubordinateLeaveApplicationModel;

import java.util.ArrayList;

public class SqliteDb extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Payroll";
    // private static final String[] TABLES = new String[] { "student_table"};
    public static final String TABLE_NAME = "EmployeeDetails";
    public static String nameColumn = "employee_name";



    public SqliteDb(Context context) {
        super(context, DATABASE_NAME,null,1);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        String tableEmp=("create table "+TABLE_NAME+ " (appliction_code text,appliction_id integer primary key  ,approved_by text,approved_by_id integer,approved_date text,approved_level integer,description text,employee_id integer, "+nameColumn+" text,final_approved_by text,from_date text,leave_name text,leave_status text,supervisor1_id integer,supervisor2_id integer,supervisor_remark text,total_days text,to_date text )");
        db.execSQL(tableEmp);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);

    }
    public void deleteAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME);
        db.close();
    }

    public void insertData(String appliction_code,Integer appliction_id, String approved_by,
                           Integer approved_by_id,String approved_date,Integer approved_level,String description,
                           Integer employee_id,String employee_name,String final_approved_by,String from_date,
                           String leave_name,String leave_status,Integer supervisor1_id,Integer supervisor2_id,
                           String supervisor_remark,Integer total_days,String to_date)
    {
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("appliction_code",appliction_code);
        values.put("appliction_id",appliction_id);
        values.put("approved_by",approved_by);
        values.put("approved_by_id",approved_by_id);
        values.put("approved_date",approved_date);
        values.put("approved_level",approved_level);
        values.put("description",description);
        values.put("employee_id",employee_id);
        values.put("employee_name",employee_name);
        values.put("final_approved_by",final_approved_by);
        values.put("from_date",from_date);
        values.put("leave_name",leave_name);
        values.put("leave_status",leave_status);
        values.put("supervisor1_id",supervisor1_id);
        values.put("supervisor2_id",supervisor2_id);
        values.put("supervisor_remark",supervisor_remark);
        values.put("total_days",total_days);
        values.put("to_date",to_date);
        sqLiteDatabase.insert(TABLE_NAME,null,values);
        sqLiteDatabase.close();
    }

    public ArrayList<SubordinateLeaveApplicationModel> getAllData() {
        ArrayList<SubordinateLeaveApplicationModel> subordinateLeaveApplicationModelArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "+TABLE_NAME,null);

        while(cursor.moveToNext()) {

            String appliction_code=cursor.getString(0);
            Integer appliction_id=cursor.getInt(1);
            String approved_by=cursor.getString(2);
            Integer approved_by_id=cursor.getInt(3);
            String approved_date=cursor.getString(4);
            Integer approved_level=cursor.getInt(5);
            String description=cursor.getString(6);
            Integer employee_id=cursor.getInt(7);
            String employee_name=cursor.getString(8);
            String final_approved_by=cursor.getString(9);
            String from_date=cursor.getString(10);
            String leave_name=cursor.getString(11);
            String leave_status=cursor.getString(12);
            Integer supervisor1_id=cursor.getInt(13);
            Integer supervisor2_id=cursor.getInt(14);
            String supervisor_remark=cursor.getString(15);
            Integer total_days=cursor.getInt(16);
            String to_date=cursor.getString(17);


            SubordinateLeaveApplicationModel subordinateLeaveApplicationModel = new SubordinateLeaveApplicationModel(appliction_code,appliction_id,approved_by,approved_by_id,approved_date,approved_level,
                    description,employee_id,employee_name,final_approved_by,from_date,leave_name,leave_status,
                    supervisor1_id,supervisor2_id,supervisor_remark,total_days,to_date);
            subordinateLeaveApplicationModelArrayList.add(subordinateLeaveApplicationModel);
        }
        return subordinateLeaveApplicationModelArrayList;
    }
    public ArrayList<SubordinateLeaveApplicationModel> Seatchdata(String keyword) {
        ArrayList<SubordinateLeaveApplicationModel> subordinateLeaveApplicationModelArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where " + nameColumn + " like ?", new String[] { "%" + keyword + "%" });

        while(cursor.moveToNext()) {

            String appliction_code=cursor.getString(0);
            Integer appliction_id=cursor.getInt(1);
            String approved_by=cursor.getString(2);
            Integer approved_by_id=cursor.getInt(3);
            String approved_date=cursor.getString(4);
            Integer approved_level=cursor.getInt(5);
            String description=cursor.getString(6);
            Integer employee_id=cursor.getInt(7);
            String employee_name=cursor.getString(8);
            String final_approved_by=cursor.getString(9);
            String from_date=cursor.getString(10);
            String leave_name=cursor.getString(11);
            String leave_status=cursor.getString(12);
            Integer supervisor1_id=cursor.getInt(13);
            Integer supervisor2_id=cursor.getInt(14);
            String supervisor_remark=cursor.getString(15);
            Integer total_days=cursor.getInt(16);
            String to_date=cursor.getString(17);


            SubordinateLeaveApplicationModel setget = new SubordinateLeaveApplicationModel(appliction_code,appliction_id,approved_by,approved_by_id,approved_date,approved_level,
                    description,employee_id,employee_name,final_approved_by,from_date,leave_name,leave_status,
                    supervisor1_id,supervisor2_id,supervisor_remark,total_days,to_date);
            subordinateLeaveApplicationModelArrayList.add(setget);
        }
        return subordinateLeaveApplicationModelArrayList;
    }
}
