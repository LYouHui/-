package com.example.schoolpet;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by 林尤辉 on 2017/7/9.
 */

public class CreateUserDialog extends Dialog {
    Activity context;
    public Button button_sure;
    public static EditText editText_time;
    public static EditText editText_place;
    public RadioGroup radioGroup;
    public RadioButton radioButton_eating;
    public RadioButton radioButton_study;
    public RadioButton radioButton_sport;
    public RadioButton radioButton_play;
    public RadioButton radioButton_out;
    public User friend;
    public static int type;
    public static String time;
    public static String place;

    private View.OnClickListener mClickListener;

    public CreateUserDialog(Activity context) {
        super(context);
        this.context = context;
    }

    public CreateUserDialog(Activity context, int theme, View.OnClickListener clickListener) {
        super(context, theme);
        this.context = context;
        this.mClickListener = clickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 指定布局
        this.setContentView(R.layout.activity_create_user_dialog);

        button_sure=(Button)findViewById(R.id.createUserDialog_button_sure);
        editText_time=(EditText)findViewById(R.id.createUserDialog_editText_time);
        editText_place=(EditText)findViewById(R.id.createUserDialog_editText_place);
        radioGroup=(RadioGroup)findViewById(R.id.createUserDialog_radioGroup);
        radioButton_eating=(RadioButton)findViewById(R.id.createUserDialog_radioButton_eating);
        radioButton_study=(RadioButton)findViewById(R.id.createUserDialog_radioButton_study);
        radioButton_sport=(RadioButton)findViewById(R.id.createUserDialog_radioButton_sport);
        radioButton_play=(RadioButton)findViewById(R.id.createUserDialog_radioButton_play);
        radioButton_out=(RadioButton)findViewById(R.id.createUserDialog_radioButton_out);
        editText_time=(EditText)findViewById(R.id.createUserDialog_editText_time);
        editText_place=(EditText)findViewById(R.id.createUserDialog_editText_place);



        RadioButton radioButton = (RadioButton)findViewById(radioGroup.getCheckedRadioButtonId());
        /*
         * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
         * 对象,这样这可以以同样的方式改变这个Activity的属性.
         */
        Window dialogWindow = this.getWindow();

        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.8
        dialogWindow.setAttributes(p);

        // 为按钮绑定点击事件监听器
        button_sure.setOnClickListener(mClickListener);

        this.setCancelable(true);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //在这个函数里面用来改变选择的radioButton的数值，以及与其值相关的 //任何操作，详见下文 selectRadioBtn();
                User bmobUser = BmobUser.getCurrentUser(User.class);
                if (radioButton_eating.getId() == checkedId) {
                    type=2;
                } else if (radioButton_study.getId() == checkedId) {
                    type=3;
                } else if (radioButton_sport.getId() == checkedId) {
                    type=4;
                } else if (radioButton_play.getId() == checkedId) {
                    type=5;
                } else if (radioButton_out.getId() == checkedId) {
                    type=6;
                }
            }
        });
    }

    public void setFriend(User friend) {
        this.friend = friend;
    }

    public static String getTime() {
        return time=editText_time.getText().toString().trim();
    }

    public static String getPlace() {
        return place=editText_place.getText().toString().trim();
    }
}


