package com.example.schoolpet;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class Register extends AppCompatActivity {
    public Spinner spinner_hometown;
    public Spinner spinner_dormitory;
    public Spinner spinner_department;
    public EditText edit_trueName;
    public EditText edit_petName;
    public EditText edit_roomNumber;
    private EditText edit_user;
    private EditText edit_password;
    private EditText edit_password_2;
    private List<String> data_list1;
    private ArrayAdapter<String> arr_adapter1;
    private List<String> data_list2;
    private ArrayAdapter<String> arr_adapter2;
    private List<String> data_list3;
    private ArrayAdapter<String> arr_adapter3;
    public Button next;
    private String hometown;
    private String dormitory;
    private String department;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        spinner_hometown = (Spinner) findViewById(R.id.spinner_hometown);
        spinner_dormitory = (Spinner) findViewById(R.id.spinner_dormitory);
        spinner_department=(Spinner)findViewById(R.id.spinner_department);
        edit_user = (EditText) findViewById(R.id.register_editText_IDcard);
        edit_password = (EditText) findViewById(R.id.register_ediText_password);
        edit_password_2 = (EditText) findViewById(R.id.register_editText_password2);
        edit_trueName=(EditText)findViewById(R.id.register_editText_trueName);
        edit_petName=(EditText)findViewById(R.id.register_editText_petName);
        edit_roomNumber=(EditText)findViewById(R.id.register_editText_roomNumber) ;
        next = (Button) findViewById(R.id.register_pet_button_sure);
        //数据
        data_list1 = new ArrayList<String>();
        data_list1.add("北京");
        data_list1.add("广东");
        data_list1.add("山东");
        data_list1.add("江苏");
        data_list1.add("河南");
        data_list1.add("上海");
        data_list1.add("河北");
        data_list1.add("浙江");
        data_list1.add("香港");
        data_list1.add("陕西");
        data_list1.add("湖南");
        data_list1.add("重庆");
        data_list1.add("福建");
        data_list1.add("天津");
        data_list1.add("云南");
        data_list1.add("四川");
        data_list1.add("广西");
        data_list1.add("安徽");
        data_list1.add("海南");
        data_list1.add("江西");
        data_list1.add("山西");
        data_list1.add("辽宁");
        data_list1.add("台湾");
        data_list1.add("黑龙江");
        data_list1.add("内蒙古");
        data_list1.add("澳门");
        data_list1.add("贵州");
        data_list1.add("甘肃");
        data_list1.add("青海");
        data_list1.add("新疆");
        data_list1.add("西藏");
        data_list1.add("吉林");
        data_list1.add("宁夏");

        data_list2 = new ArrayList<String>();
        data_list2.add("梅一A");
        data_list2.add("梅一B");
        data_list2.add("梅一C");
        data_list2.add("梅一D");
        data_list2.add("梅二A");
        data_list2.add("梅二B");
        data_list2.add("梅二C");
        data_list2.add("梅二D");
        data_list2.add("梅三A");
        data_list2.add("梅三B");
        data_list2.add("梅三C");
        data_list2.add("梅三D");
        data_list2.add("梅四A");
        data_list2.add("梅四B");
        data_list2.add("梅四C");
        data_list2.add("梅四D");
        data_list2.add("梅五A");
        data_list2.add("梅五B");
        data_list2.add("梅五C");
        data_list2.add("梅五D");
        data_list2.add("梅六A");
        data_list2.add("梅六B");
        data_list2.add("梅六C");
        data_list2.add("梅六D");
        data_list2.add("梅七A");
        data_list2.add("梅七B");
        data_list2.add("梅七C");
        data_list2.add("梅七D");
        data_list2.add("梅八A");
        data_list2.add("梅八B");
        data_list2.add("梅八C");
        data_list2.add("梅八D");
        data_list2.add("梅九A");
        data_list2.add("梅九B");
        data_list2.add("梅九C");
        data_list2.add("梅九D");
        data_list2.add("桃一A");
        data_list2.add("桃一B");
        data_list2.add("桃一C");
        data_list2.add("桃一D");
        data_list2.add("桃二A");
        data_list2.add("桃二B");
        data_list2.add("桃二C");
        data_list2.add("桃二D");
        data_list2.add("桃三A");
        data_list2.add("桃三B");
        data_list2.add("桃三C");
        data_list2.add("桃三D");
        data_list2.add("桃四A");
        data_list2.add("桃四B");
        data_list2.add("桃四C");
        data_list2.add("桃四D");
        data_list2.add("桃五A");
        data_list2.add("桃五B");
        data_list2.add("桃五C");
        data_list2.add("桃五D");
        data_list2.add("桃六A");
        data_list2.add("桃六B");
        data_list2.add("桃六C");
        data_list2.add("桃六D");
        data_list2.add("桃七A");
        data_list2.add("桃七B");
        data_list2.add("桃七C");
        data_list2.add("桃七D");

        data_list3 = new ArrayList<String>();
        data_list3.add("建筑学院");data_list3.add("机械工程学院");
        data_list3.add("能源与环境学院");data_list3.add("信息科学与工程学院");
        data_list3.add("土木工程学院");data_list3.add("电子科学与工程学院");
        data_list3.add("数学学院");data_list3.add("自动化学院");
        data_list3.add("计算机科学与工程学院");data_list3.add("物理学院");
        data_list3.add("生物医学与工程学院");data_list3.add("材料科学与工程学院");
        data_list3.add("人文学院");data_list3.add("经济管理学院");
        data_list3.add("电气工程学院");data_list3.add("外国语学院");
        data_list3.add("化学与化工学院");data_list3.add("交通学院");
        data_list3.add("仪器科学与工程学院");data_list3.add("艺术学院");
        data_list3.add("法学院");data_list3.add("医学院");
        data_list3.add("公共卫生学院");data_list3.add("吴健雄学院");
        data_list3.add("软件学院");
        //适配器
        arr_adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list1);
        arr_adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list2);
        arr_adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list3);
        //设置样式
        arr_adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        arr_adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        arr_adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spinner_hometown.setAdapter(arr_adapter1);
        spinner_dormitory.setAdapter(arr_adapter2);
        spinner_department.setAdapter(arr_adapter3);

        spinner_hometown.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                hometown = data_list1.get(arg2);
                //设置显示当前选择的项
                arg0.setVisibility(View.VISIBLE);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        spinner_dormitory.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                dormitory = data_list2.get(arg2);
                //设置显示当前选择的项
                arg0.setVisibility(View.VISIBLE);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        spinner_department.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
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

        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final String user_name = edit_user.getText().toString().trim();
                final String m_password = edit_password.getText().toString().trim();
                String m_password_2 = edit_password_2.getText().toString().trim();
                boolean judge_password = isnotSimple(m_password);
                boolean judge_user = isnotSimple(user_name);

                if (!m_password.equals(m_password_2) || m_password.isEmpty()) {
                    createDialog("用户名为空", "您是不是把用户名给忘了？");
                } else if (user_name.isEmpty()) {
                    createDialog("您的用户名有点短~", "能再长一点吗？");
                } else if (judge_password == false) {
                    createDialog("密码过于朴素", "您的密码有点简单了哦，换一个试试");
                } else if (judge_user == false) {
                    createDialog("密码错误", "两次输入的密码不同哦");
                }
                else {
                    String petName = edit_petName.getText().toString().trim();
                    String trueName = edit_trueName.getText().toString().trim();
                    String roomNumber = edit_roomNumber.getText().toString().trim();
                    User bu = new User();
                    bu.setUsername(user_name);
                    bu.setPassword(m_password);
                    bu.sethometown(hometown);
                    bu.setdormitory(dormitory);
                    bu.setdepartment(department);
                    bu.settureName(trueName);
                    bu.setpetName(petName);
                    bu.setroomNumber(roomNumber);
                    bu.setHealth(100);
                    bu.setMood(100);
                    bu.setIsEvenHungry(true);
                    bu.setIsEvenEating(false);
                    bu.setImageId(0);
                    bu.signUp(new SaveListener<User>() {
                        public void done(final User s, BmobException e) {
                            if(e==null) {
                                //自动登录
                                BmobUser bu2 = new BmobUser();
                                bu2.setUsername(user_name);
                                bu2.setPassword(m_password);
                                bu2.login(new SaveListener<BmobUser>() {
                                    public void done(BmobUser bmobUser, BmobException e) {
                                        if(e==null){
                                            //在好友数据库中添加该用户
                                            User user = BmobUser.getCurrentUser(User.class);
                                            FriendDatebase myFriendDatabase=new FriendDatebase();
                                            myFriendDatabase.setMyUser(user);
                                            BmobRelation relation=new BmobRelation();
                                            relation.add(user);
                                            myFriendDatabase.setFriend(relation);
                                            myFriendDatabase.save(new SaveListener<String>() {
                                                @Override
                                                public void done(String objectId,BmobException e) {
                                                    if(e==null){
                                                    }else{
                                                        Toast.makeText(Register.this,"创建好友数据失败，请联系客服",Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                            //继续创建位置数据库
                                            LocationDatebase locationDatebase=new LocationDatebase();
                                            locationDatebase.setUser(user);
                                            locationDatebase.setLocation(0,0);
                                            locationDatebase.save(new SaveListener<String>() {
                                                @Override
                                                public void done(String objectId,BmobException e) {
                                                    if(e==null){
                                                    }else{
                                                        Toast.makeText(Register.this,"创建位置数据失败，请联系客服",Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                            Intent intent=new Intent(Register.this,PetPage.class);
                                            startActivity(intent);
                                        }else{
                                            createDialog("自动登录失败",e.getMessage()+e.getErrorCode());
                                        }
                                    }
                                });
                            }else{
                                createDialog("注册失败",e.getMessage());
                            }
                        }
                    });
                }
            }
        });
    }
    private boolean isnotSimple(String mstring){
        String num="^[0-9A-Za-z_+-/*%]{6,20}$";
        if(TextUtils.isEmpty(mstring)){
            return false;
        }
        else{
            return mstring.matches(num);//字符串是否在给定的正则表达式匹配
        }
    }
    private void createDialog(String title, String msg) {
        AlertDialog.Builder b=new AlertDialog.Builder(this);
        b.setMessage(msg);
        b.setTitle(title);
        b.setNegativeButton("重试", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int which){
                dialog.dismiss();
            }
        });
        b.create().show();
    }
}
