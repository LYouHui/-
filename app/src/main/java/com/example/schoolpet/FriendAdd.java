package com.example.schoolpet;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class FriendAdd extends AppCompatActivity {
    private Button searchUser;
    private EditText newFriendName;
    private List<User> newFriendList=new ArrayList<>();
    private RecyclerView recyclerView;
    private StrangerAdapter adapter;
    private LinearLayoutManager layoutManager;
    private User myFriend=new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        searchUser = (Button) findViewById(R.id.addFriend_button_fingNewFriend);
        newFriendName = (EditText) findViewById(R.id.addFriend_editText_inputFriendName);
        recyclerView=(RecyclerView)findViewById(R.id.addFriend_recyclerView);
        adapter =new StrangerAdapter(newFriendList);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        searchUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strangerName=newFriendName.getText().toString();
                if(strangerName.isEmpty()){
                    createDialog("用户名为空","请输入您所查找用户的用户名");
                }else{
                    BmobQuery<User> query = new BmobQuery<User>();
                    query.addWhereEqualTo("trueName", strangerName);
                    query.findObjects(new FindListener<User>() {
                        @Override
                        public void done(List<User> object, BmobException e) {
                            if(e==null){
                                if (object.isEmpty()==true){
                                    createDialog("查询用户失败","您所查找的用户不存在，请重新输入！");
                                }else{
                                    newFriendList.clear();
                                    for (User gameScore : object) {
                                        //获得信息
                                        User newFriend = new User(gameScore.gettureName(), gameScore.getdepartment());
                                        newFriend.setObjectId(gameScore.getObjectId());
                                        newFriendList.add(newFriend);
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                            }else{
                                createDialog("查询用户失败","发生未知错误，请再试一次！");
                            }
                        }
                    });
                }
            }
        });
        adapter.setOnItemClickListener(new StrangerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                myFriend.setObjectId(newFriendList.get(position).getObjectId());
                sendAddFriendMessage("发送好友请求","点击确定发送好友请求");
            }
        });

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

    public void sendAddFriendMessage(String title, String msg) {
        AlertDialog.Builder b=new AlertDialog.Builder(this);
        b.setMessage(msg);
        b.setTitle(title);
        b.setNegativeButton("确定", new DialogInterface.OnClickListener(){
            public void onClick(final DialogInterface dialog, int which){
                User userInfo = BmobUser.getCurrentUser(User.class);
                MessageDatebase message = new MessageDatebase(0,userInfo,myFriend);
                message.save(new SaveListener<String>() {
                    @Override
                    public void done(String objectId, BmobException e) {
                        if(e==null){
                            dialog.dismiss();
                        }else{
                            createDialog("发送好友请求失败","有股奇怪的力量阻止你们成为好友，请再试一次！");
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
        b.create().show();
    }
}