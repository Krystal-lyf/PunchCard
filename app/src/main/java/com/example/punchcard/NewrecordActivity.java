package com.example.punchcard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.punchcard.adapter.IconAdapter;
import com.example.punchcard.bean.Icon;
import com.example.punchcard.database.SQLiteHelper;
import com.example.punchcard.utils.DBUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;

/**
 * 新建一个打卡项目的activity
 */
public class NewrecordActivity extends Activity {

    private static final String TAG = "NewrecordActivity" ;
    EditText editText;
    Button saveButton;
    SQLiteHelper mSQLiteHelper;
    GridView mgridView;
    List<Icon> list;
    IconAdapter adapter;
    ImageView saveImage,iv_back4;
    Integer saveId;//记录最后保存的图标的id

    //每张图片的id
    int image_ids[] = {R.drawable.apple, R.drawable.book, R.drawable.house,
            R.drawable.book1, R.drawable.leaf, R.drawable.love, R.drawable.pear,
            R.drawable.sun, R.drawable.baobiao , R.drawable.flower, R.drawable.plants,
            R.drawable.share, R.drawable.tree, R.drawable.statisticanalysis,
            R.drawable.strabrray, R.drawable.amazed, R.drawable.smallhouse, R.drawable.icon_learning,
            R.drawable.fish, R.drawable.icon_bullseye, R.drawable.live, R.drawable.umbella,
            R.drawable.wink, R.drawable.send};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newrecord);

        initdata();
        clickMethod();

    }

    private void clickMethod() {
        //点击每个图标后
        mgridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG,"===== 点击了" + list.get(position).getName());

                saveImage.setImageDrawable(getResources().getDrawable(list.get(position).getId()));
                saveId = list.get(position).getId();
            }
        });

        //点击保存按钮
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editText.getText().toString().equalsIgnoreCase("")){
                    String item = editText.getText().toString().trim();
                    Log.d(TAG, "item == " + item);
                    if(saveId == null)
                        saveId = image_ids[0];
                    if (mSQLiteHelper.insertData(item, DBUtils.getTime(),saveId) ){
                        showToast("保存成功");
                        Log.d(TAG, "=====保存成功");
                        setResult(2);
                        finish();
                    }else{
                        showToast("保存失败");
                        Log.d(TAG, "=====保存失败");
                    }
                }else{
                    showToast("输入为空");
                }
            }
        });
        iv_back4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initdata() {
        mSQLiteHelper = new SQLiteHelper(this);

        editText = (EditText)findViewById(R.id.item);
        editText.clearFocus();
        saveButton = (Button)findViewById(R.id.saveButton);
        mgridView = (GridView)findViewById(R.id.gv);
        saveImage = (ImageView)findViewById(R.id.saveImage);
        saveImage.setImageDrawable(getResources().getDrawable(image_ids[0]));
        iv_back4 = (ImageView)findViewById(R.id.iv_back4);

        list = new ArrayList<Icon>();
        for(int i = 0; i<image_ids.length; i++){
            list.add(new Icon(image_ids[i], "图片" + i));
        }

        adapter = new IconAdapter(this, list);
        mgridView.setAdapter(adapter);
    }

    public void showToast(String message){
        Toast.makeText(NewrecordActivity.this,message,Toast.LENGTH_SHORT).show();
    }
}
