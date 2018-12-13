package com.sports.sportclub.local_db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.OperationCanceledException;

import java.util.ArrayList;

public class DbManager {

    private DBHelper dbHelper;
    private static DbManager Db_M = null;

    private DbManager(Context context){
        dbHelper=new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //在这里创建表
        String sql = "CREATE TABLE IF NOT EXISTS USER ("
                + "id varchar(1) PRIMARY KEY, username varchar(20)," + "password varchar(20))";
        db.execSQL(sql);
        if (select(new String[]{"id"},new String[]{"USER"},null,null).size() == 0){
            insert("USER",new String[]{"id","username","password"}
                    ,new String[]{"1","admin","admin"});
        }

        sql = "CREATE TABLE IF NOT EXISTS COACH ("
                + "name varchar(30), photo varchar(100)," + "introduction varchar(20))";
        SQLiteDatabase dbs = dbHelper.getWritableDatabase();
        dbs.execSQL(sql);
        if (select(new String[]{"name"},new String[]{"COACH"},null,null).size() == 0) {
            insert("COACH", new String[]{"name", "photo", "introduction"}
                    , new String[]{"local_coach1", "local_photo1_url1", "local_introduction1"});
            insert("COACH", new String[]{"name", "photo", "introduction"}
                    , new String[]{"local_coach2", "local_photo1_url2", "local_introduction2"});
            insert("COACH", new String[]{"name", "photo", "introduction"}
                    , new String[]{"local_coach3", "local_photo1_url3", "local_introduction3"});
            insert("COACH", new String[]{"name", "photo", "introduction"}
                    , new String[]{"local_coach4", "local_photo1_url4", "local_introduction4"});
            insert("COACH", new String[]{"name", "photo", "introduction"}
                    , new String[]{"local_coach5", "local_photo1_url5", "local_introduction5"});
        }
        db.close();
        dbs.close();
    }

    public static DbManager getDb_M(Context context){
        if (Db_M == null){
            Db_M = new DbManager(context);
        }
        return Db_M;
    }

    /**
     * @param tablename the name of table
     * @param field the field you want to set
     * @param fieldvalue the value of each field
     * @return long(Object_id) the object id for the data you have insert
     */
    public int insert(String tablename,String[] field,String[] fieldvalue){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //打开连接，写入数据
        ContentValues values=new ContentValues();
        for (int i = 0;i < field.length;i++){
            values.put(field[i],fieldvalue[i]);
        }

        //
        long student_Id=db.insert(tablename,null,values);
        db.close();
        return (int)student_Id;
    }

    /**
     * @param tablename the name of table
     * @param field the field you want to set
     * @param fieldvalue the value of each field
     */
    public void delete(String tablename,String[] field,String[] fieldvalue){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = "";
        for (int i = 0;i < field.length;i++){
            if (i == field.length - 1){
                whereClause += field[i] + "=?";
                break;
            }
            whereClause += field[i] + "=?,";
        }
        db.delete(tablename,whereClause, fieldvalue);
        db.close();
    }

    /**
     * @param tablename the name of table
     * @param field the field you want to set
     * @param fieldvalue the value of each field
     * @param selectfield this can help you find the data you want to updata
     * @param selectfieldvalue this can help you find the data you want to updata
     */
    public void update(String tablename,String[] field,String[] fieldvalue,String[] selectfield,String[] selectfieldvalue){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = "";
        for (int i = 0;i < selectfield.length;i++){
            if (i == selectfield.length - 1){
                whereClause += selectfield[i] + "=?";
                break;
            }
            whereClause += selectfield[i] + "=?,";
        }
        ContentValues values=new ContentValues();
        for (int i = 0;i < field.length;i++){
            values.put(field[i],fieldvalue[i]);
        }

        db.update(tablename,values,whereClause,selectfieldvalue);
        db.close();
    }

    /**
     * @param selectlist your selectClause
     * @param formlist your fromClause
     * @param wherelist your whereClause
     * @param wherelistvalue the value of whereClause "?"
     * @return String[] the result set of select operation
     */
    public ArrayList<String[]> select(String[] selectlist, String[] formlist, String[] wherelist, String[] wherelistvalue){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selectQuery="SELECT * ";
        for (int i = 0;i < formlist.length;i++){
            if (i == 0){
                selectQuery += "FROM " + formlist[i];
            }else {
                selectQuery += "," + formlist[i];
            }
        }
        if (wherelist != null) {
            for (int i = 0; i < wherelist.length; i++) {
                if (i == 0) {
                    selectQuery += " WHERE " + wherelist[i] + "=?";
                } else {
                    selectQuery += "," + wherelist[i] + "=?";
                }
            }
        }

        ArrayList<String[]> ResultSet=new ArrayList<String[]>();
        Cursor cursor=db.rawQuery(selectQuery,wherelistvalue);

        if(cursor.moveToFirst()){
            do{
                String[] message=new String[selectlist.length];
                for (int i = 0;i < selectlist.length;i++){
                    message[i] = cursor.getString(cursor.getColumnIndex(selectlist[i]));
                }
                ResultSet.add(message);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return ResultSet;
    }
}
