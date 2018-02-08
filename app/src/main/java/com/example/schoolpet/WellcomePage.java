package com.example.schoolpet;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

public class WellcomePage extends AppCompatActivity {
    private Handler handler;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wellcome_page);
        android.support.v7.app.ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null)
            actionBar.hide();

        Bmob.initialize(this, "b4c67534c096410e19a5fbd5b8f6f950");
        final BmobUser bmobUser = BmobUser.getCurrentUser();
        Handler handler = new Handler();
        //当计时结束,跳转至主界面
        handler.postDelayed(new Runnable() {
            public void run() {
                if(bmobUser != null){
                    // 允许用户使用应用*/
                    Intent intent = new Intent(WellcomePage.this, PetPage.class);
                    startActivity(intent);
                    WellcomePage.this.finish();
                }else{
                    //缓存用户对象为空时， 可打开用户注册界面…
                    Intent intent = new Intent(WellcomePage.this, RegisterOrLogin.class);
                    startActivity(intent);
                    WellcomePage.this.finish();
                }
            }
        }, 3000);
    }
}
