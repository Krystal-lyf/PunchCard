package com.example.punchcard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.punchcard.bean.Energy;
import com.example.punchcard.database.EnergyStatisticsHelper;
import com.example.punchcard.utils.DBUtils;

public class TreeActivity extends AppCompatActivity {

    private static final String TAG = "TreeActivity";
    private int energyNumber = 0;
    private Context context = this;
    private String tag;
    private ImageView iv_energy;
    private ImageView iv_login_ball;
    private ImageView iv_punch_ball;
    private ImageView iv_home_chose;
    private ImageView iv_cloud_one;
    private ImageView iv_cloud_two;
    private TextView tv_energy_number;
    private ImageView iv_leaves_chose;
    private ImageView iml_tree;
    private ImageView imll_tree;
    private ImageView imlll_tree;
    private ImageView imllll_tree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tree);
        initView();
        judeEnergy();
        setImgTree();
        setTotalNumber();
        translate_energy();
        translate_cloud();
        initClick();
    }
    private void initView(){
        iml_tree = this.findViewById(R.id.iml_tree);
        imll_tree = this.findViewById(R.id.imll_tree);
        imlll_tree = this.findViewById(R.id.imlll_tree);
        imllll_tree = this.findViewById(R.id.imllll_tree);
        iv_login_ball = this.findViewById(R.id.iv_apple);
        iv_punch_ball = this.findViewById(R.id.iv_apple1);
        iv_home_chose = this.findViewById(R.id.bt_home);
        iv_cloud_one = this.findViewById(R.id.iv_cloud_one);
        iv_cloud_two = this.findViewById(R.id.iv_cloud_two);
        tv_energy_number = this.findViewById(R.id.tv_energy_number);
        iv_leaves_chose = this.findViewById(R.id.bt_leaves);

        iv_home_chose.setImageDrawable(getResources().getDrawable(R.drawable.home_no));
        iv_leaves_chose.setImageDrawable(getResources().getDrawable(R.drawable.leaves));
    }
    //能量的判断，能量球的显示
    private void judeEnergy(){
        EnergyStatisticsHelper op = new EnergyStatisticsHelper(context);
        int number = op.judge();
        Log.d(TAG,"++++++"+number);
        if(number!=0){
            if(op.getEnergy(1).getLogin() == 1){
                iv_login_ball.setBackgroundResource(R.drawable.energy);
            }
            if(op.getEnergy(1).getPunchCard() == 1){
                iv_punch_ball.setBackgroundResource(R.drawable.energy);
            }
            energyNumber = op.getEnergy(1).getEnergy();
        }
    }

    //显示总能量数
    private void setTotalNumber(){
        tv_energy_number.setText(energyNumber/2+"g ");
    }


    //树苗的更换
    private void setImgTree(){
        if(energyNumber>=0&&energyNumber<100){
            iml_tree.setBackgroundResource(R.drawable.m_tree);
        }
        else if(energyNumber >= 100 && energyNumber < 140){

            imll_tree.setBackgroundResource(R.drawable.l_tree);
        }
        else if(energyNumber >= 140&& energyNumber<200 ){
            imlll_tree.setBackgroundResource(R.drawable.xl_tree);
        }
        else if(energyNumber >=200){
            imllll_tree.setBackgroundResource(R.drawable.xl_tree);
        }
        Log.d(TAG,"___+++____+++energyNumber"+ energyNumber);
    }

    //能量球的移动效果
    private void translate_energy(){
        Animation translate = AnimationUtils.loadAnimation(this,R.anim.translate_animation);
        iv_login_ball.startAnimation(translate);
        iv_punch_ball.startAnimation(translate);
    }

    //云的移动效果实现
    private void translate_cloud(){
        Animation translate_cloud_one = AnimationUtils.loadAnimation(this,R.anim.cloud_one_translate);
        Animation translate_cloud_two = AnimationUtils.loadAnimation(this,R.anim.cloud_two_translate);
        iv_cloud_one.startAnimation(translate_cloud_one);
        iv_cloud_two.startAnimation(translate_cloud_two);

    }

    //能量球点击后的动画效果
    private void translate_login_gather(){
        Animation translate_login_ball = AnimationUtils.loadAnimation(this,R.anim.gather_login_ball);
        iv_login_ball.startAnimation(translate_login_ball);
        Handler handler = new Handler();
        //延时执行页面转换
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                iv_login_ball.setBackgroundResource(R.drawable.no);
                gather_energy_login();
                judeEnergy();
                setImgTree();
                setTotalNumber();
            }
        }, 2000);
    }
    private void translate_punch_gather (){
        Animation translate_punch_ball = AnimationUtils.loadAnimation(this,R.anim.gather_punch_ball);
        iv_punch_ball.startAnimation(translate_punch_ball);
        //延时执行页面转换
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                iv_punch_ball.setBackgroundResource(R.drawable.no);
                gather_energy_punch();
                judeEnergy();
                setImgTree();
                setTotalNumber();
            }
        }, 2000);

    }

    //登录能量球点击之后的数据变化
    private void gather_energy_login(){
        EnergyStatisticsHelper helper = new EnergyStatisticsHelper(context);
        int oldEnergyNumber = helper.getEnergy(1).getEnergy();
        int newEnergyNumber = oldEnergyNumber + 2;
        Energy newEnergy = new Energy(newEnergyNumber, helper.getEnergy(1).getDate(),0,helper.getEnergy(1).getPunchCard());
        newEnergy.setId(1);
        helper.updateData(newEnergy);
        helper.close();
    }
    //打卡能量球点击之后的数据变化
    private void gather_energy_punch(){
        EnergyStatisticsHelper helper = new EnergyStatisticsHelper(context);
        int oldEnergyNumber = helper.getEnergy(1).getEnergy();
        int newEnergyNumber = oldEnergyNumber + 2;
        Energy newEnergy = new Energy(newEnergyNumber, helper.getEnergy(1).getDate(),helper.getEnergy(1).getLogin(),2);
        newEnergy.setId(1);
        helper.updateData(newEnergy);
        helper.close();
    }

    //点击事件
    private void initClick(){
        //登录能量球的点击事件
        iv_login_ball.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                translate_login_gather();
            }
        });
        //打卡能两球的点击事件
        iv_punch_ball.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                translate_punch_gather();
            }
        });
        //home键点击之后的界面展示事件
        iv_home_chose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(TreeActivity.this,MainActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
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
