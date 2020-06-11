package com.example.punchcard;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import com.example.punchcard.adapter.PunchCardAdapter;
import com.example.punchcard.bean.Energy;
import com.example.punchcard.bean.PunchCard;
import com.example.punchcard.database.EnergyStatisticsHelper;
import com.example.punchcard.database.NoteSQLiteHelper;
import com.example.punchcard.database.SQLiteHelper;
import com.example.punchcard.database.StatisticsDbHelper;
import com.example.punchcard.utils.DBUtils;

import java.util.List;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity" ;
    ListView listView;
    List<PunchCard> list;
    Context context = this;
    SQLiteHelper mSQLiteHelper;
    PunchCardAdapter adapter;
    NoteSQLiteHelper mNoteSQLiteHelper;
    StatisticsDbHelper mStatisticsDbHelper;
    ImageView add,homeImage, leavesImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        updateEnergyDatabase();

        //用于显示便签的列表
        listView = (ListView) findViewById(R.id.listview);
        add = (ImageView) findViewById(R.id.add);
        homeImage = (ImageView)findViewById(R.id.bt_home);
        leavesImage = (ImageView)findViewById(R.id.bt_leaves);

        //创建数据库
        mSQLiteHelper = new SQLiteHelper(this);
        mNoteSQLiteHelper = new NoteSQLiteHelper(this);
        mStatisticsDbHelper = new StatisticsDbHelper(this);

        initData();
        clickMethod();
    }

    private void clickMethod() {
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,
                        NewrecordActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        leavesImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,TreeActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
    }

    protected void initData() {
        showQueryData();
        //点击每条item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,View view,int position,long id){
                PunchCard punchCard = list.get(position);
                Intent intent = new Intent(MainActivity.this, RecordActivity.class);
                intent.putExtra("id", punchCard.getId());
                intent.putExtra("item", punchCard.getItem());
                intent.putExtra("times", punchCard.getTimes());
                intent.putExtra("activity", punchCard.getActivity());
                MainActivity.this.startActivityForResult(intent, 1);
            }
        });

        //长按每条item删除
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int
                    position, long id) {
                AlertDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder( MainActivity.this)
                        .setMessage("是否删除这条习惯？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PunchCard notepadBean = list.get(position);
                                Log.d(TAG,"Id=========="+notepadBean.getId());
                                Log.d(TAG,"item=========="+notepadBean.getItem());
                                Log.d(TAG,"syear========="+notepadBean.getSyear());
                                Long eventId = notepadBean.getEventId();
                                Log.d(TAG,"eventId=========="+eventId);
                                ContentResolver cr = getContentResolver();
                                Uri deleteUri = ContentUris.withAppendedId(Uri.parse("content://" + "com.android.calendar/" + "events"), notepadBean.getEventId());
                                int rows = cr.delete(deleteUri, null, null);

                                if(mSQLiteHelper.deleteData(notepadBean.getId())){
                                    list.remove(position);
                                    adapter.notifyDataSetChanged();
                                    if(rows == -1) {
                                        Toast.makeText(MainActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                                    }else {
                                        Log.d(TAG,"============rows = "+ rows);
                                        Toast.makeText(MainActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                                    }
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
    public void showQueryData(){
        if (list!=null){
            list.clear();
        }
        //从数据库中查询出所有的数据
        list = mSQLiteHelper.query();
        adapter = new PunchCardAdapter(this, list);
        listView.setAdapter(adapter);

        homeImage.setImageDrawable(getResources().getDrawable(R.drawable.home));
        leavesImage.setImageDrawable(getResources().getDrawable(R.drawable.leaves_no));
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1&&resultCode==2){
            showQueryData();
            onResume();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        onCreate(null);
    }
    //每次进入这个界面刷新能量数据库
    private void updateEnergyDatabase(){
        EnergyStatisticsHelper helper = new EnergyStatisticsHelper(context);
        int databaseColumn = helper.judge();
        String x = DBUtils.getTime();
        if(databaseColumn == 0){
            Energy energy =new Energy(0,x,1,0);
            helper.GatherEnergy(energy);
            helper.close();
        }
        else{
            if(!helper.getEnergy(1).getDate().equals(DBUtils.getTime()) ){
                int energyNumber = helper.getEnergy(1).getEnergy();
                Energy energy = new Energy(energyNumber,x,1,0);
                energy.setId(1);
                helper.updateData(energy);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 当按下返回键时所执行的命令
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 此处写你按返回键之后要执行的事件的逻辑
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            return super.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);

    }
}

