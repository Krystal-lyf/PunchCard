package com.example.punchcard.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NoteDBUtils {
    public static final String DATABASE_NAME = "Notes.db";  //数据库名
    public static final String DATABASE_TABLE = "Notes";     //表名
    public static final int DATABASE_VERION = 1;                 //数据库版本

    //数据库表中的列名
    public static final String _ID = "id";                           //id
    public static final String _PUNCHCARD_ID = "punchcard_id";     //打卡项目id
    public static final String _CONTENT = "content";                //笔记内容
    public static final String _TIME = "time";                      //记录写笔记时间

    //获取当前日期
    public static final String getTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd HH:mm");
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }
}
