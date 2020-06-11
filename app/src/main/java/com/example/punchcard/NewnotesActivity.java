package com.example.punchcard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.punchcard.database.NoteSQLiteHelper;
import com.example.punchcard.utils.NoteDBUtils;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class NewnotesActivity extends Activity {

    ImageView note_save,iv_back5;
    EditText note_content;
    NoteSQLiteHelper mNotesSQLiteHelper;
    Intent intent;
    String id; //从RecordActivity传递过来的id
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newnotes);

        mNotesSQLiteHelper = new NoteSQLiteHelper(this);

        initdata();
        clickmethod();

    }

    private void clickmethod() {
        note_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!note_content.getText().toString().trim().equals("")){
                    //将数据存进数据库
                    if(mNotesSQLiteHelper.insertData(id, note_content.getText().toString().trim(), NoteDBUtils.getTime() )){
                        showToast("保存成功");
                        setResult(2);
                        finish();
                    }
                    else {
                        showToast("保存失败");
                    }

                }else{
                    showToast("输入为空！");
                }
            }
        });
        iv_back5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initdata() {
        note_save = (ImageView)findViewById(R.id.note_save);
        note_content = (EditText)findViewById(R.id.note_content);
        iv_back5 = (ImageView)findViewById(R.id.iv_back5);

        //获取从前一界面传递过来的数据
        intent = getIntent();
        id = intent.getStringExtra("_id");
    }


    public void showToast(String message){
        Toast.makeText(NewnotesActivity.this,message,Toast.LENGTH_SHORT).show();
    }
}
