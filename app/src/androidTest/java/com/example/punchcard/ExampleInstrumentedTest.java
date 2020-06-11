package com.example.punchcard;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.util.Log;

import com.example.punchcard.bean.PunchCard;
import com.example.punchcard.database.NoteSQLiteHelper;

import com.example.punchcard.database.SQLiteHelper;
import com.example.punchcard.database.StatisticsDbHelper;
import com.example.punchcard.utils.DBUtils;
import com.example.punchcard.utils.NoteDBUtils;

import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private static final String TAG =  "ExampleInstrumentedTest";
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.punchcard", appContext.getPackageName());
    }

    @Test
    public void testCreateDatabase(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        SQLiteHelper mSQLiteHelper = new SQLiteHelper(appContext);
    }

  //  @Test
 /*   public void testInserData(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        SQLiteHelper mSQLiteHelper = new SQLiteHelper(appContext);
        mSQLiteHelper.insertData("早起", DBUtils.getTime());
    }*/

    @Test
    public void testQuery(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        SQLiteHelper mSQLiteHelper = new SQLiteHelper(appContext);
        List<PunchCard> mList = mSQLiteHelper.query();
        Log.d(TAG,"list = " + mList.get(1) );
    }

    @Test
    public void testDelete(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        SQLiteHelper mSQLiteHelper = new SQLiteHelper(appContext);
        Boolean bo = mSQLiteHelper.deleteData("2");
        Log.d(TAG,"bo == " + bo);
    }

   /*
   @Test
    public void testShowQueryData(){
        MainActivity mainActivity = new MainActivity();
        List<PunchCard> list = mainActivity.showQueryData();
        Log.d(TAG , "list == " + list);
    }*/

   @Test
   public void testUpdateData(){
       Context appContext = InstrumentationRegistry.getTargetContext();
       SQLiteHelper mSQLiteHelper = new SQLiteHelper(appContext);
       //Boolean y = mSQLiteHelper.updateData("3",0,0);
      // Log.d(TAG, "y == "+y);
   }

   @Test
   public void testGetTime(){
      Log.d(TAG,"=====" + NoteDBUtils.getTime());

   }

   @Test
   public void testOncreat(){
       Context appContext = InstrumentationRegistry.getTargetContext();
       NoteSQLiteHelper mNotesSQLiteHelper = new NoteSQLiteHelper(appContext);

   }

   @Test
    public void testNoteInsert(){
       Context appContext = InstrumentationRegistry.getTargetContext();
       NoteSQLiteHelper mNotesSQLiteHelper = new NoteSQLiteHelper(appContext);
       Boolean a = mNotesSQLiteHelper.insertData("1", "今天背了5个", NoteDBUtils.getTime());
       Log.d(TAG,"a======= " +a);
   }

    @Test
    public void testNoteQuery(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        NoteSQLiteHelper mNotesSQLiteHelper = new NoteSQLiteHelper(appContext);
        mNotesSQLiteHelper.query("2");
    }

    @Test
    public void testDeleteNote(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        NoteSQLiteHelper mNotesSQLiteHelper = new NoteSQLiteHelper(appContext);
        mNotesSQLiteHelper.deleteData("1");
    }

    @Test
    public void testSetPicture(){
        int id = R.drawable.apple;
        Log.d(TAG,"appleid    == " +id);
    }

    @Test
    public void testQueryByItemID(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        StatisticsDbHelper StatisticDatabase = new StatisticsDbHelper(appContext);
        int times =StatisticDatabase.queryByMounth(1,6);
        Log.d(TAG,"========="+times);
    }
}
