package com.example.punchcard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.punchcard.adapter.CalendarAdapter;
import com.example.punchcard.database.StatisticsDbHelper;
import com.example.punchcard.utils.DateUtils;

public class CalendarActivity extends AppCompatActivity {

    private RecyclerView list;
    private int year;
    private int month;
    private int[][] days;
    private CalendarAdapter adapter;
    private TextView tv_title;
    private String id;
    private String TAg = "CalendarActivity";
    private int StatisticYear;
    private int StatisticMonth;
    private int StatisticDay;
    private int day;
    private ImageView ib_left;
    private ImageView ib_right;
    private ImageView iv_back3;
    private Integer times;
    private TextView tv_times;
    private TextView tv_mouthTimes;
    private StatisticsDbHelper statisticsDbHelper;
    private int id1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        initView();   //初始化变量
        initClickListener();  //点击事件的实现
        getData();        //得到别的界面传入的数据
        GridShow();      //初始化适配器
        Log.d(TAg,"+++++"+id);
    }

    private void GridShow(){

        tv_title.setText(String.valueOf(StatisticYear) + "年" +String.valueOf(StatisticMonth)+"月");
        if(id != null) {
            id1 = Integer.valueOf(id);
        }else{
            id = "0";
        }
        tv_mouthTimes.setText(statisticsDbHelper.queryByMounth(id1, DateUtils.getMonth())+"");
        tv_times.setText(times+"");
        //创建布局管理器
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,7);

        list.setLayoutManager(gridLayoutManager);
        int itemId = Integer.parseInt(id);
        days = DateUtils.getDayOfMonthFormat(StatisticYear,StatisticMonth);
        //生成适配器
        adapter = new CalendarAdapter(this,days,itemId,StatisticYear,StatisticMonth);

        list.setAdapter(adapter);
    }
    //从别的Activity得到的数据
    private void getData(){
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        times = intent.getIntExtra("times", 0);
    }
    /*
     *得到xml文件的id，并生成相应的成员变量
     *
     */
    private void initView(){
        year = DateUtils.getYear();
        month = DateUtils.getMonth();
        StatisticYear = year;
        StatisticMonth = month;
        StatisticDay = day;
        day = DateUtils.getCurrentDayOfMonth();
        tv_title = this.findViewById(R.id.tv_calendar_title);
        tv_times = this.findViewById(R.id.tv_times);
        tv_mouthTimes = this.findViewById(R.id.tv_mouthTimes);
        list = this.findViewById(R.id.rv_list);
        ib_left = this.findViewById(R.id.ib_left);
        ib_right = this.findViewById(R.id.ib_right);
        iv_back3 = this.findViewById(R.id.iv_back3);
        statisticsDbHelper = new StatisticsDbHelper(this);


    }
    /*
     *点击事件
     */
    private void initClickListener(){
        ib_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leftSkip();
                GridShow();
                Log.d(TAg,"+++++++实现了本功能" + StatisticMonth);
            }
        });

        ib_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rightSkip();
                GridShow();
            }
        });

        iv_back3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /*
     *向左按钮的点击事件的具体实现
     */
    private void leftSkip(){
        if(StatisticMonth != 1){
            StatisticMonth = StatisticMonth - 1;
        }
        else{
            StatisticYear = StatisticYear - 1;
            StatisticMonth = 12;
        }
    }

    /*
     * 向右按钮的点击事件的具体实现
     */
    private void rightSkip(){
        if(StatisticMonth == month&&StatisticYear == year){

        }
        else if(StatisticMonth!=12){
            StatisticMonth = StatisticMonth + 1;
        }
        else if(StatisticMonth ==12 && StatisticMonth!=month){
            StatisticMonth = 1;
            StatisticYear = StatisticYear +1;
        }
    }
}
