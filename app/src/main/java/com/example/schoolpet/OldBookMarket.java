package com.example.schoolpet;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class OldBookMarket extends AppCompatActivity {
    private List<OldBookDatebase> oldBookList=new ArrayList<>();
    private OldBookAdapter adapter;
    private FloatingActionButton addOldBook;
    private OldBookDatebase oldBookDatebase=new OldBookDatebase();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old_book_market);

        initOldBooks();
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.oldBookMarket_recyclerView);
        addOldBook=(FloatingActionButton)findViewById(R.id.oldBookMarket_floatingActionButton);
        GridLayoutManager layoutManager=new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new OldBookAdapter(oldBookList);
        recyclerView.setAdapter(adapter);

        addOldBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(OldBookMarket.this,OldBookAdd.class);
                startActivity(intent);
            }
        });

        adapter.setOnItemClickListener(new OldBookAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                oldBookDatebase.setObjectId(oldBookList.get(position).getObjectId());
                oldBookDatebase.setOwner(oldBookList.get(position).getOwner());
                oldBookDatebase.setDepartment(oldBookList.get(position).getDepartment());
                oldBookDatebase.setBookName(oldBookList.get(position).getBookName());
                sendAddFriendMessage("发送借书请求","点击确定发送借书请求");
            }
        });
    }

    public void sendAddFriendMessage(String title, String msg) {
        AlertDialog.Builder b=new AlertDialog.Builder(this);
        b.setMessage(msg);
        b.setTitle(title);
        b.setNegativeButton("确定", new DialogInterface.OnClickListener(){
            public void onClick(final DialogInterface dialog, int which){
                User userInfo = BmobUser.getCurrentUser(User.class);
                MessageDatebase message = new MessageDatebase(1,userInfo,oldBookDatebase.getOwner(),oldBookDatebase);
                message.save(new SaveListener<String>() {
                    @Override
                    public void done(String objectId, BmobException e) {
                        if(e==null){
                            dialog.dismiss();
                        }else{
                            createDialog("发送借书请求失败","有股奇怪的力量阻止了你们的交易，请再试一次！");
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
        b.create().show();
    }

    private void initOldBooks(){
        BmobQuery<OldBookDatebase> query = new BmobQuery<OldBookDatebase>();
        query.addWhereEqualTo("valid", true);
        query.setLimit(50);
        query.findObjects(new FindListener<OldBookDatebase>() {
            @Override
            public void done(List<OldBookDatebase> object, BmobException e) {
                if(e==null){
                    oldBookList.clear();
                    for (OldBookDatebase gameScore : object) {
                        //获得信息
                        OldBookDatebase newFriend = new OldBookDatebase();
                        newFriend.setObjectId(gameScore.getObjectId());
                        newFriend.setBookName(gameScore.getBookName());
                        newFriend.setDepartment(gameScore.getDepartment());
                        newFriend.setOwner(gameScore.getOwner());
                        newFriend.setOwnerPhoneNumber(gameScore.getOwnerPhoneNumber());
                        oldBookList.add(newFriend);
                    }
                    adapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(OldBookMarket.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
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
}
