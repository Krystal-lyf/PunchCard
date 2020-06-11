package com.example.punchcard.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.SearchRecentSuggestionsProvider;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.punchcard.bean.PunchStatistics;
import com.example.punchcard.utils.DBUtils;
import com.example.punchcard.utils.StatisticsDBUtils;

import java.util.ArrayList;
import java.util.List;

public class StatisticsDbHelper extends SQLiteOpenHelper {
    private SQLiteDatabase StatisticDatabase;
    public StatisticsDbHelper(Context context) {
        super(context, StatisticsDBUtils.DATABASE_NAME, null, StatisticsDBUtils.DATABASE_VERSION);
        StatisticDatabase = this.getWritableDatabase();
    }

    //创建表
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+ StatisticsDBUtils.DATABASE_TABLE+ " ("
                + StatisticsDBUtils._ID +" integer primary key autoincrement,"
                + StatisticsDBUtils.PROJECT_NAME + " text,"
                + StatisticsDBUtils.YEAR+ " text,"
                + StatisticsDBUtils.MONTH + " text,"
                + StatisticsDBUtils.DAY + " text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    //添加数据，新建一个项目
    public PunchStatistics PunchCard(PunchStatistics statistic){
        ContentValues contentValues = new ContentValues();
        contentValues.put(StatisticsDBUtils.PROJECT_NAME,statistic.getProjectName());
        contentValues.put(StatisticsDBUtils.YEAR,statistic.getYear());
        contentValues.put(StatisticsDBUtils.MONTH,statistic.getMonth());
        contentValues.put(StatisticsDBUtils.DAY,statistic.getDay());
        long insertId = StatisticDatabase.insert(StatisticsDBUtils.DATABASE_TABLE,null,contentValues);
        statistic.setId((int)insertId);
        return statistic;
    }
    //多条件查询，获得符合条件的一个对象数组
    public List<PunchStatistics> GetStatistics(int itemId, int year ,int month){
        List<PunchStatistics> Statistics = new ArrayList<>();
        Cursor cursor = StatisticDatabase.rawQuery("select * from "+StatisticsDBUtils.DATABASE_TABLE +" where "+ StatisticsDBUtils.PROJECT_NAME +" == "+ itemId+" and "+
                StatisticsDBUtils.YEAR+ " == "+year + " and " + StatisticsDBUtils.MONTH + " == "+ month ,null);
        if(cursor.getCount()>0){
            while (cursor.moveToNext()){
                PunchStatistics statistic = new PunchStatistics();
                statistic.setId(cursor.getInt(cursor.getColumnIndex(StatisticsDBUtils._ID)));
                statistic.setProjectName(cursor.getInt(cursor.getColumnIndex(StatisticsDBUtils.PROJECT_NAME)));
                statistic.setYear(cursor.getInt(cursor.getColumnIndex(StatisticsDBUtils.YEAR)));
                statistic.setMonth(cursor.getInt(cursor.getColumnIndex(StatisticsDBUtils.MONTH)));
                statistic.setDay(cursor.getInt(cursor.getColumnIndex(StatisticsDBUtils.DAY)));
                Statistics.add(statistic);
            }
        }
        return Statistics;
    }

    //根据punchcard_id和月数查询本月打卡次数
    public Integer queryByMounth(int itemId,int month){
        Integer monthTimes = 0;
        Cursor cursor = StatisticDatabase.rawQuery("select * from "+StatisticsDBUtils.DATABASE_TABLE +" where "+ StatisticsDBUtils.PROJECT_NAME +" == "+ itemId+" and "+
                StatisticsDBUtils.MONTH+ " == "+ month  ,null);
        if(cursor.getCount()>0) {
            while (cursor.moveToNext()) {
                monthTimes++;
            }
        }
        return monthTimes;
    }
}