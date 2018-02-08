package com.example.schoolpet;

import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.Manifest;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

public class OldBookAdd extends AppCompatActivity {
    private Spinner mySpinner;
    public String department;
    private List<String> data_list3;
    private ArrayAdapter<String> arr_adapter3;
    private Button sure;
    private Button carcer;
    private EditText bookName;
    private EditText ownerPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_old_book);

        mySpinner = (Spinner) findViewById(R.id.oldBookAdd_spinner);
        sure = (Button) findViewById(R.id.addOldBook_button_sure);
        carcer = (Button) findViewById(R.id.addOldBook_button_carcer);
        bookName = (EditText) findViewById(R.id.addOldBook_editText_bookName);
        ownerPhone=(EditText)findViewById(R.id.addOldBook_editText_ownerPhone);

        data_list3 = new ArrayList<String>();
        data_list3.add("大类课程，部分院系适用");
        data_list3.add("通识选修，全校适用");
        data_list3.add("建筑学院");
        data_list3.add("机械工程学院");
        data_list3.add("能源与环境学院");
        data_list3.add("信息科学与工程学院");
        data_list3.add("土木工程学院");
        data_list3.add("电子科学与工程学院");
        data_list3.add("数学学院");
        data_list3.add("自动化学院");
        data_list3.add("计算机科学与工程学院");
        data_list3.add("软件学院");
        data_list3.add("物理学院");
        data_list3.add("生物医学与工程学院");
        data_list3.add("材料科学与工程学院");
        data_list3.add("人文学院");
        data_list3.add("经济管理学院");
        data_list3.add("电气工程学院");
        data_list3.add("外国语学院");
        data_list3.add("化学与化工学院");
        data_list3.add("交通学院");
        data_list3.add("仪器科学与工程学院");
        data_list3.add("艺术学院");
        data_list3.add("法学院");
        data_list3.add("医学院");
        data_list3.add("公共卫生学院");
        data_list3.add("吴健雄学院");
        arr_adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list3);
        arr_adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(arr_adapter3);

        mySpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                department = data_list3.get(arg2);
                //设置显示当前选择的项
                arg0.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        carcer.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent=new Intent(OldBookAdd.this,OldBookMarket.class);
                startActivity(intent);
            }
        });

        sure.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                final String myBookName=bookName.getText().toString().trim();
                final String myOwnerPhone=ownerPhone.getText().toString().trim();
                User user = BmobUser.getCurrentUser(User.class);

                OldBookDatebase gameScore = new OldBookDatebase();
                gameScore.setBookName(myBookName);
                gameScore.setDepartment(department);
                gameScore.setOwnerPhoneNumber(myOwnerPhone);
                gameScore.setOwner(user);
                gameScore.setValid(true);
                gameScore.save(new SaveListener<String>() {
                    @Override
                    public void done(String objectId, BmobException e) {
                        if(e==null){
                            createDialog("旧书发布成功","您提交的旧书信息已经成功保存，需要者会根据您提供的联系方式联系您");
                        }else{
                            Toast.makeText(OldBookAdd.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }

    private void createDialog(String title, String msg) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setMessage(msg);
        b.setTitle(title);
        b.setNegativeButton("好的", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent=new Intent(OldBookAdd.this,OldBookMarket.class);
                startActivity(intent);
            }
        });
        b.create().show();
    }
}
