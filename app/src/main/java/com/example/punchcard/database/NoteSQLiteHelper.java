package com.example.punchcard.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.punchcard.bean.Note;
import com.example.punchcard.utils.DBUtils;
import com.example.punchcard.utils.NoteDBUtils;

import java.util.ArrayList;
import java.util.List;


public class NoteSQLiteHelper extends SQLiteOpenHelper {

    private static final String TAG = "NotesSQLiteHelper" ;
    private SQLiteDatabase sqLiteDatabase;

    public NoteSQLiteHelper(Context context) {
        super(context,NoteDBUtils.DATABASE_NAME , null, NoteDBUtils.DATABASE_VERION);
        sqLiteDatabase = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+NoteDBUtils.DATABASE_TABLE+"("+NoteDBUtils._ID+
                " integer primary key autoincrement,"+ NoteDBUtils._PUNCHCARD_ID +
                " text," + NoteDBUtils._CONTENT+ " text," + NoteDBUtils._TIME + " text)");
        Log.d(TAG,"notes数据库创建了。。。。。。。。。");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }

    //添加数据，新建一个项目。新建项目时times和activity默认为0
    public boolean insertData(String punchcardId, String content, String time){
        ContentValues contentValues = new ContentValues();
        contentValues.put(NoteDBUtils._PUNCHCARD_ID, punchcardId);
        contentValues.put(NoteDBUtils._CONTENT, content);
        contentValues.put(NoteDBUtils._TIME, time);
        return
                sqLiteDatabase.insert(NoteDBUtils.DATABASE_TABLE,null,contentValues)>0;
    }
    //删除数据，根据id删除
    public boolean deleteData(String id){
        String sql=NoteDBUtils._ID+"=?";
        String[] contentValuesArray=new String[]{String.valueOf(id)};
        return
                sqLiteDatabase.delete(NoteDBUtils.DATABASE_TABLE,sql,contentValuesArray)>0;
    }

    //查询数据
    public List<Note> query(String punchcardId){
        List<Note> list = new ArrayList<>();
        Cursor cursor=sqLiteDatabase.query(NoteDBUtils.DATABASE_TABLE,
                null,
                NoteDBUtils._PUNCHCARD_ID+"=?",
                new String[]{punchcardId},
                null,
                null,
                NoteDBUtils._ID+" desc");
        if (cursor!=null){
            while (cursor.moveToNext()){
                Note info = new Note();

                String id = String.valueOf(cursor.getInt(cursor.getColumnIndex(NoteDBUtils._ID)));
                String content = cursor.getString(cursor.getColumnIndex(NoteDBUtils._CONTENT));
                String time = cursor.getString(cursor.getColumnIndex(NoteDBUtils._TIME));

                info.setId(id);
                info.setPunchcard_id(punchcardId);
                info.setTime(time);
                info.setContent(content);

                list.add(info);
            }
            cursor.close();
        }
        Log.d(TAG,"list  ====== "+list);
        return list;
    }
}
