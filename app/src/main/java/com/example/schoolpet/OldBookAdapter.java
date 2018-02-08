package com.example.schoolpet;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 林尤辉 on 2017/6/23.
 */

public class OldBookAdapter extends RecyclerView.Adapter<OldBookAdapter.ViewHolder> {
    private Context mContext;
    private List<OldBookDatebase> mOldBookList=new ArrayList<>();
    private OnItemClickListener mOnItemClickListener;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView ownerPhone;
        TextView oldBookName;
        TextView oldBookDepartment;

        public ViewHolder(View view){
            super(view);
            ownerPhone=(TextView)view.findViewById(R.id.oldBookItem_phone);
            oldBookName=(TextView)view.findViewById(R.id.oldBookItem_textView_book);
            oldBookDepartment=(TextView)view.findViewById(R.id.oldBookItem_textView_department);
        }
    }

    public  OldBookAdapter(List<OldBookDatebase>oldBookList){
        mOldBookList=oldBookList;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        if(mContext==null){
            mContext=parent.getContext();
        }
        View view= LayoutInflater.from(mContext).inflate(R.layout.oldbook_item,parent,false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(final ViewHolder holder, int position){
        OldBookDatebase oldBook=mOldBookList.get(position);
        holder.oldBookName.setText(oldBook.getBookName());
        holder.ownerPhone.setText(oldBook.getOwnerPhoneNumber());
        holder.oldBookDepartment.setText(oldBook.getDepartment());

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
        return mOldBookList.size();
    }

    public void setDatas(List<OldBookDatebase> list) {
        mOldBookList.clear();
        if (null != list) {
            mOldBookList.addAll(list);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }

    public void setOnItemClickListener(OldBookAdapter.OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }
}
