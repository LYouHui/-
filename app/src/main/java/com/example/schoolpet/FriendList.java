package com.example.schoolpet;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class FriendList extends AppCompatActivity {
    private static List<User> friendList=new ArrayList<>();
    private static User bmobUser = BmobUser.getCurrentUser(User.class);
    private User myFriend=new User();
    private CreateUserDialog createUserDialog;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        initFriend();
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.reccycler_view_friendList);
        textView=(TextView)findViewById(R.id.friendList_textView);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        FriendAdapter adapter=new FriendAdapter(friendList);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new FriendAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                myFriend.setObjectId(friendList.get(position).getObjectId1());
                showEditDialog(view,myFriend);
            }
        });
    }

    private void initFriend() {
        //加载好友数据
        //查询用户的好友数据库
        BmobQuery<FriendDatebase> query = new BmobQuery<FriendDatebase>();
        query.addWhereEqualTo("myUser", bmobUser);
        query.findObjects(new FindListener<FriendDatebase>() {
            @Override
            public void done(List<FriendDatebase> object,BmobException e) {
                if(e==null){
                    FriendDatebase friendDatebase=new FriendDatebase();
                    friendDatebase.setObjectId(object.get(0).getObjectId());//这步完成，friendDatebase即是本地用户的好友数据库

                    BmobQuery<User> query = new BmobQuery<User>();
                    query.addWhereRelatedTo("friend", new BmobPointer(friendDatebase));
                    query.findObjects(new FindListener<User>() {
                        @Override
                        public void done(List<User> object,BmobException e) {
                            if(e==null){
                                if(object.isEmpty()==true){
                                    textView.setText("您的好友数据为空，快去添加几个好友吧！");
                                }
                                List<User> friendList1=new ArrayList<>();
                                for (User gameScore : object) {
                                    //获得信息
                                    User friend = new User(gameScore.gettureName(), gameScore.getdepartment(),gameScore.getObjectId());
                                    friendList1.add(friend);
                                }
                                if(friendList!=friendList1)
                                    friendList=friendList1;
                            }else{
                                createDialog("失败",e.getErrorCode()+e.getMessage());
                            }
                        }
                    });
                }else{
                    createDialog("失败",e.getErrorCode()+e.getMessage());
                }
            }
        });
    }


    private void createDialog(String title, String msg) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setMessage(msg);
        b.setTitle(title);
        b.setNegativeButton("重试", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        b.create().show();
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.createUserDialog_button_sure:
                    String time=CreateUserDialog.getTime();
                    String place=CreateUserDialog.getPlace();

                    if(time.isEmpty()==true){
                        createDialog("邀约时间为空","没有时间，小伙伴怎么找你呢，回去填一下吧");
                    }
                    if(time.isEmpty()==true){
                        createDialog("邀约地点为空","没有地点，小伙伴怎么找你呢，回去填一下吧");
                    }
                    else{
                        sendMessage(CreateUserDialog.type,bmobUser,myFriend,time,place);
                        createUserDialog.dismiss();
                    }
                    break;
            }
        }
    };
    public void showEditDialog(View view,User myFriend) {
        createUserDialog = new CreateUserDialog(this,R.style.Theme_AppCompat_Light_Dialog,onClickListener);
        createUserDialog.show();
        createUserDialog.setFriend(myFriend);
    }

    public void sendMessage(int type,User myUser,User friend,String time,String place){
        MessageDatebase gameScore = new MessageDatebase(type,myUser,friend,time,place);
        gameScore.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if(e==null){

                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }
}
