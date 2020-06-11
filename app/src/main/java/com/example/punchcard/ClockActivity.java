package com.example.punchcard;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import com.example.punchcard.database.SQLiteHelper;
import com.example.punchcard.utils.DBUtils;
import com.example.punchcard.utils.DateUtils;

import android.provider.CalendarContract.Events;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.time.Year;
import java.util.Calendar;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
@TargetApi(Build.VERSION_CODES.M)

public class ClockActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ColockdActivity";
    public static final int PERMISSION_RESULT_CODE = 1;
    private EditText mTs;
    private Button mDelet;
    ImageView mSive,iv_back2;
    TextView left_days,xian,clock_name,cloName;
    LinearLayout ll_clock;
    private SQLiteHelper mSQLiteHelper;
    Integer calID, syear, smonth, sday, shour, sminute, sts,dyear, dmonth, dday, dhour,dTs, dminute, amonth;
    long eventId;
    String item, id, chdate;
    Intent intent;
    Context context;
    long cha0,cha;
    Date todaydate, stardate;

    //滑动数字
    private NumberPicker np1,np2;
    //定义上下限具体值
    private int h = 0,m = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);
        mTs = (EditText) findViewById(R.id.Ts);
        mSive = (ImageView) findViewById(R.id.Save);
        mDelet = (Button) findViewById(R.id.Deletclock);
        left_days = (TextView)findViewById(R.id.left_days);
        ll_clock = (LinearLayout)findViewById(R.id.ll_clock);
        xian = (TextView)findViewById(R.id.xian);
        clock_name = (TextView)findViewById(R.id.clock_name);
        cloName = (TextView)findViewById(R.id.cloName);
        iv_back2 = (ImageView)findViewById(R.id.iv_back2);
        getDate();         //获取该项目数据库数据
        showtime();      //将已经设置的时间显示在页面
        checkCalendarPermission();    //检查权限
        //querycalendar();            //获取手机日历信息
        chdate = syear + "-" + smonth + "-" + sday;
        if(calID == 1){
            todaydate = getTime();
            stardate = changetime(chdate);
            cha0 = getTimeDistance(stardate,todaydate);
            Log.d(TAG, "间隔======" + cha0);
            cha = sts - cha0;
            left_days.setText(cha-1+"");
            //Log.d(TAG, "间隔======" + cha);
        }
        mSive.setOnClickListener(this);   //保存/修改按钮
        mDelet.setOnClickListener(this);  //删除按钮
        iv_back2.setOnClickListener(this);


        np1 = (NumberPicker) findViewById(R.id.np1);
        //设置np1的最大值只和最小值
        np1.setMinValue(0);
        np1.setMaxValue(23);
        //设置当前值
        np1.setValue(shour);
        np1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                h = newVal;

            }
        });
        np2 = (NumberPicker) findViewById(R.id.np2);
        //设置np1的最大值只和最小值
        np2.setMinValue(0);
        np2.setMaxValue(59);
        //设置当前值
        np2.setValue(sminute);
        np2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                m = newVal;

            }
        });
    }



    private void showtime() {
        //显示闹钟名称
        cloName.setText(item);
        if (calID == 1) {
            //修改事件
            clock_name.setText("修改习惯提醒");
            mTs.setText(sts+"");
        } else {
            //新建事件,不显示删除按钮和显示还剩提醒多少天
            mTs.setText("1");
            clock_name.setText("新建习惯提醒");
            mDelet.setVisibility(View.INVISIBLE);
            ll_clock.setVisibility(View.INVISIBLE);
            xian.setVisibility(View.INVISIBLE);
        }

    }

    @RequiresApi
    private void checkCalendarPermission() {
        Log.d(TAG, "准备申请权限=============================================");
        int writeResult = checkSelfPermission(Manifest.permission.WRITE_CALENDAR);
        int readResult = checkSelfPermission(Manifest.permission.READ_CALENDAR);
        if (writeResult != PackageManager.PERMISSION_GRANTED || readResult != PackageManager.PERMISSION_GRANTED) {
            //没有权限,需要申请权限

            //请求权限
            requestPermissions(new String[]{Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR}, PERMISSION_RESULT_CODE);
        } else {
            //有权限

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //权限请求结果
        if (requestCode == PERMISSION_RESULT_CODE) {
            // grantResults 结果
            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //有权限，不用做操作
                querycalendar();
            } else {
                // 无权限，结束程序
                finish();
            }
        }
    }

    private void querycalendar() {
        ContentResolver contentResolver = getContentResolver();
        Uri uri = Uri.parse("content://" + "com.android.calendar/" + "calendars");
        //Uri uri = CalendarContract.Events.CONTENT_URI;
        Log.d(TAG, "准备读取=============================================");
        Cursor query = contentResolver.query(uri, null, null, null, null);
        String[] columnNames = query.getColumnNames();
        while (query.moveToNext()) {
            Log.d(TAG, "=============================================");
            for (String columnName : columnNames) {
                Log.d(TAG, "field --- > " + columnName + "value -- > " + query.getString(query.getColumnIndex(columnName)));
            }
            Log.d(TAG, "=============================================");
        }
        query.close();
    }

    //向日历中插入新的事件
    public void addcalenderEvent() {
        Log.d(TAG,"准备查询数据=============================================");
        //获取用户填写的时间
        dyear = DateUtils.getYear();
        amonth = DateUtils.getMonth();
        dday = DateUtils.getCurrentDayOfMonth();
        dTs = Integer.parseInt(mTs.getText().toString());
        dhour = h;
        dminute = m;
        dmonth = amonth-1;    //月份减一
        Log.d(TAG, "日历id=============================================");
        calID = 1;     //手机日历id
        //时间创建
        Log.d(TAG, "开始时间=============================================");
        Calendar beginTime = Calendar.getInstance();

        beginTime.set(dyear, dmonth, dday, dhour, dminute);
        long startMillis = beginTime.getTimeInMillis();
        Log.d(TAG, "beginTime -- > " + startMillis);
        //准备好插入事件数据库的内容
        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        //开始时间
        values.put(Events.DTSTART, startMillis);
        //重复20次
        values.put(Events.RRULE, "FREQ=DAILY;COUNT=" + dTs);
        //持续一天
        values.put(Events.DURATION, "PT1H");
        //标题为item
        values.put(Events.TITLE, item);
        //日历ID
        values.put(Events.CALENDAR_ID, calID);
        //设置有提醒
        values.put(Events.HAS_ALARM, 10);
        //时间时区
        String timeZone = TimeZone.getDefault().getID();
        Log.d(TAG, "time zone -- > " + timeZone);
        values.put(Events.EVENT_TIMEZONE, timeZone);
        //事件uri
        Uri uri = Uri.parse("content://" + "com.android.calendar/" + "events");
        Uri resulturi = cr.insert(uri, values);//插入
        Log.d(TAG, "insert result --- > " + resulturi);
        eventId = ContentUris.parseId(resulturi);    //获取事件id
        Log.d(TAG, "eventid --- > " + eventId);
        if (eventId == 0) {
            return;
        } else {
            //成功插入后设置提醒
            ContentResolver cr2 = getContentResolver();
            ContentValues values2 = new ContentValues();
            //进行提醒
            values2.put(CalendarContract.Reminders.MINUTES, 0);
            values2.put(CalendarContract.Reminders.EVENT_ID, eventId);
            values2.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
            Uri uri2 = Uri.parse("content://com.android.calendar/reminders");
            Uri clockuri = cr2.insert(uri2, values2);
            Log.d(TAG, "clock uri -- > " + clockuri);
        }
    }

    //事件已经存在时进行更新
    public void updatecalendErevent() {
        dyear = DateUtils.getYear();
        amonth = DateUtils.getMonth();
        dday = DateUtils.getCurrentDayOfMonth();
        dTs = Integer.parseInt(mTs.getText().toString());
        dhour = h;
        //dminute = Integer.parseInt(mMinute.getText().toString());
        dminute = m;
        dmonth = amonth-1;

        Calendar beginTime = Calendar.getInstance();
        beginTime.set(dyear, dmonth, dday, dhour, dminute);
        long startMillis = beginTime.getTimeInMillis();
        Log.d(TAG, "beginTime -- > " + startMillis);
        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        //开始时间
        values.put(Events.DTSTART, startMillis);
        //重复
        values.put(Events.RRULE, "FREQ=DAILY;COUNT=" + dTs);
        //持续
        values.put(Events.DURATION, "PT1H");
        //标题
        values.put(Events.TITLE, item);
        //日历ID
        values.put(Events.CALENDAR_ID, calID);
        //设置有闹钟提醒
        values.put(Events.HAS_ALARM, 0);
        //时间时区
        String timeZone = TimeZone.getDefault().getID();
        Log.d(TAG, "time zone -- > " + timeZone);
        values.put(Events.EVENT_TIMEZONE, timeZone);

        //更新uri
        Uri updateUri = ContentUris.withAppendedId(Uri.parse("content://" + "com.android.calendar/" + "events"), eventId);
        Log.d(TAG,"修改日历=============================================");
        //更新
        int ups = cr.update(updateUri, values,null, null);
        Log.d(TAG, "eventid --- > " + eventId);
        if (ups == -1) {
            Log.d(TAG,"修改日历失败=============================================");
            Toast.makeText(ClockActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
            return;
        } else {
            //成功后修改提醒
            Log.d(TAG,"修改提醒=============================================");
            ContentResolver cr2 = getContentResolver();
            ContentValues values2 = new ContentValues();
            //进行提醒
            values2.put(CalendarContract.Reminders.MINUTES, 0);
            values2.put(CalendarContract.Reminders.EVENT_ID, eventId);
            values2.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
            //提醒uri
            Uri uri2 = Uri.parse("content://com.android.calendar/reminders");
            Uri clockuri = cr2.insert(uri2, values2);
            Log.d(TAG, "clock uri -- > " + clockuri);
        }
    }

    //点击事件执行
    public void onClick(View v) {
        switch (v.getId()) {
            //保存/修改
            case R.id.Save:
                Log.d(TAG, "点击按钮，插入数据=============================================");
                if (calID == 1) {
                    //修改事件
                    Log.d(TAG, "点击按钮，修改数据=============================================");
                    updatecalendErevent();
                    mSQLiteHelper.updateClockData(id, calID, dyear, amonth, dday, dhour, dminute, eventId,dTs);
                    todaydate = getTime();
                    stardate = changetime(chdate);
                    cha0 = getTimeDistance(stardate,todaydate);
                    Log.d(TAG, "间隔======" + cha0);
                    cha = dTs - cha0;
                    left_days.setText(cha+"");
                    Toast.makeText(ClockActivity.this, "习惯提醒已修改", Toast.LENGTH_SHORT).show();
                    finish();
                } else if(calID == 0){
                    //新事件
                    calID = 1;
                    //向日历插入时间
                    addcalenderEvent();
                    mSQLiteHelper.updateClockData(id, calID, dyear, amonth, dday, dhour, dminute, eventId, dTs);
                    todaydate = getTime();
                    stardate = changetime(chdate);
                    cha0 = getTimeDistance(stardate,todaydate);
                    Log.d(TAG, "间隔======" + cha0);
                    cha = dTs - cha0;
                    left_days.setText(cha+"");
                    Toast.makeText(ClockActivity.this, "习惯提醒已建立", Toast.LENGTH_SHORT).show();
                    finish();
                }
                //mSQLiteHelper.updateClockData(id,calID,dyear,dmonth,dday,dhour,dminute);
                break;
            case R.id.Deletclock:
                //删除手机日历事件
                ContentResolver cr = getContentResolver();
                Log.d(TAG,"evevtid===============" + eventId);
                Uri deleteUri = ContentUris.withAppendedId(Uri.parse("content://" + "com.android.calendar/" + "events"), eventId);
                Log.d(TAG,"删除日历=============================================");
                int rows = cr.delete(deleteUri, null, null);
                if(rows == -1){
                    Log.d(TAG,"删除日历失败=============================================");
                    Toast.makeText(ClockActivity.this, "删除习惯提醒失败", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    calID = 0;
                    //app数据库修改为初值
                    Log.d(TAG,"本地数据库删除=============================================");
                    mSQLiteHelper.updateClockData(id,calID,0,0,0,0,0,0,0);
                    left_days.setText("-");
                    Log.d(TAG,"calID=================" + calID);
                    finish();
                    Toast.makeText(ClockActivity.this, "删除习惯提醒成功", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_back2:
                finish();
                break;
        }
    }


    //获取前面传来的数据库数据
    public void getDate() {
        Log.d(TAG,"准备获取数据库=============================================");
        mSQLiteHelper = new SQLiteHelper(this);
        intent = getIntent();
        id = intent.getStringExtra("id");
        item  = intent.getStringExtra("item");
        calID = intent.getIntExtra("calID",0);
        syear = intent.getIntExtra("syear",0);
        smonth = intent.getIntExtra("smonth",0);
        sday = intent.getIntExtra("sday",0);
        shour = intent.getIntExtra("shour",0);
        sminute = intent.getIntExtra("sminute",0);
        eventId = intent.getLongExtra("eventid",0);
        sts = intent.getIntExtra("sts",0);
        Log.d(TAG,"传入syear=======" + syear);
        Log.d(TAG,"传入calid=======" + calID);
        Log.d(TAG,"传入eventid=======" + eventId);
    }


    public static int getTimeDistance(Date beginDate, Date endDate) {
        Calendar fromCalendar = Calendar.getInstance();
        return (int) ((endDate.getTime() - beginDate.getTime() ) / (1000 * 60 * 60 * 24));

    }

    public static Date getTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        Date date = new Date(System.currentTimeMillis());
        Log.d(TAG, "今天日期=======" + date);
        return date;
    }

    public static Date changetime (String date1){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Date date = null;
        try {
            date = format.parse(date1);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "开始日期=======" + date);
        return date;
    }

}
