package com.example.schoolpet;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
/**
 * Created by 尤辉 on 2017/3/22.
 */

public class StrangerAdapter extends RecyclerView.Adapter<StrangerAdapter.ViewHolder> {
    private List<User> stranger;
    private OnItemClickListener mOnItemClickListener;
    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView friendImage;
        TextView friendName;
        TextView friendDepartment;
        //Button addFriend;

        public ViewHolder(View view){
            super(view);
            friendImage=(ImageView)view.findViewById(R.id.friendSearch_image_head);
            friendName=(TextView)view.findViewById(R.id.friendSearch_textView_userName);
            friendDepartment=(TextView)view.findViewById(R.id.friendSearch_textView_department);
            //addFriend=(Button)view.findViewById(R.id.friendSearch_button_addFriend);
        }
    }
    public StrangerAdapter() { };
    public StrangerAdapter(List<User> friendList){
        stranger=friendList;
    }
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_search_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        return holder;
    }
    public void onBindViewHolder(final ViewHolder holder, int position){
        User friend=stranger.get(position);
        holder.friendImage.setImageResource(friend.getImageId());
        holder.friendName.setText(friend.gettureName());
        holder.friendDepartment.setText(friend.getdepartment());

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
        return stranger.size();
    }

    public void setDatas(List<User> list) {
        stranger.clear();
        if (null != list) {
            stranger.addAll(list);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }
}
