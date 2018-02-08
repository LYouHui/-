package com.example.schoolpet;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 尤辉 on 2017/3/22.
 */

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {
    private List<User> mFriendList=new ArrayList<>();
    private OnItemClickListener mOnItemClickListener;

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView friendImage;
        TextView friendName;
        TextView friendLocation;

        public ViewHolder(View view){
            super(view);
            friendImage=(ImageView)view.findViewById(R.id.imageView_friendImage);
            friendName=(TextView)view.findViewById(R.id.textView_friendName);
            friendLocation=(TextView)view.findViewById(R.id.textView_friendLocation);
        }
    }
    public FriendAdapter(List<User> friendList){
        mFriendList=friendList;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_list_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }
    public void onBindViewHolder(final ViewHolder holder, int position){
        User friend=mFriendList.get(position);
        holder.friendImage.setImageResource(friend.getImageId());
        holder.friendName.setText(friend.gettureName());
        holder.friendLocation.setText(friend.getdepartment());
        //判断是否设置了监听器
        if(mOnItemClickListener != null){
            //为ItemView设置监听器
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition(); // 1
                    mOnItemClickListener.onItemClick(holder.itemView,position); // 2
                }
            });
        }
    }
    public int getItemCount(){
        return mFriendList.size();
    }

    public void setDatas(List<User> list) {
        mFriendList.clear();
        if (null != list) {
            mFriendList.addAll(list);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }
}
