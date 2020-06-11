package com.example.punchcard.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.punchcard.bean.PunchCard;
import com.example.punchcard.utils.DBUtils;

import java.util.ArrayList;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {
    private SQLiteDatabase sqLiteDatabase;
    private static final String TAG = "SQLiteOpenHelper" ;
    //创建数据库
    public SQLiteHelper(Context context){
        super(context, DBUtils.DATABASE_NAME, null, DBUtils.DATABASE_VERION);
        sqLiteDatabase = this.getWritableDatabase();
    }

    //创建表
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+DBUtils.DATABASE_TABLE+"("+DBUtils._ID+
                " integer primary key autoincrement,"+ DBUtils._ITEM +
                " text," + DBUtils._TIMES+ " integer," + DBUtils._ACTIVITY +
                " integer," + DBUtils._DATE + " text," + DBUtils._ICON_ID+
                " text,"+DBUtils._CALID +
                " text," + DBUtils._SYEAR + " ingeter," + DBUtils._SMONTH +
                " ingeter," + DBUtils._SDAY+ " integer," + DBUtils._SHOUR +
                " integer," + DBUtils._SMINUTE + " ingeter," + DBUtils._EVENTID +
                " long," + DBUtils._TS + " integer)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    //添加数据，新建一个项目。新建项目时times和activity默认为0
    public boolean insertData(String item, String date, Integer iconId){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBUtils._ITEM,item);
        contentValues.put(DBUtils._TIMES,0);
        contentValues.put(DBUtils._ACTIVITY,0);
        contentValues.put(DBUtils._DATE,date);
        contentValues.put(DBUtils._ICON_ID, iconId);
        contentValues.put(DBUtils._CALID,0);
        contentValues.put(DBUtils._SYEAR,0);
        contentValues.put(DBUtils._SMONTH,0);
        contentValues.put(DBUtils._SDAY,0);
        contentValues.put(DBUtils._SHOUR,0);
        contentValues.put(DBUtils._SMINUTE,0);
        contentValues.put(DBUtils._EVENTID,0);
        contentValues.put(DBUtils._TS,0);
        return
                sqLiteDatabase.insert(DBUtils.DATABASE_TABLE,null,contentValues)>0;
    }

    //删除数据，根据id删除
    public boolean deleteData(String id){
        String sql=DBUtils._ID+"=?";
        String[] contentValuesArray=new String[]{String.valueOf(id)};
        return
                sqLiteDatabase.delete(DBUtils.DATABASE_TABLE,sql,contentValuesArray)>0;
    }

    //修改数据
    public Boolean updateData(String id, Integer times ,Integer activity, String date){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBUtils._TIMES, times);
        contentValues.put(DBUtils._ACTIVITY, activity);
        contentValues.put(DBUtils._DATE, date);
        //contentValues.put(DBUtils._ITEM,item);
        String sql=DBUtils._ID+"=?";
        String[] strings=new String[]{id};
        return
                sqLiteDatabase.update(DBUtils.DATABASE_TABLE,contentValues,sql,strings)>0;

        /*String sql = "update " + DBUtils.DATABASE_TABLE + " set times=" + times + ",set activity=" + activity +" where id=" + id;
        sqLiteDatabase.execSQL(sql);*/
    }

    //修改日历闹钟数据
    public Boolean updateClockData(String id ,Integer calID,Integer year, Integer month,Integer day,Integer hour,Integer minute,long eid,Integer ts){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBUtils._CALID, calID);
        contentValues.put(DBUtils._SYEAR, year);
        contentValues.put(DBUtils._SMONTH, month);
        contentValues.put(DBUtils._SDAY, day);
        contentValues.put(DBUtils._SHOUR, hour);
        contentValues.put(DBUtils._SMINUTE, minute);
        contentValues.put(DBUtils._EVENTID, eid);
        contentValues.put(DBUtils._TS, ts);
        //contentValues.put(DBUtils._ITEM,item);
        String sql=DBUtils._ID+"=?";
        String[] strings=new String[]{id};
        return
                sqLiteDatabase.update(DBUtils.DATABASE_TABLE,contentValues,sql,strings)>0;

        /*String sql = "update " + DBUtils.DATABASE_TABLE + " set times=" + times + ",set activity=" + activity +" where id=" + id;
        sqLiteDatabase.execSQL(sql);*/
    }
    //查询数据
    public List<PunchCard> query(){
        List<PunchCard> list = new ArrayList<PunchCard>();
        Cursor cursor=sqLiteDatabase.query(DBUtils.DATABASE_TABLE,null,null,null,
                null,null,DBUtils._ID+" desc");
        if (cursor!=null){
            while (cursor.moveToNext()){
                PunchCard info = new PunchCard();

                String id = String.valueOf(cursor.getInt(cursor.getColumnIndex(DBUtils._ID)));
                String item = cursor.getString(cursor.getColumnIndex(DBUtils._ITEM));
                Integer times = cursor.getInt(cursor.getColumnIndex(DBUtils._TIMES));
                Integer activity = cursor.getInt(cursor.getColumnIndex(DBUtils._ACTIVITY));
                String date = cursor.getString(cursor.getColumnIndex(DBUtils._DATE));
                Integer iconId = cursor.getInt(cursor.getColumnIndex(DBUtils._ICON_ID));
                Integer calID = cursor.getInt(cursor.getColumnIndex(DBUtils._CALID));
                Integer syear = cursor.getInt(cursor.getColumnIndex(DBUtils._SYEAR));
                Integer smonth = cursor.getInt(cursor.getColumnIndex(DBUtils._SMONTH));
                Integer sday = cursor.getInt(cursor.getColumnIndex(DBUtils._SDAY));
                Integer shour = cursor.getInt(cursor.getColumnIndex(DBUtils._SHOUR));
                Integer sminute = cursor.getInt(cursor.getColumnIndex(DBUtils._SMINUTE));
                Long eventid = cursor.getLong(cursor.getColumnIndex(DBUtils._EVENTID));
                //将数据库存储的日期跟当前日期进行比较，不能直接用“==”进行比较，“==”比较的是地址
                if(!date.equals(DBUtils.getTime()) ) {
                    //不一样，说明已经过了一天，将activity置0，且将数据库中的activity、date更新
                    activity = 0;
                    this.updateData(id, times, 0, DBUtils.getTime());
                }

                info.setId(id);
                info.setItem(item);
                info.setTimes(times);
                info.setActivity(activity);
                info.setDate(date);
                info.setIconId(iconId);
                info.setCalID(calID);
                info.setSyear(syear);
                info.setSmonth(smonth);
                info.setSday(sday);
                info.setShour(shour);
                info.setSminute(sminute);
                info.setEventId(eventid);
                list.add(info);
                Log.d(TAG,"id=============="+ id);
                Log.d(TAG,"item============"+item);
                Log.d(TAG,"eventId=============" + eventid);
                Log.d(TAG,"syear=============" + syear);
            }
            cursor.close();
        }
        return list;
    }

    //根据punchcardId查询eventId
    public PunchCard queryById(String punchcardId){
        PunchCard info = new PunchCard();
        Cursor cursor=sqLiteDatabase.query(DBUtils.DATABASE_TABLE,
                null,
                DBUtils._ID+"=?",
                new String[]{punchcardId},
                null,
                null,
                DBUtils._ID+" desc");
        if (cursor!=null) {
            while (cursor.moveToNext()) {
                String id = String.valueOf(cursor.getInt(cursor.getColumnIndex(DBUtils._ID)));
                String item = cursor.getString(cursor.getColumnIndex(DBUtils._ITEM));
                Integer times = cursor.getInt(cursor.getColumnIndex(DBUtils._TIMES));
                Integer calID = cursor.getInt(cursor.getColumnIndex(DBUtils._CALID));
                Integer syear = cursor.getInt(cursor.getColumnIndex(DBUtils._SYEAR));
                Integer smonth = cursor.getInt(cursor.getColumnIndex(DBUtils._SMONTH));
                Integer sday = cursor.getInt(cursor.getColumnIndex(DBUtils._SDAY));
                Integer shour = cursor.getInt(cursor.getColumnIndex(DBUtils._SHOUR));
                Integer sminute = cursor.getInt(cursor.getColumnIndex(DBUtils._SMINUTE));
                long eventid = cursor.getLong(cursor.getColumnIndex(DBUtils._EVENTID));
                Integer ts = cursor.getInt(cursor.getColumnIndex(DBUtils._TS));

                info.setId(id);
                info.setItem(item);
                info.setTimes(times);
                info.setEventId(eventid);
                info.setCalID(calID);
                info.setSyear(syear);
                info.setSmonth(smonth);
                info.setSday(sday);
                info.setShour(shour);
                info.setSminute(sminute);
                info.setEventId(eventid);
                info.setTs(ts);
                break;
            }
            cursor.close();
        }
        return info;
    }

}
