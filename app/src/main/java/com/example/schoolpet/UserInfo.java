package com.example.schoolpet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

public class UserInfo extends AppCompatActivity {
    public TextView userName;
    public TextView trueName;
    public TextView petName;
    public TextView department;
    public TextView dormitory;
    public TextView howntown;
    public Button exitLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        userName=(TextView)findViewById(R.id.userInfo_textView_showUserName);
        trueName=(TextView)findViewById(R.id.userInfo_textView_trueName);
        petName=(TextView)findViewById(R.id.userInfo_textView_petName);
        department=(TextView)findViewById(R.id.userInfo_textView_department);
        dormitory=(TextView)findViewById(R.id.userInfo_textView_dormitory);
        howntown=(TextView)findViewById(R.id.userInfo_textView_hometown);
        exitLogin=(Button)findViewById(R.id.userInfo_button_exitLogin);

        String str_userName= (String) BmobUser.getObjectByKey("username");
        String str_trueName= (String) BmobUser.getObjectByKey("trueName");
        String str_petName= (String) BmobUser.getObjectByKey("petName");
        String str_dormitory= (String) BmobUser.getObjectByKey("dormitory")+(String)BmobUser.getObjectByKey("roomNumber");
        String str_department= (String) BmobUser.getObjectByKey("department");
        String str_howntown= (String) BmobUser.getObjectByKey("hometown");

        String[] userInfo={str_userName,str_trueName,str_petName,str_dormitory,str_department,str_howntown};
        TextView[] textView={userName,trueName,petName,dormitory,department,howntown};
        for(int i=0;i<6;i++) {
            StringBuilder currentPosition = new StringBuilder();
            currentPosition.append(userInfo[i]);
            textView[i].setText(currentPosition);
        }

        exitLogin.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                BmobUser.logOut();   //清除缓存用户对象
                Intent intent=new Intent(UserInfo.this,RegisterOrLogin.class);
                startActivity(intent);
            }
        });
    }
}
