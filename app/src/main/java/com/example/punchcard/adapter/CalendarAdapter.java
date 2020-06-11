package com.example.punchcard.adapter;




import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.punchcard.R;
import com.example.punchcard.bean.PunchStatistics;
import com.example.punchcard.database.StatisticsDbHelper;
import com.example.punchcard.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;


public class CalendarAdapter extends RecyclerView.Adapter {

    private static final String TAG = "CalendarAdapter";
    private int[] days = new int[42];
    private Context context;
    private int year;
    private int month;
    private int itemId;
    private  LinearLayout ll_calendar;
    private  TextView tv_calendar_number;
    private List<PunchStatistics> statistics;

    public CalendarAdapter(Context context, int[][] days,int itemId, int year, int month){
        this.context = context;
        int dayNum = 0;
        //将二维数组转化为一维数组，方便使用
        for (int i = 0; i < days.length; i++) {
            for (int j = 0; j < days[i].length; j++) {
                this.days[dayNum] = days[i][j];
                dayNum++;
            }
        }
        this.year = year;
        this.month = month;
        this.itemId = itemId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.calendar_layout,null);
//        Log.d(TAG,"+++++++,这里执行了吗InnerHolderAAAA");
        return new InnerHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

//        Log.d(TAG,"+++++++,这里执行了吗");
        //获得数组
        statistics = new ArrayList<>();
        StatisticsDbHelper op = new StatisticsDbHelper(context);
        statistics = op.GetStatistics(itemId,year,month);
        if(days[position]!=0&&days[position]!= DateUtils.getCurrentDayOfMonth()){
            String number = String.valueOf(days[position]);
            tv_calendar_number.setText(number);
            for(PunchStatistics statistic:statistics){
                if(statistic.getDay()==days[position]){
                    //ll_calendar.setBackgroundColor(Color.parseColor("#FF6600"));
                    ll_calendar.setBackgroundResource(R.drawable.round);
                }
            }
        }
        else if(days[position]!=0&&days[position]==DateUtils.getCurrentDayOfMonth()){
            String number = String.valueOf(days[position]);
            tv_calendar_number.setText(number);
            if(year == DateUtils.getYear()&&month==DateUtils.getMonth()){
                ll_calendar.setBackgroundResource(R.drawable.round1);
            }
            for(PunchStatistics statistic:statistics){
                if(statistic.getDay()==days[position]){
                    ll_calendar.setBackgroundResource(R.drawable.round);
                }
            }
        }
        else{
            tv_calendar_number.setText("");
        }

    }

    @Override
    public int getItemCount() {
        return days.length;
    }
    public class InnerHolder extends RecyclerView.ViewHolder{
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
//            Log.d(TAG,"+++++++,这里执行了吗InnerHolder");
            ll_calendar = itemView.findViewById(R.id.ll_calendar);
            tv_calendar_number = itemView.findViewById(R.id.tv_calendar_number);
        }
    }

}
