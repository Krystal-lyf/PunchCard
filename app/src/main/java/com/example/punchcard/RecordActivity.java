package com.example.punchcard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.punchcard.adapter.NoteAdapter;
import com.example.punchcard.adapter.PunchCardAdapter;
import com.example.punchcard.bean.Energy;
import com.example.punchcard.bean.Note;
import com.example.punchcard.bean.PunchCard;
import com.example.punchcard.bean.PunchStatistics;
import com.example.punchcard.database.EnergyStatisticsHelper;
import com.example.punchcard.database.NoteSQLiteHelper;
import com.example.punchcard.database.SQLiteHelper;
import com.example.punchcard.database.StatisticsDbHelper;
import com.example.punchcard.utils.DBUtils;
import com.example.punchcard.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 打卡界面的activity
 */
public class RecordActivity extends AppCompatActivity {

    private static final String TAG = "RecordActivity";
    private SQLiteHelper mSQLiteHelper;
    private NoteSQLiteHelper mNoteSQLiteHelper;
   // private Button dakaButton;
    NoteAdapter noteAdapter;
    Intent intent; //intent:Mainactivity界面传递过来的
    Integer activity, times;
    String id, item;
    ListView mListView;
    List<Note> list;
    NoteAdapter adapter;
    private ImageView dakaImage;
    private Context context = this;
    private int year;
    private int month;
    private int day;
    private List<PunchStatistics> statistics;
    ImageView clockImage, calenderImage, notesImage,iv_back1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        clockImage = (ImageView)findViewById(R.id.bt_clock);
        mListView = (ListView) findViewById(R.id.noteslistview);
        dakaImage = (ImageView)findViewById(R.id.dakaImage) ;
        calenderImage = (ImageView)findViewById(R.id.bt_calender);
        notesImage = (ImageView)findViewById(R.id.bt_notes);
        iv_back1 = (ImageView)findViewById(R.id.iv_back1) ;
        year = DateUtils.getYear();
        month = DateUtils.getMonth();
        day = DateUtils.getCurrentDayOfMonth();
        //获取数据
        initData();
        setActivityImag();
        clickMethod();

    }

    private void clickMethod() {
        dakaImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity == 0) {
                    activity = 1;
                    times++;
                    GenerateStatistic();
                    punch_click();
                    //修改成功
                    if (mSQLiteHelper.updateData(id, times, activity, DBUtils.getTime())) {
                        dakaImage.setImageDrawable(getResources().getDrawable(R.drawable.daka1));
                        setResult(2);
                        //打卡完跳到记录笔记界面
                        Intent intent3 = new Intent(RecordActivity.this, NewnotesActivity.class);
                        intent3.putExtra("_id", id);
                        RecordActivity.this.startActivityForResult(intent3, 1);

                    }else{
                        showToast("打卡失败");
                    }
                }else{//打卡按钮没有被点击过
                    showToast("今日已打卡，不能重复打卡！");
                }
            }
        });
        calenderImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipCalendar();
            }
        });
        notesImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //跳转到newRecordActivity界面，同时将id传过去
                Intent intent1 = new Intent(RecordActivity.this,
                        NewnotesActivity.class);
                intent1.putExtra("_id", id);
                RecordActivity.this.startActivityForResult(intent1, 1);
            }
        });
        clockImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {//跳转到clockactivity界面
                Intent intent2 = new Intent(RecordActivity.this,
                        ClockActivity.class);
                PunchCard info = mSQLiteHelper.queryById(id);
                intent2.putExtra("id",info.getId());
                intent2.putExtra("item",info.getItem());
                intent2.putExtra("calID", info.getCalID());
                intent2.putExtra("syear",info.getSyear());
                intent2.putExtra("smonth",info.getSmonth());
                intent2.putExtra("sday",info.getSday());
                intent2.putExtra("shour",info.getShour());
                intent2.putExtra("sminute",info.getSminute());
                intent2.putExtra("eventid",info.getEventId());
                intent2.putExtra("sts",info.getTs());
                RecordActivity.this.startActivityForResult(intent2, 1);
            }
        });
        iv_back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void GenerateStatistic() {
        PunchStatistics newStatistic = new PunchStatistics(Integer.parseInt(id),year,month,day);
        StatisticsDbHelper DbHelper = new StatisticsDbHelper(context);
        DbHelper.PunchCard(newStatistic);
        DbHelper.close();
    }

    private void skipCalendar() {
        PunchCard info = mSQLiteHelper.queryById(id);
        Intent intent2 = new Intent(RecordActivity.this,CalendarActivity.class);
        //intent2.setClass(this,CalendarActivity.class);
        intent2.putExtra("id",id);
        intent2.putExtra("times",info.getTimes());
        RecordActivity.this.startActivityForResult(intent2, 1);

        //测试
        statistics = new ArrayList<>();
        StatisticsDbHelper op = new StatisticsDbHelper(context);
        statistics = op.GetStatistics(Integer.parseInt(id),year,month);
        Log.d(TAG,"+++++"+statistics.size()+item);
        if(statistics.size()>0){
            for(PunchStatistics statistic : statistics){
                Log.d(TAG,"++++++++"+statistic.getDay()+id);
            }
        }
    }

    private void setActivityImag() {
        //先根据activity判断是否已经打卡
        if (activity == 0) {
            dakaImage.setImageDrawable(getResources().getDrawable(R.drawable.daka));
        } else {
            dakaImage.setImageDrawable(getResources().getDrawable(R.drawable.daka1));
        }
    }

    public void showToast(String message) {
        Toast.makeText(RecordActivity.this, message, Toast.LENGTH_SHORT).show();
    }


    private void initData() {

        mSQLiteHelper = new SQLiteHelper(this);
        mNoteSQLiteHelper = new NoteSQLiteHelper(this);
        intent = getIntent();

        activity = intent.getIntExtra("activity", 0);
        times = intent.getIntExtra("times", 0);
        id = intent.getStringExtra("id");
        item = intent.getStringExtra("item");

        showQueryData();

        //长按每条item删除
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int
                    position, long id) {
                AlertDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder( RecordActivity.this)
                        .setMessage("是否删除这条心情？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Note noteBean = list.get(position);
                                if(mNoteSQLiteHelper.deleteData(noteBean.getId())){
                                    list.remove(position);
                                    if(adapter != null) {
                                        adapter.notifyDataSetChanged();
                                    }
                                    Toast.makeText(RecordActivity.this,"删除成功",
                                            Toast.LENGTH_SHORT).show();
                                    showQueryData();
                                }
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                dialog = builder.create();
                dialog.show();
                return true;
            }
        });

    }


    private void showQueryData() {
        if (list != null) {
            list.clear();
        }
        //从数据库中查询出所有的数据
         list = mNoteSQLiteHelper.query(id);
         noteAdapter = new NoteAdapter(this, list);
         mListView.setAdapter(noteAdapter);
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1&&resultCode==2){
            showQueryData();
        }
    }

    //打卡成功后的事件处理
    private void punch_click(){
        EnergyStatisticsHelper op = new EnergyStatisticsHelper(context);
        if(op.getEnergy(1).getPunchCard() == 0){
            Energy newEnergy = new Energy(op.getEnergy(1).getEnergy(),op.getEnergy(1).getDate(),op.getEnergy(1).getLogin(),1);
            newEnergy.setId(1);
            op.updateData(newEnergy);
        }

    }
}
